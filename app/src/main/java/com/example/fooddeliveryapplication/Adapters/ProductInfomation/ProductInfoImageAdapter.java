package com.example.fooddeliveryapplication.Adapters.ProductInfomation;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

public class ProductInfoImageAdapter extends PagerAdapter {

    Context mContext;

    public ProductInfoImageAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    private String[] sliderImageURL = new String[]{};

    public  void insertImageUrl(String[] values)
    {
        String[] copyCat = new String[]{};
        int count = 0;
        for (int i = 0;i<values.length;i++)
        {
            if (values[i] != null)
                count++;
        }
        copyCat = new String[count];
        int index = 0;
        if (values[0] != null )
            copyCat[index++] = values[0];
        if (values[1] != null )
            copyCat[index++] = values[1];
        if (values[2] != null )
            copyCat[index++] = values[2];
        if (values[3] != null )
            copyCat[index++] = values[3];

        sliderImageURL = copyCat;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext)
                .asBitmap()
                .load(sliderImageURL[position])
                .into(imageView);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return sliderImageURL.length;
    }
}
