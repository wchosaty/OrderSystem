package aln.ktversion.ordersystem.customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aln.ktversion.ordersystem.R;
import aln.ktversion.ordersystem.itemclass.Product;
import aln.ktversion.ordersystem.tool.Common;
import aln.ktversion.ordersystem.tool.LogHistory;

public class SingleMarketProductFragment extends Fragment {
    private static final String TAG = "TAG SingleMarketProductFragment";
    private ImageButton ibSub,ibAdd,ibMarket;
    private Button btSure;
    private TextView tvName,tvPrice,tvWaitTime,tvTotalPrice;
    private EditText etCount;
    private List<Product> chooseProducts;
    private Product product;
    private double total;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_market_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        product = initial();
        handleButton();
        }

    private void handleButton() {
        ibSub.setOnClickListener(v -> {
            if(chooseProducts == null || product == null ){
                return;
            }
            if(chooseProducts.size() >= 1){
                chooseProducts.remove(0);
                etCount.setText(String.valueOf(chooseProducts.size()));
                total = product.getPrice() * chooseProducts.size();
                tvTotalPrice.setText(String.valueOf(total));
            }
        });
        ibAdd.setOnClickListener(v -> {
            if(chooseProducts == null || product == null ){
                return;
            }
            if(product != null){
                chooseProducts.add(product);
                etCount.setText(String.valueOf(chooseProducts.size()));
                total = product.getPrice() * chooseProducts.size();
                tvTotalPrice.setText(String.valueOf(total));
            }
        });
        btSure.setOnClickListener(v -> {
            if(chooseProducts!=null && chooseProducts.size()>=1 && product != null){
                Bundle bundle = new Bundle();
                String gsonString = new Gson().toJson(chooseProducts);
                LogHistory.d(TAG,"single String :"+gsonString);
                bundle.putString(Common.ALL_PRODUCT, gsonString );
                Navigation.findNavController(v)
                        .navigate(R.id.action_singleMarketProductFragment_to_chooseProductListFragment,bundle);
            }
        });
    }

    private Product initial() {
        Bundle bundle = getArguments();
        Product p = (Product) bundle.getSerializable(Common.INSERT_PRODUCT);
        LogHistory.d(TAG,"name :"+p.getName()+" price :"+p.getPrice()+" waitTime :"+p.getWaitTime());
        chooseProducts = new ArrayList<>();
        tvName.setText(p.getName());
        tvPrice.setText(getString(R.string.priceSymbol)+String.valueOf(p.getPrice()));
        tvWaitTime.setText("約"+String.valueOf(p.getWaitTime())+ getString(R.string.minute) );

        chooseProducts.add(p);
        total = p.getPrice() * chooseProducts.size();
        tvTotalPrice.setText(String.valueOf(total));
        return p;
    }

    private void findViews(View view) {
        tvName = view.findViewById(R.id.tvName_SingleMarketProduct);
        etCount = view.findViewById(R.id.etCount_SingleProduct);
        ibSub = view.findViewById(R.id.ibSub_SingleProduct);
        ibAdd = view.findViewById(R.id.ibAdd_SingleProduct);
        tvPrice = view.findViewById(R.id.tvPrice_SingleProduct);
        tvWaitTime = view.findViewById(R.id.tvWaitTime_SingleProduct);
        ibMarket = view.findViewById(R.id.ibMarket_SingleProduct);
        tvTotalPrice = view.findViewById(R.id.tvTotal_SingleProduct);
        btSure = view.findViewById(R.id.btSure_SingleProduct);
    }
}