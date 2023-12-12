package com.example.can301.things.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.can301.things.db.UserRecord;
import com.example.can301.things.R;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{
    //导入需要处理的数据
    private List<UserRecord> userRecords;
    //创建构造方法
    public RecordAdapter(List<UserRecord> userRecords) {
        this.userRecords = userRecords;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 使用 LayoutInflater 从布局文件中创建视图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_record, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserRecord userRecord = userRecords.get(position);
        // 将用户记录的属性设置到对应的 TextView 中
        holder.textViewAction.setText(userRecord.getActionDescription());
        holder.textViewDuration.setText(String.valueOf(userRecord.getDuration()));
        holder.textViewCategory.setText(userRecord.getCategory());
        holder.textViewTime.setText(userRecord.getYear()+"Year"+userRecord.getMonth() + "Month"+ userRecord.getDay()+"Day" );
//        holder.textViewTime.setText(userRecord.getCreateTime());
    }
    @Override
    public int getItemCount() {
        return userRecords.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAction;
        TextView textViewDuration;
        TextView textViewCategory;
        TextView textViewTime;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewAction = itemView.findViewById(R.id.textViewAction);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewTime = itemView.findViewById(R.id.time);
        }
    }



}
