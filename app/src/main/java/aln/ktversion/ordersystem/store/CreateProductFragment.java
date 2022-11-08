package aln.ktversion.ordersystem.store;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import aln.ktversion.ordersystem.MainActivity;
import aln.ktversion.ordersystem.R;
import aln.ktversion.ordersystem.itemclass.Product;

public class CreateProductFragment extends Fragment {
    private static final String TAG = "TAG CreateProductFragment";
    private Button btCreate;
    private TextView etProductName,etPrice,etWorkTime;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private List<String> groupTypeList;
    private String selectGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        Initial();
        handleData();

    }

    private void handleData() {
        btCreate.setOnClickListener(v -> {
            
        });
    }

    private void Initial() {
        groupTypeList = new ArrayList<>();
        groupTypeList.add(Product.GROUP_RICE);
        groupTypeList.add(Product.GROUP_NOODLE);
        groupTypeList.add(Product.GROUP_SOUP);
        selectGroup = null;
        adapter = new ArrayAdapter<>(getActivity(),R.layout.item_spinner_usertype,groupTypeList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectGroup = ( (TextView) view).getText().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findViews(View view) {
        btCreate = view.findViewById(R.id.btCreate);
        etProductName = view.findViewById(R.id.etProductName);
        etPrice = view.findViewById(R.id.etPrice);
        etWorkTime = view.findViewById(R.id.etWorkTime);

        spinner = view.findViewById(R.id.spinnerGroup);

    }
}