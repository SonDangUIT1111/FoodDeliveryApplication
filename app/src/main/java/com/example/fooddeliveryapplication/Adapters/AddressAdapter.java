package com.example.fooddeliveryapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Interfaces.IAddressAdapterListener;
import com.example.fooddeliveryapplication.Models.Address;
import com.example.fooddeliveryapplication.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{
    private Context mContext;
    private List<Address> mAddresses;
    private Address choseAddress;
    private RadioButton checkedRadioButton;
    private IAddressAdapterListener addressAdapterListener;
    private String choseAddressId;

    public AddressAdapter(Context mContext, List<Address> mAddresses, String choseAddressId) {
        this.mContext = mContext;
        this.mAddresses = mAddresses;
        this.choseAddressId = choseAddressId;
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

        if (address.getAddressId().equals(choseAddressId) && address.getState().equals("default")) {
            holder.choose.setChecked(true);
            holder.defaultText.setVisibility(View.VISIBLE);
            choseAddress = address;
            checkedRadioButton = holder.choose;
        }
        else if (address.getAddressId().equals(choseAddressId)) {
            holder.choose.setChecked(true);
            holder.defaultText.setVisibility(View.INVISIBLE);
            choseAddress = address;
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

        holder.choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkedRadioButton.setChecked(false);

                    if (addressAdapterListener != null) {
                        addressAdapterListener.onCheckedChanged(address);
                    }

                    choseAddress = address;
                    checkedRadioButton = holder.choose;
                }
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
