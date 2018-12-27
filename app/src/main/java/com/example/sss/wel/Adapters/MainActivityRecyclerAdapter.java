package com.example.sss.wel.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sss.wel.Models.Services;
import com.example.sss.wel.R;

import java.util.List;

public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerAdapter.MainActivityViewHolder> {


    private Context context;

    public MainActivityRecyclerAdapter( Context context) {

        this.context = context;
    }

    @NonNull
    @Override
    public MainActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.main_search_activity_items,parent,false);
        return new MainActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 3;
    }


    public class MainActivityViewHolder extends RecyclerView.ViewHolder{

        public MainActivityViewHolder(View itemView) {
            super(itemView);
        }
    }
}
