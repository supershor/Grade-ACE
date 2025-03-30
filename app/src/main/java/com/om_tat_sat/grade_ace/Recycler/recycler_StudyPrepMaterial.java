package com.om_tat_sat.grade_ace.Recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.om_tat_sat.grade_ace.Interface.RecyclerInterface;
import com.om_tat_sat.grade_ace.R;
import com.om_tat_sat.grade_ace.data_holders.Study_Prep_Material_Holder;

import java.util.ArrayList;

public class recycler_StudyPrepMaterial extends RecyclerView.Adapter<recycler_StudyPrepMaterial.ViewHolder>{
    Context context;
    ArrayList<Study_Prep_Material_Holder> arr;
    private final RecyclerInterface recyclerViewInterface;
    public recycler_StudyPrepMaterial(RecyclerInterface recyclerViewInterface, Context context, ArrayList<Study_Prep_Material_Holder> arr) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.arr = arr;
    }
    @NonNull
    @Override
    public recycler_StudyPrepMaterial.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.study_prep_name_shower_new_ui,parent,false);
        return new recycler_StudyPrepMaterial.ViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull recycler_StudyPrepMaterial.ViewHolder holder, int position) {
        holder.name.setText(arr.get(position).getName());
        if(arr.get(position).getMessage1()!=null && !arr.get(position).getMessage1().isEmpty()){
            holder.message1.setText(arr.get(position).getMessage1Name());
            holder.message1.append(arr.get(position).getMessage1());
            holder.message1.setVisibility(View.VISIBLE);
        }
        if(arr.get(position).getMessage2()!=null && !arr.get(position).getMessage2().isEmpty()){
            holder.message2.setText(arr.get(position).getMessage2Name());
            holder.message2.append(arr.get(position).getMessage2());
            holder.message2.setVisibility(View.VISIBLE);
        }
        if(arr.get(position).getMessage3()!=null && !arr.get(position).getMessage3().isEmpty()){
            holder.message3.setText(arr.get(position).getMessage3Name());
            holder.message3.append(arr.get(position).getMessage3());
            holder.message3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message1;
        TextView message2;
        TextView message3;
        public ViewHolder(@NonNull View itemView,RecyclerInterface recyclerViewInterface) {
            super(itemView);
            name=itemView.findViewById(R.id.tv_name);
            message1=itemView.findViewById(R.id.tv_message1);
            message2=itemView.findViewById(R.id.tv_message2);
            message3=itemView.findViewById(R.id.tv_message3);
            itemView.setOnClickListener(v->{
                if(recyclerViewInterface!=null){
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        recyclerViewInterface.onClick(position,"StudyPrepMaterial");
                    }
                }
            });
        }
    }
}
