package com.example.fooddeliveryapplication.Adapters.FeedbackAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Activities.Feedback.FeedBackActivity;
import com.example.fooddeliveryapplication.CustomMessageBox.FailToast;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.Dialog.UploadDialog;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseProductInfoHelper;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.Model.Comment;
import com.example.fooddeliveryapplication.Model.CurrencyFormatter;
import com.example.fooddeliveryapplication.Model.IntegerWrapper;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.LayoutFeedbackBillifoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<BillInfo> ds;
    private final Bill currentBill;
    private final String userId;

    //Contructor
    public FeedBackAdapter(Context mContext, ArrayList<BillInfo> ds, Bill currentBill,String id) {
        this.mContext = mContext;
        this.ds = ds;
        this.currentBill = currentBill;
        this.userId = id;
    }

    @NonNull
    @Override
    public FeedBackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeedBackAdapter.ViewHolder(LayoutFeedbackBillifoBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedBackAdapter.ViewHolder holder, int position) {
        BillInfo item=ds.get(position);

        holder.binding.edtComment.setText("");
        //Biến lưu lại rate với star bao nhiêu
        IntegerWrapper starRating = new IntegerWrapper(5);

        //Set sự kiện cho rating star
        setEventForStar(holder, starRating);

        //Cho star 5 được rating khi mới khởi tạo
        holder.binding.star5.performClick();

        //Tìm thông tin products
        FirebaseDatabase.getInstance().getReference("Products").child(item.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product tmp=snapshot.getValue(Product.class);
                //set Thông tin
                holder.binding.lnBillInfo.txtPrice.setText(CurrencyFormatter.getFormatter().format(item.getAmount()*Double.valueOf(tmp.getProductPrice()))+"");
                holder.binding.lnBillInfo.txtName.setText(tmp.getProductName());
                holder.binding.lnBillInfo.txtCount.setText("Count: " +item.getAmount()+"");
                Glide.with(mContext)
                        .load(tmp.getProductImage1())
                        .placeholder(R.drawable.default_image)
                        .into(holder.binding.lnBillInfo.imgFood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Set một listener theo dõi editText đó có vượt quá 200 kí tự không
        holder.binding.edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Không thực hiện gì trong đây cả
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                // Kiểm tra độ dài của văn bản sau mỗi lần thay đổi
                // Văn bản đã đạt tới giới hạn 200 kí tự, không cho phép nhập thêm
                // Văn bản chưa đạt tới giới hạn 200 kí tự, cho phép nhập tiếp
                if (s.length() >= 200)
                    new FailToast(mContext, "Your comment's length must not be over 200 characters!").showToast();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Không thực hiện gì trong đây cả
            }
        });
        //Set sự kiện cho button
        holder.binding.btnSend.setOnClickListener(view -> {
            if (!holder.binding.edtComment.getText().toString().isEmpty()) {
                UploadDialog dialog = new UploadDialog(mContext);
                dialog.show();

                String commentId = FirebaseDatabase.getInstance().getReference().push().getKey();
                Comment comment = new Comment(holder.binding.edtComment.getText().toString().trim(), commentId, userId, starRating.getValue());
                FirebaseDatabase.getInstance().getReference("Comments").child(item.getProductId()).child(commentId).setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            new SuccessfulToast(mContext, "Thank you for giving feedback to my product!").showToast();
                            pushNotificationFeedBack(item);
                            dialog.dismiss();
                            updateListBillInfo(item);

                            FirebaseDatabase.getInstance().getReference().child("Products").child(item.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int ratingAmount = snapshot.child("ratingAmount").getValue(int.class) + 1;
                                    double ratingStar = (snapshot.child("ratingStar").getValue(double.class) * snapshot.child("ratingAmount").getValue(int.class) + starRating.getValue()) / ratingAmount;
                                    FirebaseDatabase.getInstance().getReference().child("Products").child(item.getProductId()).child("ratingAmount").setValue(ratingAmount);
                                    FirebaseDatabase.getInstance().getReference().child("Products").child(item.getProductId()).child("ratingStar").setValue(ratingStar);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            new FailToast(mContext, "Some errors occurred!").showToast();
                            dialog.dismiss();
                        }
                    }
                });
            } else {
                AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setIcon(R.drawable.icon_alert);
                builder.setTitle("Chú ý");
                builder.setMessage("Nhớ ghi comment nha bạn ơi!");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });
    }

    public void updateListBillInfo(BillInfo item) {
        ds.remove(item);
        notifyDataSetChanged();
        //Cập nhật lại biến check cho billInfo đó
        FirebaseDatabase.getInstance().getReference("BillInfos").child(currentBill.getBillId()).child(item.getBillInfoId()).child("check").setValue(true);
        //Cập nhật lại Bill nếu tất cả BillInfo đã feedback hết
        if (ds.isEmpty()) {
            FirebaseDatabase.getInstance().getReference("Bills").child(currentBill.getBillId()).child("checkAllComment")
                    .setValue(true);
            FeedBackActivity activity = getFeedBackActivity(mContext);
            if (activity != null) {
                activity.finish();
            }

        }
    }

    private void setEventForStar(ViewHolder viewHolder,IntegerWrapper starRating) {
        viewHolder.binding.star1.setOnClickListener(view -> onStarClicked(view,viewHolder,starRating));
        viewHolder.binding.star2.setOnClickListener(view -> onStarClicked(view,viewHolder,starRating));
        viewHolder.binding.star3.setOnClickListener(view -> onStarClicked(view,viewHolder,starRating));
        viewHolder.binding.star4.setOnClickListener(view -> onStarClicked(view,viewHolder,starRating));
        viewHolder.binding.star5.setOnClickListener(view -> onStarClicked(view,viewHolder,starRating));
    }

    public static FeedBackActivity getFeedBackActivity(Context context) {
        if (context instanceof FeedBackActivity) {
            return (FeedBackActivity) context;
        }
        return null;
    }
    public void onStarClicked(View view,ViewHolder viewHolder, IntegerWrapper starRating) {
        int clickedStarPosition = Integer.parseInt(view.getTag().toString());
        starRating.setValue(clickedStarPosition);
        viewHolder.binding.star1.setImageResource(clickedStarPosition >= 1 ? R.drawable.star_filled : R.drawable.star_none);
        viewHolder.binding.star2.setImageResource(clickedStarPosition >= 2 ? R.drawable.star_filled : R.drawable.star_none);
        viewHolder.binding.star3.setImageResource(clickedStarPosition >= 3 ? R.drawable.star_filled : R.drawable.star_none);
        viewHolder.binding.star4.setImageResource(clickedStarPosition >= 4 ? R.drawable.star_filled : R.drawable.star_none);
        viewHolder.binding.star5.setImageResource(clickedStarPosition >= 5 ? R.drawable.star_filled : R.drawable.star_none);
    }



    @Override
    public int getItemCount() {
        return ds == null ? 0 : ds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutFeedbackBillifoBinding binding;

        public ViewHolder(@NonNull LayoutFeedbackBillifoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void pushNotificationFeedBack(BillInfo billInfo) {
        FirebaseDatabase.getInstance().getReference().child("Products").child(billInfo.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                String title = "Product feedback";
                String content = "Your product '" +product.getProductName() + "' have just got a new feedback. Go to product information to check it.";
                Notification notification = FirebaseNotificationHelper.createNotification(title, content, product.getProductImage1(), product.getProductId(), "None", "None", null);
                new FirebaseNotificationHelper(mContext).addNotification(product.getPublisherId(), notification, new FirebaseNotificationHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Notification> notificationList, List<Notification> notificationListToNotify) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
