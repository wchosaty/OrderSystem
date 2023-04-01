package aln.ktversion.ordersystem.store;

import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import aln.ktversion.ordersystem.R;
import aln.ktversion.ordersystem.itemclass.Product;
import aln.ktversion.ordersystem.network.RemoteAccess;
import aln.ktversion.ordersystem.tool.Common;
import aln.ktversion.ordersystem.tool.LogHistory;

public class ProductListFragment extends Fragment {
    private static final String TAG = "TAG ProductListFragment";
    private FloatingActionButton btAdd;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Product> productList;
    private Button btTest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initial();

        handleAddProduct();
        swipeRefreshLayout.setOnRefreshListener(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    showProductList();
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
        view.findViewById(R.id.btTest).setOnClickListener(v -> {
            String url = RemoteAccess.URL+"MyProductServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action","test");
            jsonObject.addProperty("data","test");
            String backString = RemoteAccess.accessProduct(url,jsonObject.toString());
        });
    }


    private void showProductList() {
        if(productList.isEmpty() || productList == null) {
            Toast.makeText(requireContext(), R.string.productisEmpty, Toast.LENGTH_SHORT).show();
            return;
        }
        ProductAdapter productAdapter = (ProductAdapter) recyclerView.getAdapter();
        if(productAdapter == null){
            recyclerView.setAdapter(new ProductAdapter(productList));
        }else {
            productAdapter.setList(productList);
            productAdapter.notifyDataSetChanged();
        }
    }

    private void initial() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        productList = getProductList();
        showProductList();
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

    private void handleAddProduct() {
        btAdd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_productListFragment_to_createProductFragment);
        });
    }

    private void findViews(View view) {
        btAdd = view.findViewById(R.id.btAdd_product);
        recyclerView = view.findViewById(R.id.recyclerView_ProductList);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_ProductList);
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
        private final LayoutInflater layoutInflater;
        private List<Product> list;

        public ProductAdapter(List<Product> list) {
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

        }

        public void setList(List<Product> list) {
            this.list = list;
        }

        class ProductViewHolder extends RecyclerView.ViewHolder{
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