package aln.ktversion.ordersystem.customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;

import aln.ktversion.ordersystem.CustomerActivity;
import aln.ktversion.ordersystem.DialogActivity;
import aln.ktversion.ordersystem.R;
import aln.ktversion.ordersystem.itemclass.Product;
import aln.ktversion.ordersystem.network.RemoteAccess;
import aln.ktversion.ordersystem.tool.Common;
import aln.ktversion.ordersystem.tool.LogHistory;


public class ChooseProductListFragment extends Fragment {
    private static final String TAG = "TAG ChooseProductListFragment";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btSend;
    private ImageButton ibMarketOpen,ibMarketClose,ibToChooseList;
    private TextView tvTotalPrice;
    private List<Product> productList, chooseProducts;
    private SharedPreferences pre;
    private Boolean savePreFlag = false,drawerOpen = false;
    private DrawerLayout drawerLayout;

    ActivityResultLauncher<Intent> marketLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),this::marketResult);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogHistory.d(TAG,"onViewCreated");
        findViews(view);
        pre = requireContext().getSharedPreferences("chooseProducts", Context.MODE_PRIVATE);

        initial();
        handleButton();

        swipeRefreshLayout.setOnRefreshListener(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    showProductList();
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void handleButton() {
        // 送出訂單
        btSend.setOnClickListener(v -> {
            if(Objects.equals(chooseProducts,null) || chooseProducts.isEmpty() ) {
                return;
            }
            String url = RemoteAccess.URL + "MyProductServlet";
            String data = new Gson().toJson(chooseProducts);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action",Common.BATCH_INSERT_ORDER);
            jsonObject.addProperty("data",data);

            if(!Objects.equals(null, CustomerActivity.customerName) && !Objects.equals("",CustomerActivity.customerName)){
                jsonObject.addProperty("customerName",CustomerActivity.customerName);
            }

            String backString = RemoteAccess.accessProduct(url,jsonObject.toString());
            LogHistory.d(TAG,"back :"+backString);

            if(!Objects.equals(null,backString) && !Objects.equals("",backString)){
                SharedPreferences pre = requireContext().getSharedPreferences("chooseProducts", Context.MODE_PRIVATE);
                pre.edit().remove(Common.ALL_PRODUCT).apply();
                chooseProducts = new ArrayList<>();
                calculateTotalPriceShow(chooseProducts,tvTotalPrice);
            }

        });

        // CustomerOrderList
        ibToChooseList.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_chooseProductListFragment_to_customerOrderListFragment);
        });
          ibMarketOpen.setOnClickListener(v -> {
              if(Objects.equals(null,chooseProducts) || Objects.equals(0,chooseProducts)){
                  return;
              }
              Bundle bundle = new Bundle();
              bundle.putString("list",new Gson().toJson(chooseProducts));
              bundle.putString("total",String.valueOf(tvTotalPrice.getText()));
              Intent intent = new Intent(requireContext(), DialogActivity.class);
              intent.putExtras(bundle);

                // A startActivity方式
//                startActivity(intent);

                // B launcher方式
              marketLauncher.launch(intent);
          });

//        ibMarketOpen.setOnClickListener(v -> {
//            Toast.makeText(requireContext(), "drawer:"+drawerOpen, Toast.LENGTH_SHORT).show();
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
//        });
//        ibMarketClose.setOnClickListener(v -> {
//            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        });
    }

    private void marketResult(ActivityResult activityResult) {

        if(activityResult.getResultCode() == RESULT_OK ){
            String s = activityResult.getData().getExtras().getString("list");
            if(!Objects.equals(null,s ) ) {
                chooseProducts = new Gson().fromJson(s,new TypeToken<List<Product>>() {}.getType());
            }
            calculateTotalPriceShow(chooseProducts,tvTotalPrice);
        }
    }

    private void initial() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        productList = getProductList();
        showProductList();

//        pre = requireContext().getSharedPreferences("chooseProducts", Context.MODE_PRIVATE);

        // A startActivity方式
//        String diaString = pre.getString("diaList",null);
//        if (diaString != null) {
//            LogHistory.d(TAG, "Pre chooseProducts Dialog:" + diaString);
//            Type type = new TypeToken<List<Product>>() {
//            }.getType();
//            chooseProducts = new Gson().fromJson(diaString, type);
//            if (chooseProducts == null) {
//                LogHistory.d(TAG, "Pre chooseProducts Dialog: new");
//                chooseProducts = new ArrayList<>();
//            } else {
//                calculateTotalPriceShow(chooseProducts, tvTotalPrice);
//            }
//            pre.edit().remove(Common.ALL_PRODUCT).remove("diaList").apply();
//        }
//A        else{
            String preString = pre.getString(Common.ALL_PRODUCT,null);
            if(preString != null){
                LogHistory.d(TAG,"Pre chooseProducts :"+preString);
                Type type = new TypeToken<List<Product>>() {}.getType();
                chooseProducts = new Gson().fromJson(preString,type);
                if(chooseProducts == null){
                    LogHistory.d(TAG,"Pre chooseProducts : new");
                    chooseProducts = new ArrayList<>();
                }else{
                    calculateTotalPriceShow(chooseProducts,tvTotalPrice);
                }
            }

//A        }

        addChooseProducts();
    }

    @Override
    public void onStop() {
        super.onStop();
            String gsonString = new Gson().toJson(chooseProducts);
            LogHistory.d(TAG, "onStop String :" + gsonString);
            pre.edit().putString(Common.ALL_PRODUCT, gsonString).apply();
            chooseProducts = null;
    }

    private void addChooseProducts() {
        Bundle bundle = getArguments();
        setArguments(null);
        if(bundle == null){
            LogHistory.d(TAG,"addChooseProducts bundle: null");
            return;
        }
        Integer count = bundle.getInt("count");
        LogHistory.d(TAG,"count:"+count);
        String productString = bundle.getString(Common.INSERT_PRODUCT);
        Product product = new Gson().fromJson(productString,Product.class);
        if(Objects.equals(chooseProducts,null)){
            LogHistory.d(TAG,"chooseProducts : null");
//            chooseProducts = new ArrayList<>();
        }
        if(!Objects.equals(null,count) && count > 0 && !Objects.equals(null,product)){
            LogHistory.d(TAG,"chooseProducts : add");
            for(int i=1;i<=count;i++){
                chooseProducts.add(product);
            }

            calculateTotalPriceShow(chooseProducts,tvTotalPrice);
        }
    }

    private void calculateTotalPriceShow(List<Product> chooseProducts, TextView textView) {
        double totalPay = 0;
        for(Product p : chooseProducts){
            totalPay += p.getPrice();
        }
        textView.setText(getString(R.string.priceSymbol)+ totalPay );
    }

    private void showProductList() {
        if(Objects.equals(productList,null) || productList.isEmpty() ) {
            Toast.makeText(requireContext(), R.string.productisEmpty, Toast.LENGTH_SHORT).show();
            return;
        }
        ProductAdapter productAdapter = (ProductAdapter) recyclerView.getAdapter();
        if(productAdapter == null){
            recyclerView.setAdapter(new ProductAdapter(productList));
        }else{
            productAdapter.setList(productList);
            productAdapter.notifyDataSetChanged();
        }
    }

    private List<Product> getProductList() {
        JsonObject jsonObject = new JsonObject();
        String url = RemoteAccess.URL+"MyProductServlet";
        jsonObject.addProperty("action", Common.ALL_PRODUCT);
        jsonObject.addProperty("data",Common.ALL_PRODUCT);

        String backString = RemoteAccess.accessProduct(url,jsonObject.toString());
//        LogHistory.d(TAG,"back :"+backString);
        Type type = new TypeToken<List<Product>>() {}.getType();

        return new Gson().fromJson(backString,type);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_ChooseProduct);
        ibMarketOpen = view.findViewById(R.id.ibMarket_chooseProduct);
        ibMarketClose = view.findViewById(R.id.ibMarket_Market);
        ibToChooseList = view.findViewById(R.id.ibToCHoseList_ChooseProduct);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice_ChooseProduct);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_ChooseProduct);
        btSend = view.findViewById(R.id.btSend_SingleProduct);

//        drawerLayout = view.findViewById(R.id.drawerLayout);
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
        private final LayoutInflater layoutInflater;
        private List<Product> list;

        private ProductAdapter(List<Product> list) {
            layoutInflater = LayoutInflater.from(requireContext());
            this.list = list;
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_productlist,parent,false);
            return new ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            final Product product = list.get(position);
            if(product.getTitleFlag()){
                holder.tvKind.setVisibility(View.VISIBLE);
                holder.tvKind.setText(product.getKind());
                holder.tvName.setVisibility(View.GONE);
                holder.tvPrice.setVisibility(View.GONE);
                holder.tvWaitTime.setVisibility(View.GONE);
            }else{
                holder.tvKind.setVisibility(View.GONE);
                holder.tvName.setVisibility(View.VISIBLE);
                holder.tvPrice.setVisibility(View.VISIBLE);
                holder.tvWaitTime.setVisibility(View.VISIBLE);
                holder.tvName.setText(product.getName());
                holder.tvPrice.setText(getString(R.string.priceSymbol)+String.valueOf(product.getPrice()));
                holder.tvWaitTime.setText("約"+String.valueOf(product.getWaitTime())+ getString(R.string.minute) );
            }

            // ToChooseProduct
            holder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Common.INSERT_PRODUCT,product);
                Navigation.findNavController(v)
                .navigate(R.id.action_chooseProductListFragment_to_singleMarketProductFragment,bundle);
            });
        }

        public void setList(List<Product> list) {
            this.list = list;
        }

        class ProductViewHolder extends RecyclerView.ViewHolder {
            private TextView tvKind,tvName,tvPrice,tvWaitTime;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                tvKind = itemView.findViewById(R.id.tvKind_ProductList);
                tvName = itemView.findViewById(R.id.tvName_productList);
                tvPrice =itemView.findViewById(R.id.tvPrice_ProductList);
                tvWaitTime = itemView.findViewById(R.id.tvWaitTime_ProductList);
            }
        }
    }
}