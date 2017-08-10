package com.neishenme.what.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.net.HttpLoader;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/5/18 21:06
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class ServicePhotoAdapter extends PagerAdapter {
    private View view;
    List<String> photos;

    public ServicePhotoAdapter(List<String> photos) {
        this.photos = photos;
    }

    public void setServicePhotosData(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public int getCount() {
        if (photos == null || photos.size() == 0) {
            return 0;
        }
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        view = View.inflate(App.getApplication(), R.layout.view_page, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.view_page_image);
        HttpLoader.getImageLoader().get(photos.get(position), ImageLoader.getImageListener(
                imageView, R.drawable.picture_moren, R.drawable.picture_moren));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
