package com.ecoven.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ecoven.R;
import com.ecoven.databinding.RadeemTicketItemBinding;
import com.ecoven.databinding.RadeemTicketItemBinding;

/**
 * Created by Ravindra Birla on 07,July,2021
 */
public class RadeemTicketAdapter extends RecyclerView.Adapter<RadeemTicketAdapter.CategoryViewHolder> {

    private Context context;
    RadeemTicketItemBinding binding;

    public RadeemTicketAdapter(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= RadeemTicketItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

binding.rlParent.setOnClickListener(v ->
        {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.radeem_ticket_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            dialog.show();
        }
        );

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(RadeemTicketItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
