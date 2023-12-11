package com.example.yzbkaka.things.tomato;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yzbkaka.things.R;
import com.example.yzbkaka.things.Record.RecordActivity;
import com.example.yzbkaka.things.db.UserRecord;

import java.util.Calendar;
import java.util.TimeZone;


public class TomatoActivity extends AppCompatActivity {
    private Button back;
    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private EditText action;
    private EditText editTextTime;
    private Spinner categories;
    private boolean isTimerRunning = false;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato);

        back = (Button)findViewById(R.id.back);
        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });
    }

    private void startTimer() {
        // 获取用户输入的时间和活动
        action = findViewById(R.id.editAction);
        editTextTime = findViewById(R.id.editTextTime);
        categories = findViewById(R.id.categories);
        String inputTime = editTextTime.getText().toString().trim();
        String inputAction = action.getText().toString().trim();
        String inputCategories = categories.getSelectedItem().toString();

        //get calendar
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1); // 注意月份是从0开始的，所以要加1
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));


        // 检查用户是否输入了时间和活动
        if (!inputTime.isEmpty() && !inputAction.isEmpty() && !inputCategories.isEmpty()) {
            int customTime = Integer.parseInt(inputTime);

            // 实现计时逻辑，使用用户输入的时间
            countDownTimer = new CountDownTimer(customTime * 60 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimerText(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    // 完成工作时钟，可以处理相应逻辑
                    UserRecord userRecord = new UserRecord();

                    //将这些获取到的数据写入db UserRecord
                    userRecord.setCategory(inputCategories);
                    userRecord.setDuration(customTime);
                    userRecord.setActionDescription(inputAction);
                    userRecord.setYear(year);
                    userRecord.setMonth(month);
                    userRecord.setDay(day);

                    userRecord.save();


                }
            }.start();

            startButton.setText("Pause");
            isTimerRunning = true;

            // 将时钟和按钮设置为可见
            timerTextView.setVisibility(View.VISIBLE);
        }else if (inputCategories.isEmpty()) {
            // 用户没有选择，可以显示一个提示
            Toast.makeText(this, "Please select a action.", Toast.LENGTH_SHORT).show();
        } else if (inputAction.isEmpty()) {
            // 用户没有输入时间，可以显示一个提示
            Toast.makeText(this, "Please enter a valid action.", Toast.LENGTH_SHORT).show();
        } else {
            // 用户没有输入活动，可以显示一个提示
            Toast.makeText(this, "Please enter a valid time.", Toast.LENGTH_SHORT).show();
        }
    }


    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        startButton.setText("Start");
        isTimerRunning = false;
    }

    private void updateTimerText(long millisUntilFinished) {
        int minutes = (int) (millisUntilFinished / 1000) / 60;
        int seconds = (int) (millisUntilFinished / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }
}