package com.example.yzbkaka.things.Today;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.example.yzbkaka.things.R;
import com.example.yzbkaka.things.db.Plan;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.example.yzbkaka.things.MainActivity.todayCount;


public class TodayCreateActivitty extends AppCompatActivity {
    private EditText editText;  //Plans written down
    private Button finish;  //Finished button
    private String write;  //Inputs
    private Calendar calendar;  //Acquisition time

    private Spinner prioritySpinner;
    Date date;
    Plan plan = new Plan();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_create_activitty);
        editText = (EditText)findViewById(R.id.edit_text);
        editText.setHint("Write down your plans for today!");
        finish = (Button)findViewById(R.id.finish);
        prioritySpinner = (Spinner)findViewById(R.id.priority_spinner);

        // Set up the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);


        calendar = Calendar.getInstance();  //Getting instances
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  //Set to China time

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write = String.valueOf(editText.getText());//Getting the input
                String selectedPriority = prioritySpinner.getSelectedItem().toString();
                if(!write.isEmpty()){
                        plan.setWritePlan(write);
                        plan.setYear(String.valueOf(calendar.get(Calendar.YEAR)));  //Set the time and year of writing
                        plan.setMonth(String.valueOf(calendar.get(Calendar.MONTH)+1));  //Set the month (starts from 0 in the system)
                        plan.setDay(String.valueOf(calendar.get(Calendar.DATE)));  //Setting the date
                        date = new Date(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));  //Setting the date in plan
                        plan.setCreateTime(date);
                        todayCount++;  //Add 1 to the number of today statistics
                        plan.save();
                }
                finish();
            }
        });

        setLightMode();
    }


    private void setLightMode(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
