package com.example.fooddeliveryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.example.fooddeliveryapplication.Helpers.FirebaseFavouriteInfoProductHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseProductInfoHelper;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.CartInfo;
import com.example.fooddeliveryapplication.Model.Comment;
import com.example.fooddeliveryapplication.Model.Favourite;
import com.example.fooddeliveryapplication.Model.FavouriteDetail;
import com.example.fooddeliveryapplication.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoActivity extends AppCompatActivity {

    ViewPager pagerProductImage;
    TabLayout tabDots;
    ImageButton btnBack;
    ImageButton btnAddFavourite;
    ImageButton btnCancelFavourite;
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
        productId = "randomProductId2";
        productName = "Súp gà ngô kem";
        productPrice = 50000;
        productImage1 = "https://cuahang.takyfood.com.vn/vnt_upload/news/01_2019/mon-ngon-10/sup-ga-ngo/sup-ga-ngo-kem-11.jpg";
        productImage2 = "https://huongsen.vn/wp-content/uploads/2019/06/sup-ngo-kem-3.jpg";
        productImage3 = "https://bepnhamo.vn/wp-content/uploads/2022/04/cach-nau-sup-ga-thap-cam-e1532943924981.jpg";
        ratingStar = Float.parseFloat("4.2");
        userName = "Đặng Thái Sơn";
        productDescription = "Súp gà ngô kem thơm ngon bổ dưỡng";
        userId = "randomUserId3";


        // find view by id
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnAddFavourite = (ImageButton) findViewById(R.id.btnAddFavourite);
        btnCancelFavourite = (ImageButton) findViewById(R.id.btnCancelFavourite);
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


        // load data cart
        final boolean[] isCartExists = new boolean[1];
        final boolean[] isProductExists = new boolean[1];
        final String[] keyOfCart = {""};
        final String[] keyOfProduct = {""};
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
        //loadDataFavourite();

        //add to cart process
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo take amount from user input
                updateCart(isCartExists[0],isProductExists[0],currentCart[0],currentCartInfo[0],1);
            }
        });
    }



    // DEFINE FUNCTION

    public void setCommentRecView()
    {
        new FirebaseProductInfoHelper(productId).readComments(new FirebaseProductInfoHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Comment> commentList, List<String> keys) {
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
    public void updateCart(boolean isCartExists, boolean isProductExists,Cart currentCart,CartInfo currentCartInfo,int amount)
    {   // truong hop user moi tao chua co gio hang
        if (isCartExists == false)
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
                        Toast.makeText(ProductInfoActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void DataIsUpdated() {
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
            else {  // truong hop da co san pham hien tai trong gio hang
                currentCart.setTotalAmount(currentCart.getTotalAmount()+amount);
                currentCart.setTotalPrice(currentCart.getTotalPrice()+amount*productPrice);
                currentCartInfo.setAmount(currentCartInfo.getAmount()+amount);
                new FirebaseArtToCartHelper().updateCart(currentCart, currentCartInfo,true, new FirebaseArtToCartHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(Cart cart, CartInfo cartInfo, boolean isExistsCart, boolean isExistsProduct) {

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
//
//    public void loadDataFavourite()
//    {
//        final String[] keyOfFavourite = new String[1];
//        final String[] keyOfFavouriteDetail = new String[1];
//        final boolean[] isExistsFavourite = new boolean[1];
//        final boolean[] isExistsFavouriteDetail = new boolean[1];
//        final Favourite[] currentFavourite = {new Favourite()};
//        new FirebaseFavouriteInfoProductHelper().readFavourite(productId, userId, new FirebaseFavouriteInfoProductHelper.DataStatus() {
//
//
//            @Override
//            public void DataIsLoaded(Favourite favourite, String keyFavourite, String keyFavouriteDetail, boolean isFavouriteExists, boolean isFavouriteDetailExists) {
//                if (isFavouriteDetailExists == true)
//                {
//                    btnAddFavourite.setVisibility(View.GONE);
//                    btnCancelFavourite.setVisibility(View.VISIBLE);
//                }
//                else {
//                    btnAddFavourite.setVisibility(View.VISIBLE);
//                    btnCancelFavourite.setVisibility(View.GONE);
//                }
//                keyOfFavourite[0] = keyFavourite;
//                keyOfFavouriteDetail[0] = keyFavouriteDetail;
//                isExistsFavourite[0] = isFavouriteExists;
//                isExistsFavouriteDetail[0] = isFavouriteDetailExists;
//                currentFavourite[0] = favourite;
//            }
//
//            @Override
//            public void DataIsInserted() {
//
//            }
//
//            @Override
//            public void DataIsUpdated() {
//
//            }
//
//            @Override
//            public void DataIsDeleted() {
//
//            }
//        });
//        btnAddFavourite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // truong hop new user
//                if (isExistsFavourite[0] == false)
//                {
//                    Favourite favourite = new Favourite();
//                    List<FavouriteDetail> details = new ArrayList<>();
//                    FavouriteDetail detail = new FavouriteDetail();
//                    detail.productId = productId;
//                    detail.productName = productName;
//                    detail.productImage = productImage1;
//                    detail.ratingStar = ratingStar;
//                    details.add(detail);
//                    favourite.userId = userId;
//                    favourite.userName = userName;
//                    new FirebaseFavouriteInfoProductHelper().addFavourite(favourite, new FirebaseArtToCartHelper.DataStatus() {
//                        @Override
//                        public void DataIsLoaded(Cart cart, String keyCart, String keyProduct, boolean isExistsCart, boolean isExistsProduct) {
//
//                        }
//
//                        @Override
//                        public void DataIsInserted() {
//                            Toast.makeText(ProductInfoActivity.this, "Đã thêm vào danh mục yêu thích", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void DataIsUpdated() {
//
//                        }
//
//                        @Override
//                        public void DataIsDeleted() {
//
//                        }
//                    });
//                }
//                else {
//                    if (isExistsFavouriteDetail[0] == false)
//                    {
//                        FavouriteDetail detail = new FavouriteDetail();
//                        detail.productId = productId;
//                        detail.productName = productName;
//                        detail.productImage = productImage1;
//                        detail.ratingStar = ratingStar;
//                        currentFavourite[0].favouriteList.add(detail);
//                        new FirebaseFavouriteInfoProductHelper().updateFavourite(keyOfFavourite[0], currentFavourite[0], new FirebaseArtToCartHelper.DataStatus() {
//                            @Override
//                            public void DataIsLoaded(Cart cart, String keyCart, String keyProduct, boolean isExistsCart, boolean isExistsProduct) {
//
//                            }
//
//                            @Override
//                            public void DataIsInserted() {
//
//                            }
//
//                            @Override
//                            public void DataIsUpdated() {
//                                Toast.makeText(ProductInfoActivity.this, "Đã thêm vào danh mục yêu thích", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void DataIsDeleted() {
//
//                            }
//                        });
//                    }
//                }
//            }
//        });
//
//        btnCancelFavourite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for (int i =0;i < currentFavourite[0].favouriteList.size();i++)
//                {
//                    if (currentFavourite[0].favouriteList.get(i).productId.equals(productId)) {
//                        currentFavourite[0].favouriteList.remove(i);
//                        break;
//                    }
//                }
//                new FirebaseFavouriteInfoProductHelper().updateFavourite(keyOfFavourite[0], currentFavourite[0], new FirebaseArtToCartHelper.DataStatus() {
//                    @Override
//                    public void DataIsLoaded(Cart cart, String keyCart, String keyProduct, boolean isExistsCart, boolean isExistsProduct) {
//
//                    }
//
//                    @Override
//                    public void DataIsInserted() {
//
//                    }
//
//                    @Override
//                    public void DataIsUpdated() {
//                        Toast.makeText(ProductInfoActivity.this, "Đã xóa khỏi danh mục yêu thích", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void DataIsDeleted() {
//
//                    }
//                });
//            }
//        });
//    }

}