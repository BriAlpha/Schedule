package com.example.can301.things.Tomato;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.can301.things.R;
import com.example.can301.things.Record.RecordListActivity;
import com.example.can301.things.db.UserRecord;

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
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;


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
                onBackPressed();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    stopConfirmationDialog();
                } else {
                    startTimer();
                }
            }
        });
    }

    private void startTimer() {
        // 如果计时器已经在运行，不再创建新的计时器
        if (isTimerRunning && countDownTimer != null) {
            return;
        }

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

                    stopTimer();
                    //FinishMessage
                    showCompletionMessage();
                }
            }.start();

            startButton.setText("Stop");
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

    private void showCompletionMessage() {
        // 弹出 Toast 消息
        Toast.makeText(TomatoActivity.this, "Task completed! Your record was loading!", Toast.LENGTH_LONG).show();

        // 播放铃声
        playRingtone();

        // 执行震动
        vibrate();

        // 创建按钮
        Button toRecordActivityButton = new Button(this);
        toRecordActivityButton.setText("View all your work");
        // 设置按钮的背景颜色为橙色
        toRecordActivityButton.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));

        // 设置按钮的点击监听器
        toRecordActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 停止播放铃声
                stopRingtone();
                // 停止震动
                stopVibration();
                // 在按钮点击时执行跳转逻辑
                Intent intent = new Intent(TomatoActivity.this, RecordListActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.startButton); // 改成你想要放置按钮的位置

        ((RelativeLayout) findViewById(R.id.RelativeLayout)).addView(toRecordActivityButton, layoutParams);
    }

    private void playRingtone() {
        // 初始化 MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.ring); // 替换为你的铃声资源

        // 设置循环播放
        mediaPlayer.setLooping(false);

        // 开始播放
        mediaPlayer.start();
    }
    private void stopRingtone() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    private void vibrate() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // 震动 500 毫秒（可以根据需要调整）
        if (vibrator != null) {
            vibrator.vibrate(500);
        }
    }
    private void stopVibration() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    private void updateTimerText(long millisUntilFinished) {
        int minutes = (int) (millisUntilFinished / 1000) / 60;
        int seconds = (int) (millisUntilFinished / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    //退出提示
    @Override
    public void onBackPressed() {
        if (isTimerRunning) {
            showExitConfirmationDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_Dialog);
        builder.setTitle("Confirmation")
                .setMessage("Are you want to cancel the timer? Finish your job will record it in your list.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户确认退出，停止计时器并关闭应用
                        stopTimer();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户取消退出，继续计时器
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void stopConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_Dialog);
        builder.setTitle("Confirmation")
                .setMessage("Are you want to stop the timer? Finish your job will record it in your list.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户确认退出，停止计时器并关闭应用
                        stopTimer();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户取消退出，继续计时器
                        dialog.dismiss();
                    }
                })
                .show();
    }
}