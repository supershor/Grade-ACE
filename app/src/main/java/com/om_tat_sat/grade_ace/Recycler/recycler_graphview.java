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
    String ogpaType;
    public recycler_graphview(Context context, ArrayList<String>arr, RecyclerInterface recyclerViewInterface){
        this.context=context;
        this.arr=arr;
        this.recyclerViewInterface= recyclerViewInterface;
    }
    public recycler_graphview(Context context, ArrayList<String>arr, RecyclerInterface recyclerViewInterface, String ogpaType){
        this.context=context;
        this.arr=arr;
        this.recyclerViewInterface= recyclerViewInterface;
        this.ogpaType =ogpaType;
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
        if(ogpaType!=null){
            holder.ogpa.append(ogpaType);
            holder.ogpa.setVisibility(View.VISIBLE);
//            holder.viewInsights.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView ogpa;
        TextView viewInsights;
        public ViewHolder(@NonNull View itemView,RecyclerInterface recyclerViewInterface) {
            super(itemView);
            name=itemView.findViewById(R.id.name_info_graphview);
            ogpa=itemView.findViewById(R.id.ogpa_type);
            viewInsights=itemView.findViewById(R.id.viewInsights);
            itemView.setOnClickListener(v -> {
                if(recyclerViewInterface!=null){
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(ogpaType!=null){
                            recyclerViewInterface.onClick(position,ogpaType);
                        }else{
                            recyclerViewInterface.onClick(position);
                        }
                    }
                }
            });
            viewInsights.setOnClickListener(v->{
                if(recyclerViewInterface!=null){
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(ogpaType!=null){
                            recyclerViewInterface.onClick(position,ogpaType+"=Insights");
                        }else{
                            recyclerViewInterface.onClick(position);
                        }
                    }
                }
            });
        }
    }
}