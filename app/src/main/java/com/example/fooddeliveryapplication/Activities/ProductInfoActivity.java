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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    int productPrice;
    String productDescription;
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
        productPrice = 50000;
        productImage1 = "https://cuahang.takyfood.com.vn/vnt_upload/news/01_2019/mon-ngon-10/sup-ga-ngo/sup-ga-ngo-kem-11.jpg";
        productImage2 = "https://huongsen.vn/wp-content/uploads/2019/06/sup-ngo-kem-3.jpg";
        productImage3 = "https://bepnhamo.vn/wp-content/uploads/2022/04/cach-nau-sup-ga-thap-cam-e1532943924981.jpg";
        ratingStar = Float.parseFloat("4.2");
        userName = "Đặng Thái Sơn";
        productDescription = "Súp gà ngô kem thơm ngon bổ dưỡng";
        userId = "user3";


        // find view by id
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnAddFavourite = (ImageButton) findViewById(R.id.btnAddFavourite);
        pagerProductImage = (ViewPager) findViewById(R.id.pagerProductImage);
        tabDots = (TabLayout) findViewById(R.id.tabDots);
        txtNameProduct = (TextView) findViewById(R.id.txtNameProduct);
        txtDescription = (TextView) findViewById(R.id.txtDesciption);
        txtPriceProduct = (TextView) findViewById(R.id.txtPriceProduct);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        recComment = (RecyclerView) findViewById(R.id.recComment);
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);

        // set up default value

        //Todo setText for the product name and price and image
        txtNameProduct.setText(productName);
        txtPriceProduct.setText(String.valueOf(productPrice)+" VNĐ");
        txtDescription.setText(productDescription);
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
        final boolean[] isCartExists = new boolean[1];
        final boolean[] isProductExists = new boolean[1];
        final String[] keyOfCart = {""};
        final String[] keyOfProduct = {""};
        final Cart[] currentCart = {new Cart()};
        new FirebaseArtToCartHelper().readCarts(userId, productId, new FirebaseArtToCartHelper.DataStatus() {

            @Override
            public void DataIsLoaded(Cart cart, String keyCart, String keyProduct, boolean isExistsCart, boolean isExistsProduct) {
                isCartExists[0] = isExistsCart;
                isProductExists[0] = isExistsProduct;
                keyOfCart[0] = keyCart;
                keyOfProduct[0] = keyProduct;
                currentCart[0] = cart;
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


        //add to cart process
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo take amount from user input
                updateCart(isCartExists[0],isProductExists[0],keyOfCart[0],keyOfProduct[0],currentCart[0],1);
            }
        });
    }






    // DEFINE FUNCTION

    public void setCommentRecView()
    {
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

    }
    public void updateCart(boolean isCartExists, boolean isProductExists,String keyCart,String keyProduct,Cart currentCart,int amount)
    {   // truong hop user moi tao chua co gio hang
        if (isCartExists == false)
        {
            Cart cart = new Cart();
            CartInfo cartInfo = new CartInfo();
            cartInfo.productId = productId;
            cartInfo.amount = amount;
            cartInfo.productPrice = productPrice;
            cartInfo.productName = productName;
            cartInfo.productImage = productImage1;
            cartInfo.cartInfoId = "cartInfoId";
            List<CartInfo> cartInfos = new ArrayList<>();
            cartInfos.add(cartInfo);
            cart.cartInfos = cartInfos;
            cart.totalAmount = amount;
            cart.totalPrice = productPrice*amount;
            cart.userId = userId;
            cart.userName = userName;
            cart.cartId = "cartId";
            new FirebaseArtToCartHelper().addCarts(cart, new FirebaseArtToCartHelper.DataStatus() {
                @Override
                public void DataIsLoaded(Cart cart, String keyCart, String keyProduct, boolean isExistsCart, boolean isExistsProduct) {

                }

                @Override
                public void DataIsInserted() {
                    Toast.makeText(ProductInfoActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void DataIsUpdated() {}

                @Override
                public void DataIsDeleted() {}
            });
        }
        else {
            // truong hop chua co san pham hien tai trong gio hang
            if (isProductExists == false)
            {
                CartInfo cartInfo = new CartInfo();
                cartInfo.productId = productId;
                cartInfo.amount = amount;
                cartInfo.productPrice = productPrice;
                cartInfo.productName = productName;
                cartInfo.productImage = productImage1;
                cartInfo.cartInfoId = "cartInfoId";
                List<CartInfo> cartInfos = currentCart.cartInfos;
                cartInfos.add(cartInfo);
                currentCart.cartInfos = cartInfos;
                currentCart.totalAmount = currentCart.totalAmount + amount;
                currentCart.totalPrice = currentCart.totalPrice + amount*productPrice;
                new FirebaseArtToCartHelper().updateCart(keyCart, currentCart, new FirebaseArtToCartHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(Cart cart, String keyCart, String keyProduct, boolean isExistsCart, boolean isExistsProduct) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(ProductInfoActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
            else {  // truong hop da co san pham hien tai trong gio hang
                for (int i = 0;i<currentCart.cartInfos.size();i++)
                {
                    if (currentCart.cartInfos.get(i).productId.equals(productId))
                    {
                        currentCart.cartInfos.get(i).amount = currentCart.cartInfos.get(i).amount + amount;
                        currentCart.totalAmount = currentCart.totalAmount + amount;
                        currentCart.totalPrice = currentCart.totalPrice + amount * productPrice;
                        break;
                    }
                }
                new FirebaseArtToCartHelper().updateCart(keyCart, currentCart, new FirebaseArtToCartHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(Cart cart, String keyCart, String keyProduct, boolean isExistsCart, boolean isExistsProduct) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(ProductInfoActivity.this, "Sản phẩm đã được thêm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        }
    }

}