package com.example.fooddeliveryapplication.Activities.ProductInformation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fooddeliveryapplication.Activities.Home.ChatDetailActivity;
import com.example.fooddeliveryapplication.Activities.MyShop.AddFoodActivity;
import com.example.fooddeliveryapplication.Activities.Order.OrderDetailActivity;
import com.example.fooddeliveryapplication.Adapters.ProductInfomation.CommentRecyclerViewAdapter;
import com.example.fooddeliveryapplication.Adapters.ProductInfomation.ProductInfoImageAdapter;
import com.example.fooddeliveryapplication.CustomMessageBox.FailToast;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.Helpers.FirebaseArtToCartHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseFavouriteInfoProductHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseProductInfoHelper;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.CartInfo;
import com.example.fooddeliveryapplication.Model.Comment;
import com.example.fooddeliveryapplication.Model.CurrencyFormatter;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityProductInfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoActivity extends AppCompatActivity {
    private ActivityProductInfoBinding binding;
    private String productId;
    private String productName;
    private int productPrice;
    private String productDescription;
    private Double ratingStar;
    private String productImage1;
    private String productImage2;
    private String productImage3;
    private String productImage4;
    private String userName;
    private String userId;
    private String publisherId;
    private int sold;
    private String productType;
    private int remainAmount;
    private int ratingAmount;
    private String state;
    private boolean own;
    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));

        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        productName = intent.getStringExtra("productName");
        productPrice = intent.getIntExtra("productPrice",0);
        productImage1 = intent.getStringExtra("productImage1");
        productImage2 = intent.getStringExtra("productImage2");
        productImage3 = intent.getStringExtra("productImage3");
        productImage4 = intent.getStringExtra("productImage4");
        ratingStar = intent.getDoubleExtra("ratingStar",0.0);
        userName = intent.getStringExtra("userName");
        productDescription = intent.getStringExtra("productDescription");
        publisherId = intent.getStringExtra("publisherId");
        userId = intent.getStringExtra("userId");
        sold = intent.getIntExtra("sold",0);
        productType = intent.getStringExtra("productType");
        remainAmount = intent.getIntExtra("remainAmount", 0);
        ratingAmount = intent.getIntExtra("ratingAmount", 0);
        state = intent.getStringExtra("state");

        // set up default value
        binding.txtNameProduct.setText(productName);
        binding.txtPriceProduct.setText(CurrencyFormatter.getFormatter().format(Double.valueOf(productPrice)));
        binding.txtDesciption.setText(productDescription);
        binding.txtSell.setText(String.valueOf(sold));
        binding.ratingBar.setRating(ratingStar.floatValue());
        binding.txtRemainAmount.setText(String.valueOf(remainAmount));
        if (publisherId.equals(userId)) {
            own = true;
            binding.btnAddToCart.setVisibility(View.INVISIBLE);
            binding.btnCancelFavourite.setVisibility(View.INVISIBLE);
            binding.btnAddFavourite.setVisibility(View.INVISIBLE);
            binding.btnChat.setVisibility(View.INVISIBLE);
        }
        else {
            own = false;
            binding.btnEditProduct.setVisibility(View.INVISIBLE);
        }

        // set Adapter for image slider
        ArrayList<String> dsImage =new ArrayList<>();
        if (productImage1 != null) {
            dsImage.add(productImage1);
        }
        if (productImage2 != null) {
            dsImage.add(productImage2);
        }
        if (productImage3 != null) {
            dsImage.add(productImage3);
        }
        if (productImage4 != null) {
            dsImage.add(productImage4);
        }
        ProductInfoImageAdapter imageAdapter2=new ProductInfoImageAdapter(this,dsImage);
        binding.pagerProductImage.setAdapter(imageAdapter2);
        binding.tabDots.attachTo(binding.pagerProductImage);


        // load sell, favourite
        new FirebaseProductInfoHelper(productId).countFavourite(new FirebaseProductInfoHelper.DataStatusCountFavourite() {
            @Override
            public void DataIsLoaded(int countFavourite) {
                binding.txtFavourite.setText(String.valueOf(countFavourite));
            }

            @Override
            public void DataIsInserted() {}

            @Override
            public void DataIsUpdated() {}

            @Override
            public void DataIsDeleted() {}
        });


        // load data cart
        final boolean[] isCartExists = new boolean[1];
        final boolean[] isProductExists = new boolean[1];
        final Cart[] currentCart = {new Cart()};
        final CartInfo[] currentCartInfo = {new CartInfo()};
        new FirebaseArtToCartHelper(userId,productId).readCarts(new FirebaseArtToCartHelper.DataStatus() {

            @Override
            public void DataIsLoaded(Cart cart,CartInfo cartInfo, boolean isExistsCart, boolean isExistsProduct) {
                isCartExists[0] = isExistsCart;
                isProductExists[0] = isExistsProduct;
                currentCart[0] = cart;
                currentCartInfo[0] = cartInfo;
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

        // set Adapter for comment recycler view
        setCommentRecView();

        // load data favourite
        loadDataFavourite();


        //add to cart process
        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCart(isCartExists[0],isProductExists[0],currentCart[0],currentCartInfo[0],1);
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductInfoActivity.this, ChatDetailActivity.class);
                intent.setAction("productInfoActivity");
                intent.putExtra("publisherId", publisherId);
                startActivity(intent);
            }
        });

        binding.btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductInfoActivity.this, AddFoodActivity.class);
                Product product = new Product(productId, productName, productImage1, productImage2, productImage3, productImage4, productPrice, productType, remainAmount, sold, productDescription, ratingStar, ratingAmount, publisherId, state);
                intent.putExtra("Product updating", product);
                startActivity(intent);
            }
        });
    }

    // DEFINE FUNCTION
    public void setCommentRecView()
    {
        new FirebaseProductInfoHelper(productId).readComments(new FirebaseProductInfoHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Comment> commentList,int count, List<String> keys) {
                CommentRecyclerViewAdapter adapter = new CommentRecyclerViewAdapter(ProductInfoActivity.this,commentList,keys);
                binding.recComment.setHasFixedSize(true);
                binding.recComment.setLayoutManager(new LinearLayoutManager(ProductInfoActivity.this));
                binding.recComment.setAdapter(adapter);
                binding.txtRate.setText("("+count+")");
            }

            @Override
            public void DataIsInserted() {}

            @Override
            public void DataIsUpdated() {}

            @Override
            public void DataIsDeleted() {}
        });

    }
    public void updateCart(boolean isCartExists, boolean isProductExists,Cart currentCart,CartInfo currentCartInfo,int amount)
    {
        // truong hop user moi tao chua co gio hang
        if (!isCartExists)
        {
            Cart cart = new Cart();
            cart.setTotalPrice(productPrice*amount);
            cart.setTotalAmount(amount);
            cart.setUserId(userId);
            CartInfo cartInfo = new CartInfo();
            cartInfo.setAmount(amount);
            cartInfo.setProductId(productId);
            new FirebaseArtToCartHelper().addCarts(cart,cartInfo, new FirebaseArtToCartHelper.DataStatus() {
                @Override
                public void DataIsLoaded(Cart cart, CartInfo cartInfo, boolean isExistsCart, boolean isExistsProduct) {

                }

                @Override
                public void DataIsInserted() {
                    new SuccessfulToast(ProductInfoActivity.this, "Added to your favourite list").showToast();
                }

                @Override
                public void DataIsUpdated() {}

                @Override
                public void DataIsDeleted() {}
            });
        }
        else {
            // truong hop chua co san pham hien tai trong gio hang
            if (!isProductExists)
            {
                FirebaseDatabase.getInstance().getReference().child("Products").child(productId).child("remainAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int remainAmount = snapshot.getValue(int.class);
                        if (amount <= remainAmount) {
                            CartInfo cartInfo = new CartInfo();
                            cartInfo.setProductId(productId);
                            cartInfo.setAmount(amount);
                            currentCart.setTotalAmount(currentCart.getTotalAmount()+amount);
                            currentCart.setTotalPrice(currentCart.getTotalPrice()+amount*productPrice);
                            new FirebaseArtToCartHelper().updateCart(currentCart,cartInfo,false ,new FirebaseArtToCartHelper.DataStatus() {

                                @Override
                                public void DataIsLoaded(Cart cart, CartInfo cartInfo, boolean isExistsCart, boolean isExistsProduct) {

                                }

                                @Override
                                public void DataIsInserted() {
                                    new SuccessfulToast(ProductInfoActivity.this, "Added to your cart").showToast();
                                }

                                @Override
                                public void DataIsUpdated() {
                                }

                                @Override
                                public void DataIsDeleted() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else {  // truong hop da co san pham hien tai trong gio hang
                new FailToast(ProductInfoActivity.this,"This product has already been in the cart!").showToast();
            }
        }
    }

    public void loadDataFavourite()
    {
        final boolean[] isExistsFavourite = new boolean[1];
        final boolean[] isExistsFavouriteDetail = new boolean[1];
        new FirebaseFavouriteInfoProductHelper().readFavourite(productId, userId, new FirebaseFavouriteInfoProductHelper.DataStatus() {
            @Override
            public void DataIsLoaded(boolean isFavouriteExists, boolean isFavouriteDetailExists) {
                if (!own) {
                    if (isFavouriteDetailExists)
                    {
                        binding.btnAddFavourite.setVisibility(View.GONE);
                        binding.btnCancelFavourite.setVisibility(View.VISIBLE);
                    }
                    else {
                        binding.btnAddFavourite.setVisibility(View.VISIBLE);
                        binding.btnCancelFavourite.setVisibility(View.GONE);
                    }
                }
                isExistsFavourite[0] = isFavouriteExists;
                isExistsFavouriteDetail[0] = isFavouriteDetailExists;
                // end of load data
                binding.progressBarProductInfo.setVisibility(View.GONE);
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
        binding.btnAddFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // truong hop new user
                if (!isExistsFavourite[0])
                {

                    new FirebaseFavouriteInfoProductHelper().addFavourite(userId,productId, new FirebaseFavouriteInfoProductHelper.DataStatus() {

                        @Override
                        public void DataIsLoaded(boolean isFavouriteExists, boolean isFavouriteDetailExists) {
                            
                        }

                        @Override
                        public void DataIsInserted() {
                            new SuccessfulToast(ProductInfoActivity.this, "Added to your favourite list").showToast();
                            pushNotificationFavourite();
                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
                else {
                    if (isExistsFavouriteDetail[0] == false)
                    {
                        new FirebaseFavouriteInfoProductHelper().addFavourite(userId,productId, new FirebaseFavouriteInfoProductHelper.DataStatus() {


                            @Override
                            public void DataIsLoaded(boolean isFavouriteExists, boolean isFavouriteDetailExists) {
                                
                            }

                            @Override
                            public void DataIsInserted() {
                                new SuccessfulToast(ProductInfoActivity.this,"Added to your favourite list").showToast();
                                pushNotificationFavourite();
                            }

                            @Override
                            public void DataIsUpdated() {
                            
                            }

                            @Override
                            public void DataIsDeleted() {

                            }
                        });
                    }
                }
            }
        });

        binding.btnCancelFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseFavouriteInfoProductHelper().removeFavourite(userId,productId, new FirebaseFavouriteInfoProductHelper.DataStatus() {


                    @Override
                    public void DataIsLoaded(boolean isFavouriteExists, boolean isFavouriteDetailExists) {
                        
                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        new SuccessfulToast(ProductInfoActivity.this, "Removed from your favourite list").showToast();
                    }
                });
            }
        });
    }

    public void pushNotificationFavourite() {
        String title = "Favourite product";
        String content = userName + " liked your product: "+ productName + ". Go to Product Information to check it.";
        Notification notification = FirebaseNotificationHelper.createNotification(title,content,productImage1,productId,"None","None", null);
        new FirebaseNotificationHelper(this).addNotification(publisherId, notification, new FirebaseNotificationHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Notification> notificationList,List<Notification> notificationListToNotify) {

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

}