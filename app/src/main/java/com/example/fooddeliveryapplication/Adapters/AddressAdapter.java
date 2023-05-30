package com.example.fooddeliveryapplication.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.EditProfileActivity;
import com.example.fooddeliveryapplication.GlobalConfig;
import com.example.fooddeliveryapplication.Interfaces.IAddressAdapterListener;
import com.example.fooddeliveryapplication.Models.Address;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.UpdateAddAddressActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{
    private Context mContext;
    private List<Address> mAddresses;
    private RadioButton checkedRadioButton;
    private IAddressAdapterListener addressAdapterListener;

    public AddressAdapter(Context mContext, List<Address> mAddresses) {
        this.mContext = mContext;
        this.mAddresses = mAddresses;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_address, parent, false);
        return new AddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        Address address = mAddresses.get(position);

        if (address.getAddressId().equals(GlobalConfig.choseAddressId) && address.getState().equals("default")) {
            holder.choose.setChecked(true);
            holder.defaultText.setVisibility(View.VISIBLE);
            checkedRadioButton = holder.choose;
        }
        else if (address.getAddressId().equals(GlobalConfig.choseAddressId)) {
            holder.choose.setChecked(true);
            holder.defaultText.setVisibility(View.INVISIBLE);
            checkedRadioButton = holder.choose;
        }
        else if (address.getState().equals("default")) {
            holder.defaultText.setVisibility(View.VISIBLE);
            holder.choose.setChecked(false);
        }
        else {
            holder.choose.setChecked(false);
            holder.defaultText.setVisibility(View.INVISIBLE);
        }
        holder.receiverName.setText(address.getReceiverName());
        holder.receiverPhoneNumber.setText(address.getReceiverPhoneNumber());
        holder.detailAddress.setText(address.getDetailAddress());

        holder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.choose.isChecked()) {
                    checkedRadioButton.setChecked(false);

                    if (addressAdapterListener != null) {
                        addressAdapterListener.onCheckedChanged(address);
                    }

                    checkedRadioButton = holder.choose;
                }
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalConfig.updateAddressId = address.getAddressId();
                Intent intent = new Intent(mContext, UpdateAddAddressActivity.class);
                intent.putExtra("mode", "update");
                mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setMessage("Delete this address?");
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (address.getState().equals("default")) {
                            Toast.makeText(mContext, "You cannot delete the default address", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                        else {
                            FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(address.getAddressId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(mContext, "Delete address successfully!", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                }
                            });
                        }
                    }
                });
                alertDialog.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddresses.size();
    }

    public void setAddressAdapterListener(IAddressAdapterListener addressAdapterListener) {
        this.addressAdapterListener = addressAdapterListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RadioButton choose;
        public TextView receiverName;
        public TextView receiverPhoneNumber;
        public TextView detailAddress;
        public TextView defaultText;
        public TextView update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            choose = itemView.findViewById(R.id.choose);
            receiverName = itemView.findViewById(R.id.receiver_name);
            receiverPhoneNumber = itemView.findViewById(R.id.receiver_phone_number);
            detailAddress = itemView.findViewById(R.id.detail_address);
            defaultText = itemView.findViewById(R.id.default_text);
            update = itemView.findViewById(R.id.update);
        }
    }
}
