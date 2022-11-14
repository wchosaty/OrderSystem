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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import aln.ktversion.ordersystem.R;
import aln.ktversion.ordersystem.itemclass.Product;
import aln.ktversion.ordersystem.network.RemoteAccess;
import aln.ktversion.ordersystem.tool.Common;
import aln.ktversion.ordersystem.tool.LogHistory;

public class CreateProductFragment extends Fragment {
    private static final String TAG = "TAG CreateProductFragment";
    private Button btCreate;
    private EditText etProductName,etPrice,etWorkTime;
    private TextView tvMessage;
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
            String name = etProductName.getText().toString().trim();
            String priceString = etPrice.getText().toString().trim();
            String waitTimeString = etWorkTime.getText().toString().trim();
            if (checkInput(name) && checkInput(priceString)
                    && checkInput(waitTimeString) && checkInput(selectGroup)) {
                double price = Double.valueOf(priceString);
                Integer waitTime = Integer.valueOf(waitTimeString);
                Product product = new Product(false,name,price,waitTime,0,0,null,selectGroup);

                String data = new Gson().toJson(product);
//                String url = RemoteAccess.URL+"MyProductServlet";
                String url = RemoteAccess.URL+"MyProductServlet_Maintenance";

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", Common.INSERT_PRODUCT);
                jsonObject.addProperty("data",data);

                String backString = RemoteAccess.accessProduct(url,jsonObject.toString());
                LogHistory.d(TAG,"back :"+backString);

            } else {
                tvMessage.setText(R.string.inputIsEmpty);
            }
        });
    }

    private Boolean checkInput(String string) {
        if(!Objects.equals(string,null) && (!Objects.equals(string,""))){
            return true;
        }else{
            return false;
        }
    }

    private void Initial() {
        groupTypeList = new ArrayList<>();
        groupTypeList.add(Common.GROUP_RICE);
        groupTypeList.add(Common.GROUP_NOODLE);
        groupTypeList.add(Common.GROUP_SOUP);
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
        tvMessage = view.findViewById(R.id.tvMessage_create);
    }
}