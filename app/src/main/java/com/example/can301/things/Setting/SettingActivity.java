package com.example.can301.things.Setting;


import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.can301.things.ActivityCollector;
import com.example.can301.things.R;
import com.example.can301.things.Service.SendMessage;

public class SettingActivity extends AppCompatActivity {
    private Button checkSendMessage;
    private Button back;
    private LinearLayout explain_schedule;
    private LinearLayout explain_log;
    private LinearLayout explain_tomato;
//    private LinearLayout about;
//    private LinearLayout exit;
    public static int count = 0;
    private Button dark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActivityCollector.addActivity(this);
        checkSendMessage = (Button) findViewById(R.id.check_send);
        back = (Button)findViewById(R.id.back);
        explain_schedule = (LinearLayout)findViewById(R.id.how_to_use_today);
        explain_tomato = (LinearLayout)findViewById(R.id.how_to_use_tomato);
        explain_log = (LinearLayout)findViewById(R.id.how_to_use_log);
//        about = (LinearLayout)findViewById(R.id.about);
//        exit = (LinearLayout)findViewById(R.id.exit);

        if(count%2 == 0){
            checkSendMessage.setBackgroundResource(R.drawable.checkno);
        }
        else{
            checkSendMessage.setBackgroundResource(R.drawable.checkyes);
        }

        checkSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count%2 == 0){  //同意开启发送通知
                    count++;
                    checkSendMessage.setBackgroundResource(R.drawable.checkyes);
                    Intent intent = new Intent(SettingActivity.this, SendMessage.class);
                    startService(intent);  //启动服务
                }
                else{  //拒绝发送通知
                    count++;
                    checkSendMessage.setBackgroundResource(R.drawable.checkno);
                }
            }
        });

        explain_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explainIntent = new Intent(SettingActivity.this, ExplainActivity_Today.class);
                startActivity(explainIntent);
            }
        });

        explain_tomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explainTomatoIntent = new Intent(SettingActivity.this,ExplainActivity_Tomato.class);
                startActivity(explainTomatoIntent);
            }
        });

        explain_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explainLogIntent = new Intent(SettingActivity.this, ExplainActivity_Log.class);
                startActivity(explainLogIntent);
            }
        });


//        about.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent aboutIntent = new Intent(SettingActivity.this,AboutActivity.class);
//                startActivity(aboutIntent);
//            }
//        });
//
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ActivityCollector.finishAll();
//            }
//        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setLightMode();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    private void setLightMode(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
