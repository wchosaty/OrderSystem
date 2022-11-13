package aln.ktversion.ordersystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

public class CustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        initial();
    }

    private void initial() {
//        NavHostFragment navHostFragment =
//                (NavHostFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.fragmentContainerView_store);

    }
}