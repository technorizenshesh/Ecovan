package com.ecoven.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecoven.R;
import com.ecoven.databinding.MachineListBinding;
import com.ecoven.retrofit.models.MachineListModal;

import java.util.List;

public class MachineAdapter extends
        RecyclerView.Adapter<MachineAdapter.CategoryViewHolder> {

    private final List<MachineListModal.Result> get_result;
    private Context context;
    MachineListBinding binding;
    private String description;
    private String name;
    private String image;
    private String price;
    private String status;
    private String schedule;
    private String location;
    private String lat;
    private String lon;

    public MachineAdapter(Context context,
                          List<MachineListModal.Result> get_result) {
        this.context = context;
        this.get_result = get_result;
    }
    @NonNull
    @Override
    public CategoryViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = MachineListBinding.inflate(LayoutInflater.from(context));
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        image = get_result.get(position).image;
        name = get_result.get(position).name;
        schedule = get_result.get(position).schedule;
        location = get_result.get(position).location;
        lat = get_result.get(position).lat;
        lon = get_result.get(position).lon;
        status = get_result.get(position).status;
        binding.name.setText(" " + name);
        binding.tvdescription.setText(" " + location);
        binding.status.setText(" " + status);
        binding.schedule.setText(context.getString(R.string.machine_a)+" "+get_result.get(position).getDistance()+" "+context.getString(R.string.km_away));
        if (!image.equals("")) {
            Glide.with(context).load(image).into(binding.imageMachine);
        }

        binding.tvTime.setOnClickListener(v ->
                {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.time_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    AppCompatButton to_closed = dialog.findViewById(R.id.to_closed);
                    TextView time = dialog.findViewById(R.id.time);
                    time.setText(context.getString(R.string.monday_to_friday)+" " + schedule);

                    to_closed.setOnClickListener(v1 -> {
                        dialog.hide();
                    });

                    dialog.show();
                }
        );

        binding.map.setOnClickListener(v -> {
            String uri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return get_result.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public CategoryViewHolder(MachineListBinding binding1) {
            super(binding1.getRoot());
        }
    }
}
