package com.app.appellahealth.commons;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by mehuljoisar on 29/12/16.
 */

public class BindingUtils {

    private static final String TAG = BindingUtils.class.getSimpleName();
    private static final String bigImg = "https://unsplash.it/1024/1024";


    public static void loadImageWithPlaceholder(ImageView view, String image_url, Drawable placeholder) {
        Glide.with(view.getContext()).load(image_url).fitCenter().into(view);
    }

    public static void loadCircularImageWithPlaceholder(ImageView view, String image_url, Drawable placeholder) {
        Glide.with(view.getContext()).load(image_url).bitmapTransform(new CropCircleTransformation(view.getContext())).placeholder(placeholder).into(view);
    }

    public static void loadCircularImage(ImageView view, File file) {
        Glide.with(view.getContext()).load(file).bitmapTransform(new CropCircleTransformation(view.getContext())).into(view);
    }

    public static void loadImage(ImageView view, int resource_id) {
        Glide.with(view.getContext()).load(resource_id).into(view);
    }


}
