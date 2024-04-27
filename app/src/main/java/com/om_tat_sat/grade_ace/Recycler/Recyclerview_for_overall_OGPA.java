package com.om_tat_sat.grade_ace.Recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.om_tat_sat.grade_ace.Interface.RecyclerInterface;
import com.om_tat_sat.grade_ace.R;
import com.om_tat_sat.grade_ace.data_holders.ogpa_holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Recyclerview_for_overall_OGPA extends RecyclerView.Adapter<Recyclerview_for_overall_OGPA.ViewHolder> {
    ArrayList<ArrayList<ogpa_holder>>arrayLists;
    Context context;

    public Recyclerview_for_overall_OGPA(ArrayList<ArrayList<ogpa_holder>>arrayLists, Context context) {
        this.arrayLists = arrayLists;
        this.context = context;
    }

    @NonNull
    @Override
    public Recyclerview_for_overall_OGPA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_main_page,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Recyclerview_for_overall_OGPA.ViewHolder holder, int position) {
        ArrayList<ogpa_holder>arrayList=arrayLists.get(position);
        StringBuilder sem= new StringBuilder();
        double total_ogpa=0D;
        int total_sem=0;
        for (int i = 0; i< arrayList.size(); i++){
            total_sem++;
            total_ogpa+=Double.parseDouble(arrayList.get(i).ogpa);
            if (i!=arrayList.size()-1){
                sem.append(arrayList.get(i).sem).append(", ");
            }else {
                sem.append(arrayList.get(i).sem);
            }
        }
        holder.name.setText("NAME :- "+arrayList.get(0).name);
        holder.ogpa.setText("OGPA :- "+(total_ogpa/total_sem)+"");
        holder.sem.setText("SEM :- "+sem);
    }

    @Override
    public int getItemCount() {
        return arrayLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView ogpa;
        TextView sem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name_recycler);
            ogpa=itemView.findViewById(R.id.ogpa_recycler);
            sem=itemView.findViewById(R.id.sem_recycler);
        }
    }
}
