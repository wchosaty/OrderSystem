package aln.ktversion.ordersystem.customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import aln.ktversion.ordersystem.R;
import aln.ktversion.ordersystem.itemclass.Order;
import aln.ktversion.ordersystem.itemclass.OrderId;
import aln.ktversion.ordersystem.itemclass.Product;
import aln.ktversion.ordersystem.network.RemoteAccess;
import aln.ktversion.ordersystem.tool.Common;
import aln.ktversion.ordersystem.tool.LogHistory;

public class CustomerOrderListFragment extends Fragment {
    private static final String TAG = "TAG CustomerOrderListFragment";
    private RecyclerView recyclerView_order,recyclerView_id;
    private SwipeRefreshLayout swipeRefreshLayout_order,swipeRefreshLayout_id;
    private TextView tvTitleOrderInf;
    private Order itemOrder;
    private List<OrderId> orderIdList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_order_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initial();
        handleRefresh();
    }

    private void handleRefresh() {
        swipeRefreshLayout_id.setOnRefreshListener(()->{
            orderIdList = getOrderIdList();
            showOrderIdList();
            swipeRefreshLayout_id.setRefreshing(false);
        });
        swipeRefreshLayout_order.setOnRefreshListener(()->{
            swipeRefreshLayout_order.setRefreshing(false);
        });
    }

    private void initial() {
        recyclerView_id.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView_order.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderIdList = getOrderIdList();
        showOrderIdList();
    }

    private void showOrderIdList() {
        if(Objects.equals(orderIdList,null) || orderIdList.isEmpty()){
//            LogHistory.d(TAG,"showOrderIds : null or empty");
            return;
        }
        IdAdapter idAdapter = (IdAdapter) recyclerView_id.getAdapter();
        if(idAdapter == null){
            recyclerView_id.setAdapter(new IdAdapter(orderIdList));
        }else{
            idAdapter.setList(orderIdList);
            idAdapter.notifyDataSetChanged();
        }
    }

    private void showItemOrder() {
        if(Objects.equals(itemOrder,null)){
            LogHistory.d(TAG,"itemOrder : null");
            return;
        }
        OrderAdapter orderAdapter = (OrderAdapter) recyclerView_order.getAdapter();
        if(orderAdapter == null){
            recyclerView_order.setAdapter(new OrderAdapter(itemOrder,itemOrder.getList()));
        }else{
            orderAdapter.setList(itemOrder,itemOrder.getList());
            orderAdapter.notifyDataSetChanged();
        }
    }

    private List<OrderId> getOrderIdList() {
        JsonObject jsonObject = new JsonObject();
        String url = RemoteAccess.URL+"MyProductServlet";
        jsonObject.addProperty("action", Common.ALL_ID);
        jsonObject.addProperty("data",Common.ALL_ID);
        String backString = RemoteAccess.accessProduct(url,jsonObject.toString());
        Log.d(TAG,"back id :"+backString);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Type type = new TypeToken<List<OrderId>>() {}.getType();
        return gson.fromJson(backString,type);
    }

    private Order getSingleOrder(Long orderId) {
        JsonObject jsonObject = new JsonObject();
        String url = RemoteAccess.URL+"MyProductServlet";
        //findId
        jsonObject.addProperty("action",Common.QUERY_ORDER);
        jsonObject.addProperty("data",Common.QUERY_ORDER);
        jsonObject.addProperty("orderId",orderId);

        String backString = RemoteAccess.accessProduct(url,jsonObject.toString());
        LogHistory.d(TAG,"back :"+backString);
        return new Gson().fromJson(backString,Order.class);
    }

    private Order getAllOrder() {
        JsonObject jsonObject = new JsonObject();
        String url = RemoteAccess.URL+"MyProductServlet";
        // getAll
        jsonObject.addProperty("action",Common.ALL_ORDER);
        jsonObject.addProperty("data",Common.ALL_ORDER);
        String backString = RemoteAccess.accessProduct(url,jsonObject.toString());
        LogHistory.d(TAG,"back :"+backString);
        return new Gson().fromJson(backString,Order.class);
    }


    private void findViews(View view) {
        recyclerView_order = view.findViewById(R.id.recyclerView_CustomerOrder);
        swipeRefreshLayout_order = view.findViewById(R.id.swipeRefreshLayout_CustomerOrder);
        recyclerView_id = view.findViewById(R.id.recyclerView_OrderId);
        swipeRefreshLayout_id = view.findViewById(R.id.swipeRefreshLayout_OrderId);
        tvTitleOrderInf = view.findViewById(R.id.tvTitleOrderInf_CustomerOrder);
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
        private final LayoutInflater layoutInflater;
        private Order order;
        private List<Product> list;

        private OrderAdapter(Order order,List<Product> list) {
            this.list = list;
            layoutInflater = LayoutInflater.from(requireContext());
            this.order = order;
        }

        public void setList(Order order,List<Product> list) {
            this.order = order;
            this.list = list;
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : order.getList().size();
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_customer_order,parent,false);
            return new OrderViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Product product = list.get(position);
//            LogHistory.d(TAG,"OrderAdapter position:"+position);
            if(!product.getTitleFlag()){
                // 顯示商品
                holder.tvOrderId.setVisibility(View.GONE);
                holder.tvTitleMessage.setVisibility(View.GONE);
                holder.tvName.setVisibility(View.VISIBLE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvName.setText(product.getName());
                holder.tvStatus.setText(product.getStatus());
            }else{
                // 顯示Title
                holder.tvOrderId.setVisibility(View.VISIBLE);
                holder.tvTitleMessage.setVisibility(View.VISIBLE);
                holder.tvName.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.GONE);
                holder.tvOrderId.setText(getString(R.string.orderid)+order.getOrder_id());
//                holder.tvTitleMessage.setText(order.getStatus());
                holder.tvTitleMessage.setText(getString(R.string.priceSymbol)+order.getTotalPay());
            }
        }


        class OrderViewHolder extends RecyclerView.ViewHolder{
            private TextView tvOrderId,tvName,tvStatus,tvTitleMessage;

            public OrderViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrderId = itemView.findViewById(R.id.tvTitle_CustomerOrder);
                tvName = itemView.findViewById(R.id.tvName_CustomerOrder);
                tvStatus = itemView.findViewById(R.id.tvStatus_CustomerOrder);
                tvTitleMessage = itemView.findViewById(R.id.tvTitleMessage_CustomerOrder);
            }
        }
    }

    private class IdAdapter extends RecyclerView.Adapter<IdAdapter.IdViewHolder> {
        LayoutInflater layoutInflater;
        List<OrderId> list;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public IdAdapter(List<OrderId> list) {
            layoutInflater = LayoutInflater.from(requireContext());
            this.list = list;
        }


        public void setList(List<OrderId> list) {
            this.list = list;
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        @NonNull
        @Override
        public IdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_orderid,parent,false);
            return new IdViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull IdViewHolder holder, int position) {
            final OrderId orderId = list.get(position);

            holder.tvOrderId.setText(getString(R.string.orderid) + orderId.getOrder_id());
            holder.tvStatus.setText(orderId.getStatus());
            holder.tvTime.setText(sdf.format(orderId.getStart_time()));
            holder.itemView.setOnClickListener(v -> {
                itemOrder = null;
                itemOrder = getSingleOrder(orderId.getOrder_id());
                LogHistory.d(TAG,"orderList size :"+itemOrder.getList().size());
                showItemOrder();
            });
        }


        class IdViewHolder extends RecyclerView.ViewHolder {
            TextView tvOrderId,tvStatus,tvTime;

            public IdViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrderId = itemView.findViewById(R.id.tvOrderId_ItemId);
                tvStatus = itemView.findViewById(R.id.tvStatus_itemId);
                tvTime = itemView.findViewById(R.id.tvTime_itemId);
            }
        }
    }
}