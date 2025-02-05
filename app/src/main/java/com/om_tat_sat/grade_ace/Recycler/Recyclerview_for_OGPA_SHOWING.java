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

import java.util.ArrayList;

public class Recyclerview_for_OGPA_SHOWING extends RecyclerView.Adapter<Recyclerview_for_OGPA_SHOWING.ViewHolder> {
    ArrayList<Item> arrayList;
    Context context;

    public Recyclerview_for_OGPA_SHOWING(ArrayList<Item> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Recyclerview_for_OGPA_SHOWING.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_main_page,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Recyclerview_for_OGPA_SHOWING.ViewHolder holder, int position) {
        holder.name.append(arrayList.get(position).getName());
        holder.ogpa.append(arrayList.get(position).getOgpa());
        holder.sem.append(arrayList.get(position).getSem());
        holder.ogpa_type.append(arrayList.get(position).ogpa_type);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView ogpa;
        TextView sem;
        TextView ogpa_type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name_recycler);
            ogpa=itemView.findViewById(R.id.ogpa_recycler);
            sem=itemView.findViewById(R.id.sem_recycler);
            ogpa_type=itemView.findViewById(R.id.ogpa_type);
        }
    }
}
