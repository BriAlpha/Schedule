package com.example.can301.things.Adapter;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.can301.things.R;
import com.example.can301.things.Schedule.AlterScheduleActivity;
import com.example.can301.things.db.Plan;

import org.litepal.LitePal;

import java.util.List;

import static com.example.can301.things.Schedule.ScheduleViewActivity.scheduleAdapter;

import androidx.recyclerview.widget.RecyclerView;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<Plan> mDataList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View scheduleView;
        TextView schedule;
        ImageView kuang;
        TextView time;
        ImageView alt_finish;
        ImageView delete_schedule;

        public ViewHolder(View view){
            super(view);
            scheduleView = view;
            schedule = (TextView)view.findViewById(R.id.schedule_plan);
            kuang = (ImageView)view.findViewById(R.id.kuang);
            delete_schedule = (ImageView)view.findViewById(R.id.delete_schedule);
            alt_finish = (ImageView)view.findViewById(R.id.alt_finish);
            time = (TextView)view.findViewById(R.id.schedule_time);
        }
    }


    public ScheduleAdapter(List<Plan> dataList){
        mDataList = dataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item,parent,false);  //加载子项布局
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder , final int position){
        Plan plan = mDataList.get(position);
        holder.time.setText(plan.getMonth() + "Month" + plan.getDay() + "Day");
        holder.schedule.setText(plan.getWritePlan());

        if(mDataList.get(position).getStatus() == false){
            holder.kuang.setImageResource(R.drawable.no);
        }else {
            holder.kuang.setImageResource(R.drawable.yes);
        }

        holder.scheduleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int writePosition = holder.getAdapterPosition();
                Plan viewPlan = mDataList.get(writePosition);
                String writeToday = viewPlan.getWritePlan();
                Intent intent = new Intent(view.getContext(),AlterScheduleActivity.class);
                intent.putExtra("write",writeToday);
                view.getContext().startActivity(intent);
            }
        });

        holder.delete_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int scheduleDeletePosition = holder.getAdapterPosition();
                Plan scheduleDeletePlan = mDataList.get(scheduleDeletePosition);
                String scheduleDeletePlanWrite = scheduleDeletePlan.getWritePlan();
                mDataList.remove(scheduleDeletePosition);
                scheduleAdapter.notifyDataSetChanged();
                LitePal.deleteAll(Plan.class,"writePlan = ?",scheduleDeletePlanWrite);
            }
        });

        holder.alt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int scheduleDeletePosition = holder.getAdapterPosition();
                Plan scheduleDeletePlan = mDataList.get(scheduleDeletePosition);
                String scheduleDeletePlanWrite = scheduleDeletePlan.getWritePlan();
                mDataList.get(scheduleDeletePosition).setStatus(true);
                scheduleAdapter.notifyDataSetChanged();
                Plan plan = new Plan();
                plan.setStatus(true);
                plan.updateAll("writePlan = ?",scheduleDeletePlanWrite);
            }
        });

    }


    @Override
    public int getItemCount(){
        return mDataList.size();
    }
}
