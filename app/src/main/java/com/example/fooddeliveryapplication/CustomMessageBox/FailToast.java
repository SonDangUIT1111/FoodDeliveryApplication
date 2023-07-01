package com.example.fooddeliveryapplication.CustomMessageBox;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.fooddeliveryapplication.R;

public class FailToast {
    public FailToast() {
    }

    public static void showToast(Context mContext, String content)
    {
        Toast toast = new Toast(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_fail_toast,null);
        TextView txtContent = view.findViewById(R.id.txtContentMessage);
        txtContent.setText(content);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
        CardView layout_fail_toast = view.findViewById(R.id.layout_fail_toast);
        LinearLayout backgroundToast = view.findViewById(R.id.backgroundFailToast);
        layout_fail_toast.setTranslationX(-2000);
        backgroundToast.setTranslationX(-2000);
        layout_fail_toast.animate().translationX(0).setDuration(1000).setStartDelay(0);
        backgroundToast.animate().translationX(0).setDuration(800).setStartDelay(2500);
    }
}
