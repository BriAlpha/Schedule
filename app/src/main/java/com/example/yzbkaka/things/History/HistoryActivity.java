package com.example.yzbkaka.things.History;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yzbkaka.things.Adapter.HistoryAdapter;
import com.example.yzbkaka.things.R;
import com.example.yzbkaka.things.db.Plan;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private Button back;
    private RecyclerView historyView;
    private Button clear;
    private List<Plan> historyList = new ArrayList<>();
    private List<Plan> dataList = new ArrayList<>();
    private HistoryAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        back = (Button)findViewById(R.id.back);
        historyView = (RecyclerView)findViewById(R.id.history_view);
        clear = (Button)findViewById(R.id.clear);
        dataList = LitePal.findAll(Plan.class);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        historyView.setLayoutManager(manager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {  //clear the list once clicked
            @Override
            public void onClick(View view) {
                Toast.makeText(HistoryActivity.this, "now empty", Toast.LENGTH_SHORT).show();
                historyList.clear();
                myAdapter.notifyDataSetChanged();  //refresh the adapter each time the list is updated
                LitePal.deleteAll(Plan.class,"status = ?","1");  //delete all the plans whose status is true from the database
            }
        });

        if(dataList.size() > 0){
            historyList.clear();
            for(Plan plan : dataList){
                if(plan.getStatus() == true){  //if the status is true(complete), then add to the historyList
                    historyList.add(plan);
                }
            }
        }
        myAdapter = new HistoryAdapter(historyList);
        historyView.setAdapter(myAdapter);
        setLightMode();
    }


    private void setLightMode(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
