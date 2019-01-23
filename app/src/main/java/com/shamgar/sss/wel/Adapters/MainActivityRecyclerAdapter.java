package com.shamgar.sss.wel.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.shamgar.sss.wel.R;
import com.shamgar.sss.wel.Models.SearchItems;
import com.shamgar.sss.wel.UI.BlockSearchItemActivity;


import java.security.PrivateKey;
import java.util.List;

import javax.xml.validation.Validator;

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
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, final int position) {

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
                    .error(R.drawable.one_wel_loge)
                    // read original from cache (if present) otherwise download it and decode it
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.search_item_image);
        }

        holder.searchItemBlockOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu pm = new PopupMenu(context, v);
                pm.getMenuInflater().inflate(R.menu.post_popup_menu, pm.getMenu());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pm.setGravity(Gravity.END);
                }
                pm.show();

                pm.getMenu().findItem(R.id.invite).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        Intent block=new Intent(context, BlockSearchItemActivity.class);
                        block.putExtra("user_id",searchItems.get(position).getUser_id());
                        block.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(block);
                        return false;
                    }
                });
            }
        });
    }


        @Override
    public int getItemCount() {
        return searchItems.size();
    }


    public class MainActivityViewHolder extends RecyclerView.ViewHolder{

        private TextView search_name_db,search_category_from_db,search_phone_from_db,search_address_fromdb,search_websiteUrl_from_db,search_desp;
        private ImageView search_item_image;
        private ImageButton searchItemBlockOption;

        public MainActivityViewHolder(View itemView) {
            super(itemView);

            search_name_db=itemView.findViewById(R.id.search_name_db);
            search_category_from_db=itemView.findViewById(R.id.search_category_from_db);
            search_phone_from_db=itemView.findViewById(R.id.search_phone_from_db);
            search_address_fromdb=itemView.findViewById(R.id.search_address_fromdb);
            search_websiteUrl_from_db=itemView.findViewById(R.id.search_websiteUrl_from_db);
            search_desp=itemView.findViewById(R.id.search_desp);
            search_item_image=itemView.findViewById(R.id.search_item_image);
            searchItemBlockOption=itemView.findViewById(R.id.searchItemBlockOption);
        }
    }
}
