package aln.ktversion.ordersystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import aln.ktversion.ordersystem.tool.Common;
import aln.ktversion.ordersystem.tool.LogHistory;

public class CustomerActivity extends AppCompatActivity {
    private static final String TAG = "TAG CustomerActivity";
    public static String customerName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        SharedPreferences preference = this.getSharedPreferences("preference", MODE_PRIVATE);
        customerName = preference.getString("user","");
        SharedPreferences pre = this.getSharedPreferences("chooseProducts", MODE_PRIVATE);
        pre.edit().remove(Common.ALL_PRODUCT).apply();
    }


}