package aln.ktversion.ordersystem.store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
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
import aln.ktversion.ordersystem.customer.CustomerOrderListFragment;
import aln.ktversion.ordersystem.itemclass.Order;
import aln.ktversion.ordersystem.itemclass.OrderId;
import aln.ktversion.ordersystem.itemclass.Product;
import aln.ktversion.ordersystem.network.RemoteAccess;
import aln.ktversion.ordersystem.tool.Common;
import aln.ktversion.ordersystem.tool.LogHistory;

public class OrderListFragment extends Fragment {
    private static final String TAG = "TAG OrderListFragment";
    private RecyclerView recyclerView_id,recyclerView_order;
    private Order itemOrder;
    private List<OrderId> orderIdList;
    private Gson gsonDate;
    SimpleDateFormat sdf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initial();
    }

    private void initial() {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gsonDate = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        recyclerView_id.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView_order.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderIdList = getOrderIdList();
        showOrderIdList();
    }

    private void showOrderIdList() {
        if(Objects.equals(orderIdList,null) || orderIdList.isEmpty()){
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
        LogHistory.d(TAG,"getOrderIdList back:"+ backString);
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
        return gsonDate.fromJson(backString,Order.class);
    }

    private void findViews(View view) {
        recyclerView_id = view.findViewById(R.id.recyclerViewId_StoreOrderList);
        recyclerView_order = view.findViewById(R.id.recyclerViewOrder_StoreOrderList);
    }

    private class IdAdapter extends RecyclerView.Adapter<IdAdapter.IdViewHolder> {
        LayoutInflater layoutInflater;
        List<OrderId> list;


        public IdAdapter(List<OrderId> list) {
            layoutInflater = LayoutInflater.from(requireContext());
            this.list = list;
        }

        public void setList(List<OrderId> list){
            this.list = this.list;
        }

        @Override
         public int getItemCount() {
             return list == null ? 0 : list.size();
         }

         @NonNull
         @Override
         public IdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View itemView = layoutInflater.inflate(R.layout.item_store_orderid,parent,false);
             return new IdViewHolder(itemView);
         }

         @Override
         public void onBindViewHolder(@NonNull IdViewHolder holder, int position) {
            final OrderId orderId = list.get(position);
             String  url = RemoteAccess.URL+"MyProductServlet";
            holder.tvOrderId.setText(getString(R.string.orderid)+ orderId.getOrder_id());
            holder.tvCustomerName.setText(orderId.getCustomer_name());
             holder.tvTime.setText(sdf.format(orderId.getStart_time()));
             holder.tvStatus.setText(orderId.getStatus());

             holder.itemView.setOnLongClickListener(v -> {
                 PopupMenu popupMenuId = new PopupMenu(requireContext(),v, Gravity.END);
                 popupMenuId.inflate(R.menu.popup_menu);
                 popupMenuId.setOnMenuItemClickListener(item -> {
                     int itemId = item.getItemId();
                     JsonObject j = new JsonObject();
                        switch (itemId){
                            case R.id.finish:
                                j.addProperty("action",Common.UPDATE_ID);
                                orderId.setStatus(Common.STATUS_PRODUCT_FINISH);
                                j.addProperty("data",gsonDate.toJson(orderId));
                                String backfinish = RemoteAccess.accessProduct(url,j.toString());
                                orderIdList = getOrderIdList();
                                showOrderIdList();
                                Toast.makeText(requireContext(), "finish", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.cancel:
                                // 測試delete
//                                j.addProperty("action",Common.DELETE_ID);
//                                j.addProperty("data",gsonDate.toJson(orderId));
//                                String backdelete = RemoteAccess.accessProduct(url,j.toString());

                                j.addProperty("action",Common.UPDATE_ID);
                                orderId.setStatus(Common.STATUS_CANCEL);
                                j.addProperty("data",gsonDate.toJson(orderId));
                                String backcanecl = RemoteAccess.accessProduct(url,j.toString());

                                orderIdList = getOrderIdList();
                                showOrderIdList();
                                Toast.makeText(requireContext(), "cancel", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.end:
                                j.addProperty("action",Common.UPDATE_ID);
                                orderId.setStatus(Common.STATUS_END);
                                j.addProperty("data",gsonDate.toJson(orderId));
                                // 測試順便更新order detail
//                                j.addProperty("data2",Common.UPDATE_FINDID_ORDER);

                                String backend = RemoteAccess.accessProduct(url,j.toString());
                                orderIdList = getOrderIdList();
                                showOrderIdList();
                                Toast.makeText(requireContext(),"end",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.preReady:

                                j.addProperty("action",Common.UPDATE_ID);
                                orderId.setStatus(Common.STATUS_PREREADY);
                                j.addProperty("data",gsonDate.toJson(orderId));
                                String backpreReady = RemoteAccess.accessProduct(url,j.toString());
                                orderIdList = getOrderIdList();
                                showOrderIdList();

                                Toast.makeText(requireContext(), "preReady", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return true;
                 });
                 popupMenuId.show();
                 return true;
             });

             holder.itemView.setOnClickListener(v -> {
                 itemOrder = null;
                 itemOrder = getSingleOrder(orderId.getOrder_id());
                 LogHistory.d(TAG,"orderList size :"+itemOrder.getList().size());
                 showItemOrder();
             });
         }



        class IdViewHolder extends RecyclerView.ViewHolder{
            TextView tvOrderId,tvCustomerName,tvTime,tvStatus;

            public IdViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrderId = itemView.findViewById(R.id.tvOrderId_StoreOrderId);
                tvCustomerName = itemView.findViewById(R.id.tvCustomerName_StoreOrderId);
                tvTime = itemView.findViewById(R.id.tvTime_StoreOrderId);
                tvStatus = itemView.findViewById(R.id.tvStatus_StoreOrderId);

            }
        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
        LayoutInflater layoutInflater;
        Order order;
        List<Product> list;

        public OrderAdapter(Order order, List<Product> list) {
            layoutInflater = LayoutInflater.from(requireContext());
            this.order = order;
            this.list = list;
        }
        public void setList(Order order,List<Product> list){
            this.list = list;
            this.order = order;
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_store_order_list,parent,false);
            return new OrderViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            String  url = RemoteAccess.URL+"MyProductServlet";
            Product product = list.get(position);

            if(!product.getTitleFlag()){
                //顯示商品
                holder.tvItemNameId.setVisibility(View.GONE);
                holder.tvTime.setVisibility(View.GONE);
                holder.tvName.setVisibility(View.VISIBLE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvName.setText(product.getName());
                holder.tvStatus.setText(product.getStatus());
            }else{
                // Title
                holder.tvItemNameId.setVisibility(View.VISIBLE);
                holder.tvTime.setVisibility(View.VISIBLE);
                holder.tvName.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.GONE);
                holder.tvItemNameId.setText(order.getCustomerName()+":"+order.getOrder_id());
                holder.tvTime.setText(sdf.format(order.getStartTime()));
            }

            holder.itemView.setOnLongClickListener(v -> {
                PopupMenu popupMenuOrder = new PopupMenu(requireContext(), v, Gravity.END);
                popupMenuOrder.inflate(R.menu.popup_item_menu);
                popupMenuOrder.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    JsonObject j = new JsonObject();
                    switch (itemId) {
                        case R.id.finish_item:
                            j.addProperty("action",Common.UPDATE_ORDER);
                            product.setStatus(Common.STATUS_PRODUCT_FINISH);
                            j.addProperty("data", gsonDate.toJson(product));
                            String backfinish = RemoteAccess.accessProduct(url,j.toString());

                            itemOrder = getSingleOrder(order.getOrder_id());
                            showItemOrder();
                            Toast.makeText(requireContext(), "finish", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.cancel_item:
                            j.addProperty("action",Common.UPDATE_ORDER);
                            product.setStatus(Common.STATUS_CANCEL);
                            j.addProperty("data", gsonDate.toJson(product));
                            String backcancel = RemoteAccess.accessProduct(url,j.toString());

                            itemOrder = getSingleOrder(order.getOrder_id());
                            showItemOrder();
                            Toast.makeText(requireContext(), "cancel", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.preReady_item:
                            j.addProperty("action",Common.UPDATE_ORDER);
                            product.setStatus(Common.STATUS_PREREADY);
                            j.addProperty("data", gsonDate.toJson(product));
                            String backpreReady = RemoteAccess.accessProduct(url,j.toString());

                            itemOrder = getSingleOrder(order.getOrder_id());
                            showItemOrder();
                            Toast.makeText(requireContext(), "preReady", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                });
                popupMenuOrder.show();
                return true;
            });
        }

        class OrderViewHolder extends RecyclerView.ViewHolder{
            private TextView tvItemNameId,tvTime,tvName,tvStatus;

            public OrderViewHolder(@NonNull View itemView) {
                super(itemView);
                tvItemNameId = itemView.findViewById(R.id.tvOrderId_ItemStoreList);
                tvTime = itemView.findViewById(R.id.tvTime_ItemStoreList);
                tvName = itemView.findViewById(R.id.tvName_ItemStoreList);
                tvStatus = itemView.findViewById(R.id.tvStatus_ItemStoreList);
            }
        }

    }
}