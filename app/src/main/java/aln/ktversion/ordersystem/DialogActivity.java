package aln.ktversion.ordersystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import aln.ktversion.ordersystem.itemclass.Product;
import aln.ktversion.ordersystem.tool.Common;
import aln.ktversion.ordersystem.tool.LogHistory;

public class DialogActivity extends AppCompatActivity {
    private static final String TAG = "TAG DialogActivity";
    private RecyclerView recyclerView;
    private ImageButton ibMarket;
    private TextView tvTotalPrice;
    List<Map<String,String>> mapList;
    private Integer TotalPrice =0;
    private SharedPreferences pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
//        LogHistory.d(TAG,"onCreate");
        findViews();
        initial();
        handleButton();
    }

    private void handleButton() {
        ibMarket.setOnClickListener(v -> {
            pre = this.getSharedPreferences("chooseProducts", Context.MODE_PRIVATE);
            MarketAdapter mAdapter = (MarketAdapter) recyclerView.getAdapter();
            mapList = mAdapter.getList();
            List<Product> products = new ArrayList<>();
            for(Map<String,String> m : mapList){
                String countString = m.get("count");
                if(!Objects.equals(null,countString) && !Objects.equals("",countString)){
                    int n = Integer.valueOf(countString);
                    for(int i=1;i<=n;i++){
                        Product p =
                        new Product(false,m.get("name"),"",Double.valueOf(m.get("price")),0,0,0,0,"");
                        products.add(p);
                    }
                }
            }
            String dialogString = new Gson().toJson(products);
            LogHistory.d(TAG,dialogString);

            // A startActivity方式
//            pre.edit().putString("diaList",dialogString).apply();
//            startActivity(new Intent(this, CustomerActivity.class));

            // B Activity Result
            // 要將Intent物件放在setResult()內方能回傳
//            Intent intent = getIntent();
            Intent intent = new Intent();
            intent.putExtra("list",dialogString);
            setResult(RESULT_OK,intent);
            finish();

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
//        LogHistory.d(TAG,"onStop ");
    }

    private void initial() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            List<Product> list = new Gson().fromJson(bundle.getString("list"),new TypeToken<List<Product>>() {}.getType());
//            LogHistory.d(TAG,"list size :"+ list.size());
            Map<String,Map<String,String>> map = new HashMap<>();
            mapList = new ArrayList<>();
            for(Product p : list){
                if (map.get(p.getName()) == null) {
                    Map<String,String> m = new HashMap<>();
                    m.put("name",p.getName());
                    m.put("count","1");
                    m.put("price",String.valueOf(p.getPrice()));
                    map.put(p.getName(), m);
                }else{
                    Map<String,String> mm =  map.get(p.getName());
                    String s = mm.get("count");
                    mm.put("count",String.valueOf(Integer.valueOf(s)+1));
                }
            }
            for(String key : map.keySet()){
                Map<String,String> m = map.get(key);
                mapList.add(m);
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(!Objects.equals(mapList,null) && !mapList.isEmpty()){
            showMapList();
        }
        showTotalPrice(tvTotalPrice,mapList);

    }

    private void showTotalPrice(TextView textView, List<Map<String, String>> list) {
        if(list.isEmpty()){
            return;
        }
        double sum = 0;
        for(Map<String,String> map : list){
//            LogHistory.d(TAG,"showTotalPrice listSize :"+list.size() );
            String p = map.get("price");
            String c = map.get("count");
            sum += Double.valueOf(p) * Integer.valueOf(c);
        }
        textView.setText("$"+String.valueOf(sum));
    }

    private void showMapList() {
        MarketAdapter marketAdapter = (MarketAdapter) recyclerView.getAdapter();
        if(marketAdapter == null){
            recyclerView.setAdapter(new MarketAdapter(this,mapList));
        }else{
            marketAdapter.setList(mapList);
            marketAdapter.notifyDataSetChanged();
        }

    }

    private void findViews() {
        recyclerView = findViewById(R.id.recyclerView_Dialog);
        ibMarket = findViewById(R.id.ibMarket_Dialog);
        tvTotalPrice = findViewById(R.id.tvPrice_Dialog);
    }

    private class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketViewHolder> {
        Context context;
        List<Map<String,String>> list;

        public MarketAdapter(Context context, List<Map<String, String>> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<Map<String, String>> mapList) {
            this.list = mapList;
        }
        public List<Map<String, String>> getList(){
            return this.list;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @NonNull
        @Override
        public MarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_market,parent,false);
            return new MarketViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MarketViewHolder holder, int position) {
            Map<String,String> m = list.get(position);
            holder.tvName.setText(m.get("name"));
//            LogHistory.d(TAG,"holder price :"+m.get("price"));
            holder.tvPrice.setText("$"+m.get("price"));
            Integer i =Integer.valueOf(m.get("count"));
            holder.etCount.setText(String.valueOf(i));
            holder.ibSub.setOnClickListener(v -> {
                if(i>=1){
                    m.put("count",String.valueOf(i-1));
                    list.set(position,m);
                    this.notifyItemChanged(position);
                }
                showTotalPrice(tvTotalPrice,this.list);
            });
            holder.ibAdd.setOnClickListener(v -> {
                m.put("count",String.valueOf(i+1));
                list.set(position,m);
                this.notifyItemChanged(position);
                showTotalPrice(tvTotalPrice,this.list);
            });
        }

        class MarketViewHolder extends RecyclerView.ViewHolder{
            TextView tvName,tvPrice;
            EditText etCount;
            ImageButton ibSub,ibAdd;

            public MarketViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName_MarketList);
                tvPrice = itemView.findViewById(R.id.tvPrice_MarketList);
                etCount = itemView.findViewById(R.id.etCount_MarketList);
                ibAdd = itemView.findViewById(R.id.ibAdd_MarketList);
                ibSub = itemView.findViewById(R.id.ibSub_MarketList);
            }
        }
    }
}