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
import com.example.fooddeliveryapplication.Activities.Home.LoginActivity;
import com.example.fooddeliveryapplication.Dialog.UploadDialog;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.Model.Comment;
import com.example.fooddeliveryapplication.Model.CurrencyFormatter;
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

public class FeedBackAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<BillInfo> ds;
    FirebaseDatabase db=FirebaseDatabase.getInstance();
    Bill currentBill;
    DatabaseReference billInfoReference=FirebaseDatabase.getInstance().getReference("BillInfos");
    DatabaseReference billReference=FirebaseDatabase.getInstance().getReference("Bills");
    String userId;


    //Contructor
    public FeedBackAdapter(Context context, ArrayList<BillInfo> ds, Bill currentBill,String id) {
        this.context = context;
        this.ds = ds;
        this.currentBill=currentBill;
        this.userId = id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutFeedbackBillifoBinding.inflate(LayoutInflater.from(context),parent,false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder=(ViewHolder) holder;
        viewHolder.binding.edtComment.setText("");
        BillInfo item=ds.get(position);
        //Biến lưu lại rate với star bao nhiêu
        IntegerWrapper starRating= new IntegerWrapper(5);
        //Set sự kiện cho rating star
        setEventForStar(viewHolder,starRating);
        //Cho star 5 được rating khi mới khởi tạo
        viewHolder.binding.star5.performClick();
        //Tìm thông tin products
        db.getReference("Products").child(item.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product tmp=snapshot.getValue(Product.class);
                //set Thông tin
                viewHolder.binding.lnBillInfo.txtPrice.setText(CurrencyFormatter.getFormatter().format(item.getAmount()*Double.valueOf(tmp.getProductPrice()))+"");
                viewHolder.binding.lnBillInfo.txtName.setText(tmp.getProductName());
                viewHolder.binding.lnBillInfo.txtCount.setText(item.getAmount()+"");
                Glide.with(context)
                        .load(tmp.getProductImage1())
                        .placeholder(R.drawable.default_image)
                        .into(viewHolder.binding.lnBillInfo.imgFood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Set một listener theo dõi editText đó có vượt quá 200 kí tự không
        viewHolder.binding.edtComment.addTextChangedListener(new TextWatcher() {
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
                    Toast.makeText(context, "Không vượt quá 200 kí tự", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Không thực hiện gì trong đây cả
            }
        });
        //Set sự kiện cho button
        viewHolder.binding.btnSend.setOnClickListener(view -> {
            if (viewHolder.binding.edtComment.getText().toString().isEmpty()==false) {
                UploadDialog dialog=new UploadDialog(context);
                dialog.show();
                Comment comment=new Comment();
                comment.setCommentDetail(((ViewHolder) holder).binding.edtComment.getText().toString());
                comment.setCommentId("");
                comment.setPublisherId(userId);
                comment.setRating(starRating.getValue());
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Comments").child(item.getProductId()).push();
                comment.setCommentId(reference.getKey());
                reference.setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context,"Cảm ơn bạn đã đánh giá cho sản phẩm",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    updateListBillInfo(item);
                                }
                            }, 4000);
                            //Cập nhật lại danh sách billInfo chưa comment

                        } else {
                            Toast.makeText(context,"Đánh giá cho sản phẩm bị lỗi",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            } else {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.icon_alert);
                builder.setTitle("Chú ý");
                builder.setMessage("Nhớ ghi comment nha bạn ơi!");
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
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
        billInfoReference.child(currentBill.getBillId()).child(item.getBillInfoId()).child("check").setValue(true);
        //Cập nhật lại Bill nếu tất cả BillInfo đã feedback hết
        if (ds.isEmpty()) {
            billReference.child(currentBill.getBillId()).child("checkAllComment")
                    .setValue(true);
            FeedBackActivity activity=getFeedBackActivity(context);
            if (activity!=null) {
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
    public void onStarClicked(View view,ViewHolder viewHolder,IntegerWrapper starRating) {
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
        if (!ds.isEmpty()) {
            return ds.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutFeedbackBillifoBinding binding;
        public ViewHolder(@NonNull LayoutFeedbackBillifoBinding tmp) {
            super(tmp.getRoot());
            binding=tmp;
        }
    }


    class IntegerWrapper {
        private int value;

        public IntegerWrapper(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
