package com.example.fooddeliveryapplication.CustomMessageBox;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.LayoutAlertDialogBinding;

public class CustomAlertDialog {
    public static AlertDialog alertDialog;
    public static LayoutAlertDialogBinding binding;

    public CustomAlertDialog(Context mContext, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        binding = LayoutAlertDialogBinding.inflate(LayoutInflater.from(mContext));
        builder.setView(binding.getRoot());

        binding.txtContentMessage.setText(content);
        alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public static void showAlertDialog() {
        alertDialog.show();
    }
}
