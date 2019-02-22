package com.shamgar.sss.wel.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamgar.sss.wel.R;

import java.util.ArrayList;

public class AdvertisementAdapter extends RecyclerView.Adapter<AdvertisementAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> advtList;

    public AdvertisementAdapter(Context context,ArrayList<String> advtList) {
      this.context=context;
      this.advtList=advtList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.advertisement_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.advtText.setText(advtList.get(position));

    }

    @Override
    public int getItemCount() {
        return advtList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView advtText;
        public ViewHolder(View itemView) {
            super(itemView);

            advtText=itemView.findViewById(R.id.advtText);
        }
    }
}
