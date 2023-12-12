package com.example.can301.things.Record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.can301.things.db.UserRecord;
import com.example.can301.things.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeActivity extends AppCompatActivity {
    private LineChart lineChart;
    private List<String> xValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        // 获取PieChart控件
        PieChart pieChart = findViewById(R.id.pieChart);

        //获取Activity2传来的Map数据
//        Intent getmessage = getIntent();
//        Map<String, Double> lableTimeMap = (HashMap<String, Double>) getmessage.getSerializableExtra("LabelTimeMap");

        List<UserRecord> userRecords = LitePal.findAll(UserRecord.class);

        Map<String, Float> lableTimeMap = new HashMap<>();
        for (UserRecord userRecord : userRecords) {
            String category = userRecord.getCategory();
            double duration = userRecord.getDuration();

            // 如果该Category已经在Map中，则累加时长，否则直接放入Map
            if (lableTimeMap.containsKey(category)) {
                float currentDuration = lableTimeMap.get(category);
                lableTimeMap.put(category, currentDuration + (float) duration);
            } else {
                lableTimeMap.put(category, (float) duration);
            }
        }

        // 创建饼状图数据
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : lableTimeMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        // 设置饼状图数据集
        PieDataSet dataSet = new PieDataSet(entries, "Label Time");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(16f);

        PieData data = new PieData(dataSet);

        pieChart.setCenterText("Categories");
        pieChart.animate();

        // 设置饼状图描述
        Description description = new Description();
        description.setText("Label Time Analysis");
        pieChart.setDescription(description);

        // 将数据设置给饼状图
        pieChart.setData(data);

        // 刷新饼状图
        pieChart.invalidate();

        //定义goHistory Button 用于返回Acitivity2
        Button goHistory = (Button)findViewById(R.id.goHistory);
        goHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHistory = new Intent(AnalyzeActivity.this, RecordListActivity.class);
                startActivity(goHistory);
            }
        });
        //定义goRecord Button 用于返回Activity1
        Button goRecord = (Button)findViewById(R.id.goRecord);
        goRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goRecord = new Intent(AnalyzeActivity.this, RecordActivity.class);
                startActivity(goRecord);
            }
        });





    }
}