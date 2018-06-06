package com.app.appellahealth.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.app.appellahealth.BuildConfig;
import com.app.appellahealth.R;
import com.app.appellahealth.adapters.GalleryGridAdapter;
import com.app.appellahealth.commons.BindingUtils;
import com.app.appellahealth.commons.LetterDrawable;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.ActivityGalleryBinding;
import com.app.appellahealth.databinding.ActivityGalleryDetailsBinding;
import com.app.appellahealth.models.Article;
import com.app.appellahealth.models.Photo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.app.appellahealth.commons.Constants.ARG_ARTICLE;
import static com.app.appellahealth.commons.Constants.ARG_PHOTO;

/**
 * Created by mehuljoisar on 15/05/18.
 */

public class GalleryDetailsActivity extends BaseActivity {

    private static final String TAG = GalleryDetailsActivity.class.getSimpleName();
    private ActivityGalleryDetailsBinding mBinding;
    private Photo mPhoto;

    @Override
    public void init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_details);
        mBinding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPhoto!=null && mPhoto.getImage()!=null && mPhoto.getImage().length()>0){
                    saveAndShare();
                }
            }
        });
    }

    @Override
    public void setupToolbar() {
        setSupportActionBar(mBinding.toolbar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initReceivedInfo();
        updateUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    private void initReceivedInfo(){
        Bundle arguments = getIntent().getExtras();
        if(arguments != null)
        {
            mPhoto = (Photo) Utils.deserialize(arguments.getString(ARG_PHOTO),new TypeToken<Photo>(){}.getType());
        }
    }


    private void updateUi(){
        if(mPhoto!=null && mPhoto.getImage()!=null && mPhoto.getImage().length()>0){
            BindingUtils.loadImageWithPlaceholder(mBinding.ivImage, mPhoto.getImage(), LetterDrawable.getRect(Utils.px100dp, Utils.px100dp, Color.WHITE, false, true, null, "", Color.GRAY));
        }
    }

    private void saveAndShare(){
        Glide
                .with(this)
                .load(mPhoto.getImage())
                .asBitmap()
                .toBytes(Bitmap.CompressFormat.JPEG, 80)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .atMost()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) // read it from cache
                .skipMemoryCache(true) // don't save in memory, needed only once
                .into(new SimpleTarget<byte[]>() {
                    @Override public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> ignore) {
                        new SaveAsFileTask().execute(resource);
                    }
                    @Override public void onLoadFailed(Exception ex, Drawable ignore) {
                    }
                })
        ;
    }

    public class SaveAsFileTask extends AsyncTask<byte[], Void, File> {
        @Override protected File doInBackground(byte[]... params) {
            try {
                File target = new File(getFilename());
                OutputStream out = new FileOutputStream(target);
                out.write((byte[])params[0]);
                return target;
            } catch(IOException ex) {
                return null;
            }
        }
        @Override protected void onPostExecute(@Nullable File result) {
            Uri uri = FileProvider.getUriForFile(GalleryDetailsActivity.this, BuildConfig.APPLICATION_ID, result);
            share(uri);
        }
    }


    public String getFilename() {
        File mediaStorageDir = new File(getFilesDir()
                + "/shared");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        String uriString = (mediaStorageDir.getAbsolutePath() + "/" + mImageName);
        return uriString;

    }

    private void share(Uri uri){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, getString(R.string.title_share_image)));
    }

}
