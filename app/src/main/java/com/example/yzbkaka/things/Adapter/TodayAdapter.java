package com.example.yzbkaka.things.Adapter;

import android.content.Intent;
import android.os.Message;
import android.provider.AlarmClock;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzbkaka.things.DoubleClickListener;
import com.example.yzbkaka.things.R;
import com.example.yzbkaka.things.Today.AlterTodayActivity;
import com.example.yzbkaka.things.db.Plan;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

import org.litepal.LitePal;

import static com.example.yzbkaka.things.MainActivity.todayCount;
import static com.example.yzbkaka.things.Today.NoteActivity.todayAdapter;


/**
 * Created by yzbkaka on 19-4-4.
 */

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {
    private List<Plan> mDataList;
    private List<Plan> mPlanList; // Declaration of the list
    private static int imagePosition;  //Set the position of the clicked checkbox in the front as a static variable

    static class ViewHolder extends RecyclerView.ViewHolder{
        View todayView;
        ImageView imageView;
        TextView priorityView;
        Button delete;

        public ViewHolder(View view){
            super(view);
            todayView = view;
            imageView = (ImageView)view.findViewById(R.id.no_finish);
//          todayText = (TextView)view.findViewById(R.id.today_plan);
            priorityView = view.findViewById(R.id.priority_text_view); // Initialize the new TextView
            delete = (Button)view.findViewById(R.id.delete);
        }
    }



    public TodayAdapter(List<Plan> planList) {
        if (planList != null) {
            mPlanList = planList;
        } else {
            mPlanList = new ArrayList<>(); // Initialize with an empty list instead of null
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_item,parent,false);  //Load sub-item layout
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Plan plan = mPlanList.get(position);
        String priority = plan.getPriority();
        holder.priorityView.setText(priority);

        final Handler handler = new Handler() {  //Asynchronous message handling mechanism to modify UI in non-main thread

            public void handleMessage(Message msg){
                switch (msg.what){
                    case 1:
                        mDataList.remove(imagePosition);  //Remove the Plan from the list (there is no removal in the database at this time)
                        todayAdapter.notifyDataSetChanged();  //Update the adapter to update the list
                        holder.imageView.setImageResource(R.drawable.no);  //Change the front box back to gray
                        break;

                    default:
                        break;
                }
            }
        };

        holder.todayView.setOnClickListener(new View.OnClickListener() {  //When you click the text, you enter the Modify activity
            @Override
            public void onClick(View view) {
                int writePosition = holder.getAdapterPosition();
                Plan viewPlan = mDataList.get(writePosition);
                String writeToday = viewPlan.getWritePlan();
                Intent intent = new Intent(view.getContext(),AlterTodayActivity.class);
                intent.putExtra("write",writeToday);
                view.getContext().startActivity(intent);
            }
        });


        holder.imageView.setOnClickListener(new View.OnClickListener() {  //When you click on the previous checkbox, the picture of the checkbox changes
            @Override
            public void onClick(View view) {
                imagePosition = holder.getAdapterPosition();
                Plan ImagePlan = mDataList.get(imagePosition);
                holder.imageView.setImageResource(R.drawable.yes);  //Switch picture
                ImagePlan.setStatus(true);  //The status is set to Done
                ImagePlan.save();  //Save the state
                todayCount--;  //After removal, the quantity is reduced by 1

                TimerTask task1 = new TimerTask() {  //Setting a scheduled Task
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);  //Send asynchronous Message
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task1, 800);  //Set the delay time in milliseconds

                Animation animation = AnimationUtils.loadAnimation(view.getContext(),R.anim.today_anim);  //Set animation
                holder.todayView.startAnimation(animation);
            }
        });


        holder.todayView.setOnLongClickListener(new View.OnLongClickListener() {  //Long press mechanism
            @Override
            public boolean onLongClick(View view) {  //Long press effect
                holder.delete.setVisibility(View.VISIBLE);  //The delete button is displayed


                return true;
            }
        });

        holder.todayView.setOnClickListener(new DoubleClickListener() {  //Double click event
            @Override
            public void onDoubleClick(View view) {
                Intent alarmsIntent = new Intent(AlarmClock.ACTION_SET_ALARM);  //Call system alarm
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deletePosition = holder.getAdapterPosition();  //Get the click order
                Plan deletePlan = mDataList.get(deletePosition);
                String deletePlanWrite = deletePlan.getWritePlan();
                mDataList.remove(deletePosition);
                todayCount--;  //Decrease in number
                todayAdapter.notifyDataSetChanged();  //Update adapter
                LitePal.deleteAll(Plan.class,"writePlan = ?",deletePlanWrite);  //Delete this article today


                TimerTask deleteTask = new TimerTask() {
                    @Override
                    public void run() {
                        holder.delete.setVisibility(View.INVISIBLE);
                    }
                };
                Timer deleteTimer = new Timer();
                deleteTimer.schedule(deleteTask,0);  //The delete button is automatically hidden after one second
            }
        });
    }



    @Override
    public int getItemCount() {
        return (mPlanList != null) ? mPlanList.size() : 0;
    }
}
