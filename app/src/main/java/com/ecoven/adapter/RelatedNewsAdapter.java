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
import com.ecoven.databinding.RelatedNewsItemBinding;
import com.ecoven.databinding.RelatedNewsItemBinding;
import com.ecoven.retrofit.models.SuccessResGetAllNews;

import java.util.ArrayList;

/**
 * Created by Ravindra Birla on 07,July,2021
 */
public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.CategoryViewHolder> {

    private Context context;
    RelatedNewsItemBinding binding;

    private ArrayList<SuccessResGetAllNews.Result> newsList = new ArrayList<>();
    public RelatedNewsAdapter(Context context,ArrayList<SuccessResGetAllNews.Result> newsList)
    {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= RelatedNewsItemBinding.inflate(LayoutInflater.from(context));

        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        TextView tvTitle = holder.itemView.findViewById(R.id.tvTitle);
        ImageView ivRelatedImage = holder.itemView.findViewById(R.id.ivRelatedNews);
        tvTitle.setText(newsList.get(position).getTitle());
        Glide.with(context)
                .load(newsList.get(position).getImage())
                .into(ivRelatedImage);

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(RelatedNewsItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
