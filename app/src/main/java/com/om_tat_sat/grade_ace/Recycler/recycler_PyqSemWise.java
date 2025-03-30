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

public class recycler_PyqSemWise extends RecyclerView.Adapter<recycler_PyqSemWise.ViewHolder>{
    Context context;
    ArrayList<Study_Prep_Material_Holder> arr;
    private final RecyclerInterface recyclerViewInterface;
    public recycler_PyqSemWise(RecyclerInterface recyclerViewInterface, Context context, ArrayList<Study_Prep_Material_Holder> arr) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.arr = arr;
    }
    @NonNull
    @Override
    public recycler_PyqSemWise.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.study_prep_name_shower_new_ui,parent,false);
        return new recycler_PyqSemWise.ViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull recycler_PyqSemWise.ViewHolder holder, int position) {
        if(arr.get(position).getMessage1Name()!=null && arr.get(position).getMessage1Name()!=""){
            holder.name.setText(arr.get(position).getMessage1Name());
            holder.name.append(arr.get(position).getName());
        }else{
            holder.name.setText(arr.get(position).getName());
        }

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView,RecyclerInterface recyclerViewInterface) {
            super(itemView);
            name=itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(v->{
                if(recyclerViewInterface!=null){
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        recyclerViewInterface.onClick(position,"PyqSemWise");
                    }
                }
            });
        }
    }
}
