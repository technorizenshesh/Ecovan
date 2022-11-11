package com.ecoven.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecoven.R;
import com.ecoven.databinding.NewsItemBinding;
import com.ecoven.retrofit.models.SuccessResGetAllNews;
import com.ecoven.utility.NewsClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravindra Birla on 07,July,2021
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.CategoryViewHolder> {

    private Context context;
    NewsItemBinding binding;

    private ArrayList<SuccessResGetAllNews.Result> newList;

    private String fromWHere;

    public NewsAdapter(Context context,ArrayList<SuccessResGetAllNews.Result> newList,String fromWHere)
    {
        this.context = context;
        this.newList = newList;
        this.fromWHere = fromWHere;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= NewsItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        ImageView ivNews = holder.itemView.findViewById(R.id.ivNews);

        LinearLayout llParent = holder.itemView.findViewById(R.id.llParent);

        TextView tvNews = holder.itemView.findViewById(R.id.tvDescription);

        TextView tvTimeAgo = holder.itemView.findViewById(R.id.tvTimeAgo);

        Glide
                .with(context)
                .load(newList.get(position).getImage())
                .into(ivNews);

        tvNews.setText(newList.get(position).getTitle());
        tvTimeAgo.setText(newList.get(position).getTimeAgo());

        llParent.setOnClickListener(v ->
                {

                    if(fromWHere.equalsIgnoreCase("home"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("id",newList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_newsDetailFragment,bundle);

                    } else if(fromWHere.equalsIgnoreCase("news"))
                    {
                        Bundle bundle = new Bundle();

                        bundle.putString("id",newList.get(position).getId());

                        Navigation.findNavController(v).navigate(R.id.action_newsFragment_to_newsDetailFragment,bundle);

                    }

                }
                );
    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        public CategoryViewHolder(NewsItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
