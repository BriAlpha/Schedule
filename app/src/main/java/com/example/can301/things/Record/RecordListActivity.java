package com.example.can301.things.Record;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.can301.things.Adapter.RecordAdapter;
import com.example.can301.things.db.UserRecord;
import com.example.can301.things.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class RecordListActivity extends AppCompatActivity {
    private RecyclerView messageContainer;
    private RecordAdapter adapter;
    private List<UserRecord> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        messageContainer = (RecyclerView) findViewById(R.id.messageContainer);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        messageContainer.setLayoutManager(manager);

        Button goBackButton = (Button) findViewById(R.id.buttonGoHome);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(RecordListActivity.this, RecordActivity.class);
                startActivity(goBack);
            }
        });

//        List<UserRecord> dataList = LitePal.findAll(UserRecord.class);
//        RecordAdapter adapter = new RecordAdapter(dataList);
//        messageContainer.setAdapter(adapter);
        Button Analyze = (Button) findViewById(R.id.AnalyzedDataButton);
        Analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Anlyze = new Intent(RecordListActivity.this, AnalyzeActivity.class);
                startActivity(Anlyze);
            }
        });

        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LitePal.deleteAll(UserRecord.class);
                dataList.clear();
                adapter.notifyDataSetChanged();
                messageContainer.setAdapter(null);
            }
        });

    }

    public void onResume(){
        super.onResume();
        List<UserRecord> dataList = LitePal.findAll(UserRecord.class);
        Log.d("RecordActivity", "Data size: " + dataList.size()); // 添加的日志
        adapter = new RecordAdapter(dataList);
        messageContainer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
