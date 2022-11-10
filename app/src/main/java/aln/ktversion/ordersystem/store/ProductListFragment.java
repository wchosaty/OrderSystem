package aln.ktversion.ordersystem.store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import aln.ktversion.ordersystem.R;

public class ProductListFragment extends Fragment {
    private static final String TAG = "TAG ProductListFragment";
    private FloatingActionButton btAdd;

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

        handleAddProduct();
    }

    private void handleAddProduct() {
        btAdd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_productListFragment_to_createProductFragment);
        });
    }

    private void findViews(View view) {
        btAdd = view.findViewById(R.id.btAdd_product);
    }
}