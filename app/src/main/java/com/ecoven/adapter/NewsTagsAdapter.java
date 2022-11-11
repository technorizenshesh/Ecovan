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
import com.ecoven.databinding.HashtagsItemBinding;
import com.ecoven.databinding.HashtagsItemBinding;
import com.ecoven.retrofit.models.SuccessResGetAllNews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravindra Birla on 07,July,2021
 */

public class NewsTagsAdapter extends RecyclerView.Adapter<NewsTagsAdapter.CategoryViewHolder> {

    private Context context;
    HashtagsItemBinding binding;

    private List<String> newList;


    public NewsTagsAdapter(Context context, List<String> newList)
    {
        this.context = context;
        this.newList = newList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= HashtagsItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        TextView tvTag = holder.itemView.findViewById(R.id.tvTag);
        tvTag.setText(newList.get(position));
    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        public CategoryViewHolder(HashtagsItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
