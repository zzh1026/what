package com.neishenme.what.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.neishenme.what.R;

/**
 * Created by gengxin on 2016/5/4.
 */
public class CarouseViewPagerAdapter extends PagerAdapter {
    private Context context;

    private int[] imgs;
    private View view;

    public CarouseViewPagerAdapter(int[] imgs, Context context) {
        this.imgs = imgs;
        this.context = context;
    }

    @Override
    public int getCount() {

        return imgs.length == 0 ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        view = LayoutInflater.from(context).inflate(R.layout.vp_imgs_item, null);
        ImageView img = (ImageView) view.findViewById(R.id.img);
        int currentPic = position % imgs.length;
        img.setImageResource(imgs[currentPic]);
//        HttpLoader.getImageLoader().get(data.getMainpush().get(position % data.getMainpush().size()).getImage(),
//                ImageLoader.getImageListener(imageView, R.drawable.home_pic_moren, R.drawable.home_pic_moren));
        container.addView(view);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "这是第" + (position % imgs.length) + "图片", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
