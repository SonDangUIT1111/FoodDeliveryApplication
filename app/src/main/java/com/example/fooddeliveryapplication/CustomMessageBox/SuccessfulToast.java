package com.example.fooddeliveryapplication.CustomMessageBox;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.fooddeliveryapplication.databinding.LayoutSuccessfulToastBinding;

public class SuccessfulToast {
    private static LayoutSuccessfulToastBinding binding;
    private static Toast toast;

    public SuccessfulToast(Context mContext, String content) {
        binding = LayoutSuccessfulToastBinding.inflate(LayoutInflater.from(mContext));

        toast = new Toast(mContext);
        toast.setView(binding.getRoot());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);

        binding.layoutSuccessfulToast.setTranslationX(-2000);
        binding.backgroundToast.setTranslationX(-2000);

        binding.txtContentMessage.setText(content);
    }

    public static void showToast() {
        toast.show();
        binding.layoutSuccessfulToast.animate().translationX(0).setDuration(1000).setStartDelay(0);
        binding.backgroundToast.animate().translationX(0).setDuration(800).setStartDelay(2500);
    }
}
