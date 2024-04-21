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
    private final RecyclerInterface recyclerInterface;

    public Recyclerview_for_OGPA_SHOWING(ArrayList<Item> arrayList, Context context, RecyclerInterface recyclerInterface) {
        this.arrayList = arrayList;
        this.context = context;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public Recyclerview_for_OGPA_SHOWING.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_main_page,parent,false);
        return new ViewHolder(view, recyclerInterface);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Recyclerview_for_OGPA_SHOWING.ViewHolder holder, int position) {
        holder.name.setText("NAME :- "+arrayList.get(position).getName());
        holder.ogpa.setText("OGPA :- "+arrayList.get(position).getOgpa());
        holder.sem.setText("SEM :- "+arrayList.get(position).getSem());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView ogpa;
        TextView sem;
        public ViewHolder(@NonNull View itemView,RecyclerInterface recyclerInterface) {
            super(itemView);
            name=itemView.findViewById(R.id.name_recycler);
            ogpa=itemView.findViewById(R.id.ogpa_recycler);
            sem=itemView.findViewById(R.id.sem_recycler);
            itemView.setOnClickListener(v -> {
                if (recyclerInterface!=null){
                    int position=getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        recyclerInterface.onClick(position);
                    }
                }
            });
        }
    }
}
