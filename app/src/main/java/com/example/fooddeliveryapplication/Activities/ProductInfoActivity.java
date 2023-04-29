package com.example.fooddeliveryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.widget.RatingBar;

import com.example.fooddeliveryapplication.Adapters.CommentRecyclerViewAdapter;
import com.example.fooddeliveryapplication.Adapters.ProductInfoImageAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseProductInfoHelper;
import com.example.fooddeliveryapplication.Model.Comment;
import com.example.fooddeliveryapplication.R;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductInfoActivity extends AppCompatActivity {

    ViewPager pagerProductImage;
    TabLayout tabDots;
    RatingBar ratingBar;
    RecyclerView recComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);


        // find view by id
        pagerProductImage = (ViewPager) findViewById(R.id.pagerProductImage);
        tabDots = (TabLayout) findViewById(R.id.tabDots);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        recComment = (RecyclerView) findViewById(R.id.recComment);

        // set up default value
        tabDots.setupWithViewPager(pagerProductImage, true);
        ratingBar.setRating(Float.parseFloat("4.5"));



        // set Adapter for image slider
        ProductInfoImageAdapter adapterPager = new ProductInfoImageAdapter(this);
        String[] imageUrl = new String[]{"https://images.unsplash.com/photo-1575936123452-b67c3203c357?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8aW1hZ2V8ZW58MHx8MHx8&w=1000&q=80",
        "https://media.istockphoto.com/id/1322277517/photo/wild-grass-in-the-mountains-at-sunset.jpg?s=612x612&w=0&k=20&c=6mItwwFFGqKNKEAzv0mv6TaxhLN3zSE43bWmFN--J5w=","data:image/jpeg;base64,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"};
        adapterPager.insertImageUrl(imageUrl);
        pagerProductImage.setAdapter(adapterPager);


        // set Adapter for comment recycler view
        new FirebaseProductInfoHelper().readComments(new FirebaseProductInfoHelper.DataStatus() {
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
}