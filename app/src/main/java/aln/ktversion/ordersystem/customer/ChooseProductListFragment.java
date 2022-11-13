package aln.ktversion.ordersystem.customer;

import android.icu.text.PluralRules;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import aln.ktversion.ordersystem.R;
import aln.ktversion.ordersystem.itemclass.Order;
import aln.ktversion.ordersystem.itemclass.Product;
import aln.ktversion.ordersystem.network.RemoteAccess;
import aln.ktversion.ordersystem.store.ProductListFragment;
import aln.ktversion.ordersystem.tool.Common;
import aln.ktversion.ordersystem.tool.LogHistory;

public class ChooseProductListFragment extends Fragment {
    private static final String TAG = "TAG ChooseProductListFragment";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton ibMarket;
    private TextView tvTotalPrice;
    private List<Product> productList;
    private Order order;

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
        findViews(view);
        initial();

        swipeRefreshLayout.setOnRefreshListener(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    showProductList();
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
    }

    private void initial() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        productList = getProductList();
        showProductList();
        addChooseProducts();
    }

    private void addChooseProducts() {
        Bundle bundle = getArguments();
        if(bundle == null){
            return;
        }
        String backString = bundle.getString(Common.ALL_PRODUCT);
        Type type = new TypeToken<List<Product>>() {}.getType();
        List<Product> list = new Gson().fromJson(backString,type);
        if(list != null){
//            tvTotalPrice.setText("size :"+list.size()+" name :"+list.get(0).getName()+" price :"+ list.get(0).getPrice());
        }
    }

    private void showProductList() {
        if(productList.isEmpty() || productList == null) {
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
        LogHistory.d(TAG,"back :"+backString);
        Type type = new TypeToken<List<Product>>() {}.getType();

        return new Gson().fromJson(backString,type);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_ChooseProduct);
        ibMarket = view.findViewById(R.id.ibMarket_chooseProduct);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice_ChooseProduct);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_ChooseProduct);
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