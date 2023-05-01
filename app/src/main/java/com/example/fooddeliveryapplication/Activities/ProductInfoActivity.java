package com.example.fooddeliveryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Adapters.CommentRecyclerViewAdapter;
import com.example.fooddeliveryapplication.Adapters.ProductInfoImageAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseArtToCartHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseProductInfoHelper;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.CartInfo;
import com.example.fooddeliveryapplication.Model.Comment;
import com.example.fooddeliveryapplication.Model.CommentDetail;
import com.example.fooddeliveryapplication.R;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductInfoActivity extends AppCompatActivity {

    ViewPager pagerProductImage;
    TabLayout tabDots;
    ImageButton btnBack;
    ImageButton btnAddFavourite;
    TextView txtNameProduct;
    TextView txtPriceProduct;
    TextView txtDescription;
    Button btnAddToCart;
    RatingBar ratingBar;
    RecyclerView recComment;

    String productId;
    String productName;
    String productPrice;
    Float ratingStar;
    String productImage1;
    String productImage2;
    String productImage3;
    String productImage4;
    String userName;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        //ToDo received input from search product navigate to here
        productId = "product1";
        productName = "Súp gà ngô kem";
        productPrice = "50.000";
        productImage1 = "https://cuahang.takyfood.com.vn/vnt_upload/news/01_2019/mon-ngon-10/sup-ga-ngo/sup-ga-ngo-kem-11.jpg";
        productImage2 = "https://huongsen.vn/wp-content/uploads/2019/06/sup-ngo-kem-3.jpg";
        productImage3 = "https://bepnhamo.vn/wp-content/uploads/2022/04/cach-nau-sup-ga-thap-cam-e1532943924981.jpg";
        ratingStar = Float.parseFloat("4.2");
        userName = "Đặng Thái Sơn";
        userId = "user1";


        // find view by id
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnAddFavourite = (ImageButton) findViewById(R.id.btnAddFavourite);
        pagerProductImage = (ViewPager) findViewById(R.id.pagerProductImage);
        tabDots = (TabLayout) findViewById(R.id.tabDots);
        txtNameProduct = (TextView) findViewById(R.id.txtNameProduct);
        txtDescription = (TextView) findViewById(R.id.txtDesciption);
        txtPriceProduct = (TextView) findViewById(R.id.txtDesciption);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        recComment = (RecyclerView) findViewById(R.id.recComment);
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);

        // set up default value

        //Todo setText for the product name and price and image
        tabDots.setupWithViewPager(pagerProductImage, true);
        ratingBar.setRating(ratingStar);



        // set Adapter for image slider
        ProductInfoImageAdapter adapterPager = new ProductInfoImageAdapter(this);
        String[] imageUrl = new String[4];
        if (productImage1 != null) {
            imageUrl[0] = productImage1;
        }
        if (productImage2 != null) {
            imageUrl[1] = productImage2;
        }
        if (productImage1 != null) {
            imageUrl[2] = productImage3;
        }
        if (productImage1 != null) {
            imageUrl[3] = productImage4;
        }

        adapterPager.insertImageUrl(imageUrl);
        pagerProductImage.setAdapter(adapterPager);



        // load data
        final int[] positionOfCart = new int[1];
        positionOfCart[0] = -1;
        final String[] key = new String[1];
        final boolean[] flag = {false};
        new FirebaseArtToCartHelper().readCarts(new FirebaseArtToCartHelper.DataStatus() {

            @Override
            public void DataIsLoaded(List<Cart> carts, List<String> keys) {
                for (int i = 0;i<carts.size();i++){
                    if (carts.get(i).getUserId().equals(userName))
                    {
                        positionOfCart[0] = i;
                        key[0] = keys.get(i);
                        break;
                    }
                }
                flag[0] = true;
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {}

            @Override
            public void DataIsDeleted() {}

        });


        // set Adapter for comment recycler view
        //todo import product name branch in to this declare
        new FirebaseProductInfoHelper(productId).readComments(new FirebaseProductInfoHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<CommentDetail> commentList, List<String> keys) {
                CommentRecyclerViewAdapter adapter = new CommentRecyclerViewAdapter(ProductInfoActivity.this,commentList,keys);
                recComment.setHasFixedSize(true);
                recComment.setLayoutManager(new LinearLayoutManager(ProductInfoActivity.this));
                recComment.setAdapter(adapter);
            }

            @Override
            public void DataIsInserted() {}

            @Override
            public void DataIsUpdated() {}

            @Override
            public void DataIsDeleted() {}
        });



        //add to cart process
        //todo add to cart button
//        btnAddToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (positionOfCart[0] == -1)
//                {
//                    updateCart(false);
//
//                }
//                else updateCart(true);
//            }
//        });
    }
//    public void updateCart(boolean value)
//    {
//        if (value == false)
//        {
//            Cart cart = new Cart();
//            CartInfo cartInfo = new CartInfo();
//            cartInfo.productId = productName;
//            cartInfo.amount = 1;
//            cartInfo.price = productPrice;
//            List<CartInfo> cartInfos = new ArrayList<>();
//            cartInfos.add(cartInfo);
//            cart.cartInfos = cartInfos;
//            cart.totalAmount = 1;
//            cart.totalPrice = productPrice;
//            cart.userId = userName;
//            cart.cartId = "cartId";
//            new FirebaseArtToCartHelper().addCarts(cart, new FirebaseArtToCartHelper.DataStatus() {
//                @Override
//                public void DataIsLoaded(List<Cart> carts, List<String> keys) {
//
//                }
//
//                @Override
//                public void DataIsInserted() {
//                    Toast.makeText(ProductInfoActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void DataIsUpdated() {
//
//                }
//
//                @Override
//                public void DataIsDeleted() {
//
//                }
//            });
//        }
//        else {
//            Toast.makeText(this, "Da ton tai", Toast.LENGTH_SHORT).show();
//        }
//    }
}