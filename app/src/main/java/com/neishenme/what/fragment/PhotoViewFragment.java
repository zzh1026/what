package com.neishenme.what.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.view.CircularProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/5/11.
 */
public class PhotoViewFragment extends Fragment {
    private String url;
    private CircularProgressBar progressBar;
    private DisplayImageOptions options;
    private View view;
    private PhotoView imageView;

    public PhotoViewFragment() {
    }

    public static PhotoViewFragment newInstence(String urls) {
        PhotoViewFragment photoViewFragment = new PhotoViewFragment();
        photoViewFragment.url = urls;
        return photoViewFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo_view, container, false);
        imageView = (PhotoView) view.findViewById(R.id.photoIm);
        // progressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);

        imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
                getActivity().finish();
            }
        });
        HttpLoader.getImageLoader().get(url,
                ImageLoader.getImageListener(imageView, R.drawable.picture_moren, R.drawable.picture_moren));

        return view;

    }
}
