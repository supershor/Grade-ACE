package com.om_tat_sat.grade_ace.Recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.om_tat_sat.grade_ace.R;
import com.om_tat_sat.grade_ace.data_holders.topper_tips_holder;

import java.util.ArrayList;
import java.util.Collection;

public class Recycler_topper_tips extends RecyclerView.Adapter<Recycler_topper_tips.ViewHolder> {
    ArrayList<topper_tips_holder>arr;
    Context content;

    public Recycler_topper_tips(ArrayList<topper_tips_holder> arr, Context content) {
        this.arr = arr;
        this.content = content;
    }

    @NonNull
    @Override
    public Recycler_topper_tips.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(content).inflate(R.layout.topper_tips_recycler_text_holder,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_topper_tips.ViewHolder holder, int position) {
        holder.name.append(arr.get(position).name);
        holder.passing_year.append(arr.get(position).passing_year);
        holder.college.append(arr.get(position).college_name);
        holder.message.append(arr.get(position).message);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView passing_year;
        TextView college;
        TextView message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.topper_tips_name);
            passing_year=itemView.findViewById(R.id.topper_tips_passing_year);
            college=itemView.findViewById(R.id.topper_tips_college);
            message=itemView.findViewById(R.id.topper_tips_message);
        }
    }
}
