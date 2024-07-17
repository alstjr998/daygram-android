package com.example.daygrampj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Diary> contentList;

    private Context mContext;

    public RecyclerAdapter(Context context, List<Diary> contentList) {
        this.contentList = contentList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_container_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Diary diary = contentList.get(position);
        holder.tvWeekDay.setText(diary.getWeekDay());
        holder.tvDate.setText(diary.getDate());
        holder.tvContent.setText(diary.getContent());

        if(holder.tvWeekDay.getText().equals("SUN")){
            holder.tvDate.setTextColor(Color.RED);
        }

        holder.tvContent.setOnClickListener(v -> {
            Intent editIntent = new Intent(mContext, DaygramEdit.class);
            editIntent.putExtra("recyclerPosition", position);
            mContext.startActivity(editIntent);
        });

        /*String content = contentList.get(position);


        holder.tvContent.setText(content);


        holder.tvContent.setOnClickListener(v -> {
            Log.i("선택된 내용", content);
        });*/
    }

    @Override
    public int getItemCount() {
//        Log.i("날짜", String.valueOf(Calendar.getInstance().getActualMaximum(Calendar.DATE)));
        return contentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWeekDay, tvDate, tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWeekDay = itemView.findViewById(R.id.tvWeekDay);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}
