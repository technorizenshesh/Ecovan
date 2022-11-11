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

import com.bumptech.glide.Glide;
import com.ecoven.R;
import com.ecoven.databinding.ProductItemBinding;
import com.ecoven.retrofit.models.GetProductModal;
import java.util.List;

public class ProductAdapter extends
        RecyclerView.Adapter<ProductAdapter.CategoryViewHolder> {

    private final List<GetProductModal.Result> get_result;
    private Context context;
    ProductItemBinding binding;
    private String description;
    private String name;
    private String image;
    private String price;

    public ProductAdapter(Context context, List<GetProductModal.Result> get_result) {
        this.context = context;
        this.get_result = get_result;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ProductItemBinding.inflate(LayoutInflater.from(context));
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder,
                                 int position) {

        description = get_result.get(position).description;
        price = get_result.get(position).price;
        image = get_result.get(position).image;
        name = get_result.get(position).name;


        binding.price.setText(" " + price);
        binding.discription.setText(" " + description);

        if (!image.equals("")) {
            Glide.with(context).load(image).into(binding.image);
        }

        binding.rlOffer.setOnClickListener(v ->
                {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.ticket_continue);

                    ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
                    AppCompatButton yes = dialog.findViewById(R.id.btnYEs);
                    AppCompatButton no = dialog.findViewById(R.id.btnNo);

                    ivCancel.setOnClickListener(v1 ->
                            {
                                dialog.dismiss();
                            }
                    );

                    yes.setOnClickListener(v1 ->
                            {
                                Navigation.findNavController(v).navigate(R.id.action_navigation_products_to_radeemTicketFragment);
                                dialog.dismiss();
                            }
                    );
                    no.setOnClickListener(v1 ->
                            {
                                dialog.dismiss();
                            }
                    );

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    dialog.show();
                }
        );

    }

    @Override
    public int getItemCount() {
        return get_result.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public CategoryViewHolder(ProductItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
