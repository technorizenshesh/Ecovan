package com.ecoven.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import com.ecoven.R;
import com.ecoven.databinding.ActivityEditProfileBinding;

public class EditProfileAct extends AppCompatActivity {

    ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(getString(R.string.edit_profile));
    }
}