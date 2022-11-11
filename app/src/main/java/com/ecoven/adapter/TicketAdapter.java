package com.ecoven.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecoven.databinding.TicketItemBinding;
import com.ecoven.databinding.TicketItemBinding;

/**
 * Created by Ravindra Birla on 07,July,2021
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.CategoryViewHolder> {

    private Context context;
    TicketItemBinding binding;
    
    
    public TicketAdapter(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= TicketItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(TicketItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
