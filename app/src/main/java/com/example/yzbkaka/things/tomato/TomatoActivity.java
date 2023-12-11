package com.example.yzbkaka.things.tomato;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yzbkaka.things.R;

public class TomatoActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato);

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);

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
        // 获取用户输入的时间
        EditText editTextTime = findViewById(R.id.editTextTime);
        int customTime = Integer.parseInt(editTextTime.getText().toString());

        // 实现计时逻辑，使用用户输入的时间
        countDownTimer = new CountDownTimer(customTime * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                // 完成工作时钟，可以处理相应逻辑
            }
        }.start();

        startButton.setText("Pause");
        isTimerRunning = true;
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