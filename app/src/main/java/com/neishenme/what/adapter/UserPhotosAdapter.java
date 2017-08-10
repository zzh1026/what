package com.neishenme.what.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.net.HttpLoader;

import org.seny.android.utils.ALog;

import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 作者：zhaozh create on 2016/5/10 19:14
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class UserPhotosAdapter extends BaseAdapter {
    List<String> photos;
    LayoutInflater mInflater;
    Context mContext;

    public UserPhotosAdapter(Context context, List<String> photos) {
        this.photos = photos;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setPhotos(List<String> photos) {
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
    public String getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ImageView itemUserdetialPhotoImage;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_userdetial_photos_gridview, parent, false);
            itemUserdetialPhotoImage = (ImageView) view.findViewById(R.id.item_userdetial_photo_image);

//            //指定Item的宽高
//            DisplayMetrics dm = new DisplayMetrics();
//            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
//            float density = dm.density;
////            int width = (int) (80 * density);
//            int hight = (int) (84 * density);
//            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, hight));
            view.setTag(itemUserdetialPhotoImage);
        } else {
            view = convertView;
            itemUserdetialPhotoImage = (ImageView) view.getTag();
        }
        String url = getItem(position);
        itemUserdetialPhotoImage.setTag(url);
        if (itemUserdetialPhotoImage.getTag() != null && itemUserdetialPhotoImage.getTag().equals(url)) {
            HttpLoader.getImageLoader().get(url,
                    ImageLoader.getImageListener(itemUserdetialPhotoImage, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        return view;
    }
}
