package com.ecoven.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecoven.R;
import com.ecoven.databinding.NewsItemBinding;
import com.ecoven.retrofit.models.SuccessResGetAllNews;
import com.ecoven.utility.NewsClickListener;

import java.util.ArrayList;

/**
 * Created by Ravindra Birla on 07,July,2021
 */
public class NewsNewAdapter extends RecyclerView.Adapter<NewsNewAdapter.CategoryViewHolder> {

    private Context context;
    NewsItemBinding binding;

    private NewsClickListener newsClickListener;

    private ArrayList<SuccessResGetAllNews.Result> newList;

    public NewsNewAdapter(Context context, ArrayList<SuccessResGetAllNews.Result> newList, NewsClickListener newsClickListener)
    {
        this.context = context;
        this.newList = newList;
        this.newsClickListener = newsClickListener;
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

        TextView tvNews = holder.itemView.findViewById(R.id.tvDescription);
        TextView tvTimeAgo = holder.itemView.findViewById(R.id.tvTimeAgo);

        Glide
                .with(context)
                .load(newList.get(position).getImage())
                .centerCrop()
                .into(ivNews);

        tvNews.setText(newList.get(position).getTitle());
        tvTimeAgo.setText(newList.get(position).getTimeAgo());

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
