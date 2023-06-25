package com.example.fooddeliveryapplication.Activities.MyShop;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Dialog.UploadDialog;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityAddFoodBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.net.URI;

public class AddFoodActivity extends AppCompatActivity {
    ActivityAddFoodBinding binding;
    String TAG="Add Food";
    int position;
    ProgressDialog progressDialog;
    UploadDialog uploadDialog;
    Uri uri1,uri2,uri3,uri4;
    String img1="",img2="",img3="",img4="";
    //Biến old để lưu lại giá trị hình cũ cần phải xóa trước khi cập nhật lại
    String imgOld1="",imgOld2="",imgOld3="",imgOld4="";
    Product productUpdate=null;
    boolean checkUpdate=false;
    String userId;
    private static final int FRIST_IMAGE = 1;
    private static final int SECOND_IMAGE = 2;
    private static final int THIRD_IMAGE = 3;
    private static final int FOURTH_IMAGE = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));
        //Nhận intent từ edit--------------
        Intent intentUpdate=getIntent();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (intentUpdate!=null&&intentUpdate.hasExtra("Product updating")) {
            productUpdate= (Product) intentUpdate.getSerializableExtra("Product updating");
            checkUpdate=true;
            binding.lnAddFood.btnAddProduct.setText("Update");
            binding.lnAddFood.edtNameOfProduct.setText(productUpdate.getProductName());
            binding.lnAddFood.edtAmount.setText(productUpdate.getRemainAmount()+"");
            binding.lnAddFood.edtDescp.setText(productUpdate.getDescription());
            binding.lnAddFood.edtPrice.setText(productUpdate.getProductPrice()+"");
            if (productUpdate.getProductType().equals("Drink")) {
                binding.lnAddFood.rbDrink.setChecked(true);
            } else {
                binding.lnAddFood.rbFood.setChecked(true);
            }
            imgOld1=productUpdate.getProductImage1();
            imgOld2=productUpdate.getProductImage2();
            imgOld3=productUpdate.getProductImage3();
            imgOld4=productUpdate.getProductImage4();

            if (!imgOld1.isEmpty())
            {
                binding.layout1.setVisibility(View.GONE);
                Glide.with(this)
                        .asBitmap()
                        .load(imgOld1)
                        .placeholder(R.drawable.background_loading_layout)
                        .into(binding.imgProduct1);
            }
            if (!imgOld2.isEmpty())
            {
                binding.layout2.setVisibility(View.GONE);
                Glide.with(this)
                        .asBitmap()
                        .load(imgOld2)
                        .placeholder(R.drawable.background_loading_layout)
                        .into(binding.imgProduct2);
            }
            if (!imgOld3.isEmpty())
            {
                binding.layout3.setVisibility(View.GONE);
                Glide.with(this)
                        .asBitmap()
                        .load(imgOld3)
                        .placeholder(R.drawable.background_loading_layout)
                        .into(binding.imgProduct3);
            }
            if (!imgOld4.isEmpty())
            {
                binding.layout4.setVisibility(View.GONE);
                Glide.with(this)
                        .asBitmap()
                        .load(imgOld4)
                        .placeholder(R.drawable.background_loading_layout)
                        .into(binding.imgProduct4);
            }

        }
        //---------------------------------
        position=-1;
        binding.addImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position=1;
                pickImg();
            }
        });
        binding.addImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position=2;
                pickImg();
            }
        });
        binding.addImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position=3;
                pickImg();
            }
        });
        binding.addImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position=4;
                pickImg();
            }
        });
        binding.lnAddFood.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLoi()==true) {
                    uploadDialog =new UploadDialog(AddFoodActivity.this);
                    uploadDialog.show();
                    uploadImage(FRIST_IMAGE);
                }
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void deleteOldImage(int position) {
        StringBuilder imageURL=new StringBuilder();
        handleImagePosition(imageURL,position);
        if (!imageURL.toString().isEmpty()) {
                FirebaseStorage.getInstance().getReferenceFromUrl(imageURL.toString()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (position==FOURTH_IMAGE) {
                            uploadDialog.dismiss();
                            Toast.makeText(AddFoodActivity.this,"Đã xóa hình cũ thành công",Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            deleteOldImage(position+1);
                        }
                    } else {
                        Toast.makeText(AddFoodActivity.this,"Bị lỗi xóa ảnh ở hình"+imageURL.toString(),Toast.LENGTH_LONG);
                    }
                }
            });
        } else {
            if (position!=FOURTH_IMAGE) {
                deleteOldImage(position + 1);
            } else {
                uploadDialog.dismiss();
                Toast.makeText(AddFoodActivity.this,"Đã xóa hình cũ thành công",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void handleImagePosition(StringBuilder imageURL, int position) {
        if (position==FRIST_IMAGE) {
            if (!img1.equals(imgOld1)) {
                imageURL.append(imgOld1);
            }
        } else if (position==SECOND_IMAGE) {
            if (!img2.equals(imgOld2)) {
                imageURL.append(imgOld2);
            }
        } else if (position==THIRD_IMAGE){
            if (!img3.equals(imgOld3)) {
                imageURL.append(imgOld3);
            }
        } else {
            if (!img4.equals(imgOld4)) {
                imageURL.append(imgOld4);
            }
        }
    }




    public void xoaHinhDaCo() {
       FirebaseStorage reference= FirebaseStorage.getInstance();
       reference.getReferenceFromUrl(imgOld1).delete();
       reference.getReferenceFromUrl(imgOld2).delete();
       reference.getReferenceFromUrl(imgOld3).delete();
       reference.getReferenceFromUrl(imgOld4).delete();

    }

    public void pickImg() {
           Dexter.withContext(this)
                   .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                   .withListener(new PermissionListener() {
                       @Override
                       public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                           Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                           intent.setType("image/*");
                           pickImageLauncher.launch(intent);
                       }

                       @Override
                       public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                           Toast.makeText(AddFoodActivity.this,"Permission denied!",Toast.LENGTH_SHORT).show();
                       }

                       @Override
                       public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                           permissionToken.continuePermissionRequest();
                           Toast.makeText(AddFoodActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();

                       }
                   }).check();

    }

    public boolean checkLoi() {
        try {
            String name=binding.lnAddFood.edtNameOfProduct.getText().toString();
            Double price=Double.valueOf(binding.lnAddFood.edtPrice.getText().toString()+".0");
            int amount=Integer.valueOf(binding.lnAddFood.edtAmount.getText().toString());
            String description=binding.lnAddFood.edtDescp.getText().toString();
            if ((img1.isEmpty()||img2.isEmpty()||img3.isEmpty()||img4.isEmpty())&&checkUpdate==false) {

                createDialog("Điền đủ 4 hình").create().show();
                return false;
            } else
            if (name.isEmpty()||name.length()<8) {
                createDialog("Tên ít nhất phải từ 8 kí tự và không được bỏ trống").create().show();
                return false;
            } else if (price<5000.0) {
                createDialog("Giá phải từ 5000 trở lên").create().show();
                return false;
            } else if (amount<=0) {
                createDialog("Số lượng phải lớn hơn 0").create().show();
                return false;
            } else if (description.isEmpty()||description.length()<10) {
                createDialog("Phần mô tả phải từ 10 ký tự trở lên và không được bỏ trống").create().show();
                return false;
            }
            return true;
        } catch (Exception e) {
            createDialog("Price và Amount chỉ được nhập ký tự là số và không được bỏ trống").create().show();
            return false;
        }
    }

    public AlertDialog.Builder createDialog(String content) {
       AlertDialog.Builder builder=new AlertDialog.Builder(AddFoodActivity.this);
       builder.setTitle("Thông báo");
       builder.setMessage(content);
       builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.cancel();
           }
       });
       builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.cancel();
           }
       });
       builder.setIcon(R.drawable.icon_dialog_alert_addfood);
       return builder;
    }

    public void uploadProduct(Product tmp) {
       if (checkUpdate==true) {
           tmp.setProductId(productUpdate.getProductId());
           FirebaseDatabase.getInstance().getReference().child("Products").child(productUpdate.getProductId())
                   .setValue(tmp).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               deleteOldImage(FRIST_IMAGE);
                           } else {
                               uploadDialog.dismiss();
                               Toast.makeText(AddFoodActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                                finish();
                           }
                       }
                   });
       } else {
           DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Products").push();
           tmp.setProductId(reference.getKey()+"");
           reference.setValue(tmp).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()) {
                       uploadDialog.dismiss();
                       finish();
                       Toast.makeText(AddFoodActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                   } else {
                       progressDialog.dismiss();
                       Toast.makeText(AddFoodActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
                       Log.e(TAG,"Lỗi thêm sản phẩm");
                   }
               }
           });
       }
    }

    public void uploadImage(int position) {
        Uri uri=uri1;
        if (position==SECOND_IMAGE) {
            uri=uri2;
        }
        if (position==THIRD_IMAGE) {
            uri=uri3;
        }
        if (position==FOURTH_IMAGE) {
            uri=uri4;
        }
        if (uri!=null) {
            FirebaseStorage storage=FirebaseStorage.getInstance();
            StorageReference reference= storage.getReference().child("Product Image").child(System.currentTimeMillis()+"");
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (position==FOURTH_IMAGE) {
                                String img4=uri.toString();
                                String name=binding.lnAddFood.edtNameOfProduct.getText().toString();
                                String price=binding.lnAddFood.edtPrice.getText().toString();
                                String amount=binding.lnAddFood.edtAmount.getText().toString();
                                String description=binding.lnAddFood.edtDescp.getText().toString();
                                Product tmp=new Product("null",name,img1,img2,img3,img4,Integer.valueOf(price),
                                        binding.lnAddFood.rbFood.isChecked()?"Food":"Drink",Integer.valueOf(amount),0,description,0.0,userId);
                                uploadProduct(tmp);
                            } else {
                                if (position==FRIST_IMAGE)  {
                                    img1=uri.toString();
                                } else if (position==SECOND_IMAGE) {
                                    img2=uri.toString();
                                } else {
                                    img3=uri.toString();
                                }
                                uploadImage(position+1);
                            }
                        }
                    });
                }
            });
        } else {
            if (position!=FOURTH_IMAGE) {
                if (position == FRIST_IMAGE) img1 = imgOld1;
                else if (position == SECOND_IMAGE) img2 = imgOld2;
                else if (position == THIRD_IMAGE) img3 = imgOld3;
                uploadImage(position+1);
            }
            else {
                img4=imgOld4;
                String name=binding.lnAddFood.edtNameOfProduct.getText().toString();
                String price=binding.lnAddFood.edtPrice.getText().toString();
                String amount=binding.lnAddFood.edtAmount.getText().toString();
                String description=binding.lnAddFood.edtDescp.getText().toString();
                Product tmp=new Product("null",name,img1,img2,img3,img4,Integer.valueOf(price),
                        binding.lnAddFood.rbFood.isChecked()?"Food":"Drink",Integer.valueOf(amount),0,description,0.0,userId);
                uploadProduct(tmp);
            }
        }

    }
    ActivityResultLauncher pickImageLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
            if (result.getResultCode()==RESULT_OK) {
                Intent intent=result.getData();
                if (intent!=null) {
                    switch (position) {
                        case 1:
                            uri1=intent.getData();
                            img1=uri1.toString();
                            binding.layout1.setVisibility(View.GONE);
                            binding.imgProduct1.setImageURI(uri1);
                            break;
                        case 2:
                            uri2=intent.getData();
                            img2=uri2.toString();
                            binding.layout2.setVisibility(View.GONE);
                            binding.imgProduct2.setImageURI(uri2);
                            break;
                        case 3:
                            uri3=intent.getData();
                            img3=uri3.toString();
                            binding.layout3.setVisibility(View.GONE);
                            binding.imgProduct3.setImageURI(uri3);
                            break;
                        case 4:
                            uri4=intent.getData();
                            img4=uri4.toString();
                            binding.layout4.setVisibility(View.GONE);
                            binding.imgProduct4.setImageURI(uri4);
                            break;
                    }
                }
            }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}