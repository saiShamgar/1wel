package com.example.sss.wel.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sss.wel.Models.SearchItems;
import com.example.sss.wel.Models.Services;
import com.example.sss.wel.R;
import com.example.sss.wel.UI.providers.ProviderSignUpActivity;

import java.security.PrivateKey;
import java.util.List;

public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerAdapter.MainActivityViewHolder> {


    private Context context;
    private List<SearchItems> searchItems;

    public MainActivityRecyclerAdapter( Context context,List<SearchItems> searchItems) {

        this.context = context;
        this.searchItems=searchItems;
    }

    @NonNull
    @Override
    public MainActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.main_search_activity_items,parent,false);
        return new MainActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, int position) {

        holder.search_name_db.setText(searchItems.get(position).getName());
        holder.search_category_from_db.setText(searchItems.get(position).getService());
        holder.search_desp.setText(searchItems.get(position).getService_des());
        holder.search_phone_from_db.setText(searchItems.get(position).getPhone());
        holder.search_websiteUrl_from_db.setText(searchItems.get(position).getWebSiteUrl());
        holder.search_address_fromdb.setText(searchItems.get(position).getAddress());

        if(!searchItems.get(position).getImage().equals("")){
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();
            Glide.with(context)
                    .load(searchItems.get(position).getImage())
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.batman)
                    // read original from cache (if present) otherwise download it and decode it
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.search_item_image);
        }
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }


    public class MainActivityViewHolder extends RecyclerView.ViewHolder{

        private TextView search_name_db,search_category_from_db,search_phone_from_db,search_address_fromdb,search_websiteUrl_from_db,search_desp;
        private ImageView search_item_image;

        public MainActivityViewHolder(View itemView) {
            super(itemView);

            search_name_db=itemView.findViewById(R.id.search_name_db);
            search_category_from_db=itemView.findViewById(R.id.search_category_from_db);
            search_phone_from_db=itemView.findViewById(R.id.search_phone_from_db);
            search_address_fromdb=itemView.findViewById(R.id.search_address_fromdb);
            search_websiteUrl_from_db=itemView.findViewById(R.id.search_websiteUrl_from_db);
            search_desp=itemView.findViewById(R.id.search_desp);
            search_item_image=itemView.findViewById(R.id.search_item_image);
        }
    }
}
