package aln.ktversion.ordersystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;

import aln.ktversion.ordersystem.tool.LogHistory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG MainActivity";
    private static final String CUSTOMER = "顧客",STORE = "商店";
    private TextView tvName,tvPassword,tvMessage;
    private EditText etName,etPassword;
    private Spinner spinner;
    private List<String> userTypeList;
    private ArrayAdapter<String> adapter;
    private String selectType;
    private Button btLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("preference",MODE_PRIVATE);
        findViews();
        initial();
        loginHandle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String user = sharedPreferences.getString("user","");
        String password = sharedPreferences.getString("password","");
        String type = sharedPreferences.getString("type","");
        boolean  status = sharedPreferences.getBoolean("status",false);
        LogHistory.d(TAG,"user :"+user+" password :"+password+" type :"+type+" status :"+status);
        if(checkString(user) && checkString(password) && status){
            LogHistory.d(TAG,"check Success");
            jumpActivity(this,type);
        }
    }

    private void jumpActivity(Context context, String type) {
        if(Objects.equals(type,STORE)){
            startActivity(new Intent(context,StoreActivity.class));
        }
        if(Objects.equals(type,CUSTOMER)){
            startActivity(new Intent(context,CustomerActivity.class));
        }
        this.finish();
    }

    private void loginHandle() {
        btLogin.setOnClickListener(v -> {
            String user = etName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if(checkString(user) && checkString(password)){
                sharedPreferences.edit().putString("user",user).putString("password",password)
                        .putString("type",selectType).putBoolean("status",true).apply();
                tvMessage.setText(R.string.loginsuccess);
                jumpActivity(this,selectType);
            }else{
                tvMessage.setText(R.string.keyempty);
            }
        });
    }

    private boolean checkString(String temp) {
        if(!Objects.equals(null,temp) && !Objects.equals("",temp)){
            return true;
        }else{
            return false;
        }
    }

    private void initial() {
        userTypeList = new ArrayList<>();
        userTypeList.add(STORE);
        userTypeList.add(CUSTOMER);
        selectType = null;

        adapter = new ArrayAdapter<>(this,R.layout.item_spinner_usertype,userTypeList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectType = ( (TextView)view ).getText().toString();
                LogHistory.d(TAG,"spinner type :"+selectType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void findViews() {
        spinner = findViewById(R.id.spinner);
        tvName  = findViewById(R.id.tvName);
        tvPassword = findViewById(R.id.tvPassword);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);

        tvMessage = findViewById(R.id.tvMessage);
    }
}