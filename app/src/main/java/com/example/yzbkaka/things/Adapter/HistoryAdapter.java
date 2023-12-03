package com.example.yzbkaka.things.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzbkaka.things.R;
import com.example.yzbkaka.things.db.Plan;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<Plan> mDataList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView history;
        ImageView right;
        TextView time;


        public ViewHolder(View view){
            super(view);
            history = (TextView)view.findViewById(R.id.history_plan);
            right = (ImageView)view.findViewById(R.id.right_kuang);
            time = (TextView)view.findViewById(R.id.time);
         }
    }

    public HistoryAdapter(List<Plan> dataList){
        mDataList = dataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent,false);  //view object
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder , final int position){
        Plan plan = mDataList.get(position);
        holder.time.setText(plan.getMonth() + "month" + plan.getDay() + "day");
        holder.history.setText(plan.getWritePlan());
    }


    @Override
    public int getItemCount(){
        return mDataList.size();
    }
}
