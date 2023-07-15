package com.example.fooddeliveryapplication.Activities.MyShop;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.CustomMessageBox.FailToast;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
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

public class AddFoodActivity extends AppCompatActivity {
    private ActivityAddFoodBinding binding;
    private String TAG="Add Food";
    private int position;
    private int PERMISSION_REQUEST_CODE = 10001;
    private UploadDialog uploadDialog;
    private Uri uri1,uri2,uri3,uri4;
    private String img1="",img2="",img3="",img4="";
    //Biến old để lưu lại giá trị hình cũ cần phải xóa trước khi cập nhật lại
    private String imgOld1="",imgOld2="",imgOld3="",imgOld4="";
    private Product productUpdate=null;
    private boolean checkUpdate=false;
    private String userId;
    private static final int FIRST_IMAGE = 1;
    private static final int SECOND_IMAGE = 2;
    private static final int THIRD_IMAGE = 3;
    private static final int FOURTH_IMAGE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));
        //Nhận intent từ edit--------------
        Intent intentUpdate=getIntent();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (intentUpdate != null && intentUpdate.hasExtra("Product updating")) {
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
        position = -1;
        binding.addImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 1;
                //checkRuntimePermission();
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                pickImageLauncher.launch(intent);
            }
        });
        binding.addImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 2;
                //checkRuntimePermission();
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                pickImageLauncher.launch(intent);
            }
        });
        binding.addImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 3;
                //checkRuntimePermission();
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                pickImageLauncher.launch(intent);
            }
        });
        binding.addImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 4;
                //checkRuntimePermission();
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                pickImageLauncher.launch(intent);
            }
        });
        binding.lnAddFood.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLoi()) {
                    uploadDialog = new UploadDialog(AddFoodActivity.this);
                    uploadDialog.show();
                    uploadImage(FIRST_IMAGE);
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
                        if (position == FOURTH_IMAGE) {
                            uploadDialog.dismiss();
                            new SuccessfulToast(AddFoodActivity.this, "Delete old image successfully!").showToast();
                            finish();
                        } else {
                            deleteOldImage(position+1);
                        }
                    } else {
                        new FailToast(AddFoodActivity.this, "Error delete image: " + imageURL).showToast();
                    }
                }
            });
        } else {
            if (position!=FOURTH_IMAGE) {
                deleteOldImage(position + 1);
            } else {
                uploadDialog.dismiss();
                new SuccessfulToast(AddFoodActivity.this, "Delete old image successfully!").showToast();
                finish();
            }
        }
    }

    private void handleImagePosition(StringBuilder imageURL, int position) {
        if (position==FIRST_IMAGE) {
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
                        new FailToast(AddFoodActivity.this, "Permission denied!").showToast();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                        new FailToast(AddFoodActivity.this, "Permission denied!").showToast();

                    }
                }).check();

    }

    public boolean checkLoi() {
        try {
            String name=binding.lnAddFood.edtNameOfProduct.getText().toString();
            double price= Double.parseDouble(binding.lnAddFood.edtPrice.getText().toString() + ".0");
            int amount=Integer.parseInt(binding.lnAddFood.edtAmount.getText().toString());
            String description=binding.lnAddFood.edtDescp.getText().toString();
            if (!checkUpdate) {
                if (img1.isEmpty() || img2.isEmpty() || img3.isEmpty() || img4.isEmpty()) {
                    createDialog("Điền đủ 4 hình").create().show();
                    return false;
                } else if (name.isEmpty() || name.length() < 8) {
                    createDialog("Tên ít nhất phải từ 8 kí tự và không được bỏ trống").create().show();
                    return false;
                } else if (price < 5000.0) {
                    createDialog("Giá phải từ 5000 trở lên").create().show();
                    return false;
                } else if (amount <= 0) {
                    createDialog("Số lượng phải lớn hơn 0").create().show();
                    return false;
                } else if (description.isEmpty() || description.length() < 10) {
                    createDialog("Phần mô tả phải từ 10 ký tự trở lên và không được bỏ trống").create().show();
                    return false;
                }
            } else if (name.isEmpty() || name.length() < 8) {
                createDialog("Tên ít nhất phải từ 8 kí tự và không được bỏ trống").create().show();
                return false;
            } else if (price < 5000.0) {
                createDialog("Giá phải từ 5000 trở lên").create().show();
                return false;
            } else if (amount <= 0) {
                createDialog("Số lượng phải lớn hơn 0").create().show();
                return false;
            } else if (description.isEmpty() || description.length() < 10) {
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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setIcon(R.drawable.icon_dialog_alert_addfood);
        return builder;
    }

    public void uploadProduct(Product tmp) {
        if (checkUpdate) {
            tmp.setProductId(productUpdate.getProductId());
            FirebaseDatabase.getInstance().getReference().child("Products").child(productUpdate.getProductId()).setValue(tmp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                StringBuilder imageURL=new StringBuilder();
                                handleImagePosition(imageURL,position);

                                if (!imageURL.toString().isEmpty()) {
                                    FirebaseStorage.getInstance().getReferenceFromUrl(imageURL.toString()).delete();
                                }

                                uploadDialog.dismiss();
                                new SuccessfulToast(AddFoodActivity.this, "Update successfully!").showToast();
                                finish();
                            } else {
                                uploadDialog.dismiss();
                                new FailToast(AddFoodActivity.this, "Some errors occurred!").showToast();
                                finish();
                            }
                        }
                    });
        }
        else {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Products").push();
            tmp.setProductId(reference.getKey()+"");
            reference.setValue(tmp).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        uploadDialog.dismiss();
                        finish();
                        new SuccessfulToast(AddFoodActivity.this, "Add product successfully!").showToast();
                    } else {
                        uploadDialog.dismiss();
                        new FailToast(AddFoodActivity.this, "Some error occurred!").showToast();
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
                            if (position == FOURTH_IMAGE) {
                                String img4=uri.toString();
                                String name=binding.lnAddFood.edtNameOfProduct.getText().toString();
                                String price=binding.lnAddFood.edtPrice.getText().toString();
                                String amount=binding.lnAddFood.edtAmount.getText().toString();
                                String description=binding.lnAddFood.edtDescp.getText().toString();
                                Product tmp = new Product("null", name, img1, img2, img3, img4, Integer.valueOf(price),
                                        binding.lnAddFood.rbFood.isChecked() ? "Food" : "Drink", Integer.valueOf(amount), 0, description, 0.0, 0, userId, "");
                                uploadProduct(tmp);
                            } else {
                                if (position==FIRST_IMAGE)  {
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
                if (position == FIRST_IMAGE) img1 = imgOld1;
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
                        binding.lnAddFood.rbFood.isChecked()?"Food":"Drink", Integer.valueOf(amount), 0, description, 0.0, 0, userId, "");
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

    private void checkRuntimePermission() {
        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            pickImg();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            buildAlertPermissionDialog().create().show();
        } else {
            requestRuntimePermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_REQUEST_CODE) {
            if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                pickImg();
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                buildAlertDeniedPermissionDialog().create().show();
            } else {
                checkRuntimePermission();
            }
        }
    }

    private AlertDialog.Builder buildAlertPermissionDialog() {
        AlertDialog.Builder builderDialog=new AlertDialog.Builder(this);
        builderDialog.setTitle("Notice")
                .setMessage("Bạn cần cấp quyền để thực hiện tính năng này")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestRuntimePermission();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return builderDialog;
    }

    private AlertDialog.Builder buildAlertDeniedPermissionDialog() {
        AlertDialog.Builder builderDialog=new AlertDialog.Builder(this);
        builderDialog.setTitle("Notice")
                .setMessage("Bạn cần vào cài đặt để cài đặt cho tính năng này")
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(createIntentToAppSetting());
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return builderDialog;
    }

    private Intent createIntentToAppSetting() {
        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri= Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        return intent;
    }

    private void requestRuntimePermission() {
        ActivityCompat.requestPermissions(AddFoodActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
        },PERMISSION_REQUEST_CODE);
    }

    private boolean isPermissionGranted(String permission) {
        return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED;
    }
}