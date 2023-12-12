package com.example.can301.things.Schedule;

import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.can301.things.R;
import com.example.can301.things.db.Plan;

import org.litepal.LitePal;

public class AlterScheduleActivity extends AppCompatActivity {
    private EditText editText;
    private Button finish;
    Plan plan = new Plan();
    String oldWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_schedule);

        editText = (EditText) findViewById(R.id.old_text);
        finish = (Button)findViewById(R.id.finish);
        Intent intent = getIntent();
        oldWrite = intent.getStringExtra("write");  //get the previous data
        editText.setText(oldWrite);  //Displays the previous data on the EditText

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newWrite = editText.getText().toString();  //Get the new data after the user changes
                if(newWrite.isEmpty()){
                    LitePal.deleteAll(Plan.class,"writePlan = ?",oldWrite);  //If the value is null after modification, it is automatically deleted
                }
                else{
                    plan.setWritePlan(newWrite);  //LitePal syntax, first set the new content, and then look for conditional updates
                    plan.updateAll("writePlan = ?",oldWrite);
                    Toast.makeText(AlterScheduleActivity.this, "Changed successfully!", Toast.LENGTH_SHORT).show();
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
