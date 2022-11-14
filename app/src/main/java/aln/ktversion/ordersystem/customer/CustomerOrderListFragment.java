package aln.ktversion.ordersystem.customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Objects;

import aln.ktversion.ordersystem.R;
import aln.ktversion.ordersystem.itemclass.Order;
import aln.ktversion.ordersystem.itemclass.Product;
import aln.ktversion.ordersystem.network.RemoteAccess;
import aln.ktversion.ordersystem.tool.Common;

public class CustomerOrderListFragment extends Fragment {
    private static final String TAG = "TAG CustomerOrderListFragment";
    private RecyclerView recyclerView_order,recyclerView_id;
    private SwipeRefreshLayout swipeRefreshLayout_order,swipeRefreshLayout_id;
    private TextView tvTitleOrderInf;
    private Order order;
    private List<String> orderIdList;

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
    }

    private void initial() {
        recyclerView_id.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderIdList = getOrderIdList();
//        order = getOrder();
//        showOrders();
    }

    private List<String> getOrderIdList() {
        JsonObject jsonObject = new JsonObject();
        String url = RemoteAccess.URL+"MyProductServlet";
        jsonObject.addProperty("action", Common.ALL_ID);
        jsonObject.addProperty("data",Common.ALL_ID);
        return null;
    }


    private void showOrders() {
        if(Objects.equals(order,null) ){
            Toast.makeText(requireContext(), R.string.orderisEmpty, Toast.LENGTH_SHORT).show();
            return;
        }
        //////
    }

    private Order getOrder() {
        JsonObject jsonObject = new JsonObject();
        String url = RemoteAccess.URL+"MyProductServlet";
        jsonObject.addProperty("action", Common.ALL_ORDER);
        jsonObject.addProperty("data",Common.ALL_ORDER);

        String backString = RemoteAccess.accessProduct(url,jsonObject.toString());
//        LogHistory.d(TAG,"back :"+backString);
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

        private OrderAdapter(Order order) {
            this.list = list;
            layoutInflater = LayoutInflater.from(requireContext());
            this.order = order;
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : order.getList().size();
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.fragment_customer_order_list,parent,false);
            return new OrderViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Product product = list.get(position);
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
                holder.tvTitleMessage.setText(order.getStatus());
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
}