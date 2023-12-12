package com.example.yzbkaka.things.Setting;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yzbkaka.things.R;

import java.util.Timer;
import java.util.TimerTask;

public class ExplainActivity_Log extends AppCompatActivity {
    private Button back;
    private LinearLayout explain3;
    private ImageView img3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain_log);
        back = (Button)findViewById(R.id.back);
        explain3 = (LinearLayout)findViewById(R.id.explain_three);
        img3 = (ImageView)findViewById(R.id.explain_button_three);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img3.setImageResource(R.drawable.yes);
                Animation animation = AnimationUtils.loadAnimation(ExplainActivity_Log.this,R.anim.today_anim);  //设置动画
                explain3.startAnimation(animation);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                explain3.setVisibility(View.GONE);
                            }
                        });
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task,800);

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
