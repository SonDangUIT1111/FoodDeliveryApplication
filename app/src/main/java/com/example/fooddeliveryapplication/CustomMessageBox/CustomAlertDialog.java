package com.example.fooddeliveryapplication.CustomMessageBox;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fooddeliveryapplication.R;

public class CustomAlertDialog {
    public static Button btnYes;
    public static Button btnNo;
    public static AlertDialog alertDialog;

    public CustomAlertDialog(Context mContext,String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_alert_dialog,null);
        builder.setView(view);
        ((TextView)view.findViewById(R.id.txtContentMessage)).setText(content);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);
        alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public static void showAlertDialog()
    {
        alertDialog.show();
    }

}
