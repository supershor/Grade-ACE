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

import java.util.ArrayList;

public class recycler_graphview extends RecyclerView.Adapter<recycler_graphview.ViewHolder>{
    Context context;
    ArrayList<String> arr;
    private final RecyclerInterface recyclerViewInterface;
    public recycler_graphview(Context context, ArrayList<String>arr, RecyclerInterface recyclerViewInterface){
        this.context=context;
        this.arr=arr;
        this.recyclerViewInterface= recyclerViewInterface;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.name_recycler_grapg_view,parent,false);
        return new ViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(arr.get(position).replace("_","/"));
    }



    @Override
    public int getItemCount() {
        return arr.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolder(@NonNull View itemView,RecyclerInterface recyclerViewInterface) {
            super(itemView);
            name=itemView.findViewById(R.id.name_info_graphview);
            itemView.setOnClickListener(v -> {
                if(recyclerViewInterface!=null){
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        recyclerViewInterface.onClick(position);
                    }
                }
            });
        }
    }
}