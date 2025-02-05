package com.om_tat_sat.grade_ace.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.om_tat_sat.grade_ace.Interface.RecyclerInterface;
import com.om_tat_sat.grade_ace.R;
import com.om_tat_sat.grade_ace.data_holders.PYQ_DataHolder;

import java.util.ArrayList;

public class recycler_PYQ extends RecyclerView.Adapter<recycler_PYQ.ViewHolder> {
    Context context;
    ArrayList<PYQ_DataHolder> arr;
    private final RecyclerInterface recyclerViewInterface;
    public recycler_PYQ(RecyclerInterface recyclerViewInterface, Context context, ArrayList<PYQ_DataHolder> arr) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public recycler_PYQ.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.pyq_name_shower_new_ui,parent,false);
        return new recycler_PYQ.ViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull recycler_PYQ.ViewHolder holder, int position) {
        holder.name.setText(arr.get(position).getName());
        holder.sem.append(arr.get(position).getSemester());
        holder.tv_contains_pyq.append(" :-"+arr.get(position).getTargeted());
        holder.user_message.setVisibility(View.GONE);
        if(arr.get(position).getMessage()!=null && !arr.get(position).getMessage().isEmpty()){
            holder.user_message.append(arr.get(position).getMessage());
            holder.user_message.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sem;
        TextView tv_contains_pyq;
        TextView user_message;
        TextView name;
        public ViewHolder(@NonNull View itemView,RecyclerInterface recyclerViewInterface) {
            super(itemView);
            sem=itemView.findViewById(R.id.tv_semester);
            tv_contains_pyq=itemView.findViewById(R.id.tv_contains_pyq);
            user_message=itemView.findViewById(R.id.tv_user_message);
            name=itemView.findViewById(R.id.tv_name);
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
