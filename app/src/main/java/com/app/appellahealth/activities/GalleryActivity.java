package com.app.appellahealth.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;

import com.app.appellahealth.R;
import com.app.appellahealth.adapters.GalleryGridAdapter;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.ActivityGalleryBinding;
import com.app.appellahealth.models.Article;
import com.app.appellahealth.models.Photo;
import com.google.gson.reflect.TypeToken;
import static com.app.appellahealth.commons.Constants.ARG_ARTICLE;
import static com.app.appellahealth.commons.Constants.ARG_PHOTO;

/**
 * Created by mehuljoisar on 15/05/18.
 */

public class GalleryActivity extends BaseActivity {

    private static final String TAG = GalleryActivity.class.getSimpleName();
    private ActivityGalleryBinding mBinding;
    private GalleryGridAdapter mAdapter;
    private Article mArticle;

    @Override
    public void init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
    }

    @Override
    public void setupToolbar() {
        setSupportActionBar(mBinding.toolbar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        initReceivedInfo();
        initAdapter();
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
            mArticle = (Article) Utils.deserialize(arguments.getString(ARG_ARTICLE),new TypeToken<Article>(){}.getType());
        }
    }

    private void initAdapter(){
        mAdapter = new GalleryGridAdapter();
        mAdapter.setOnActionListener(new GalleryGridAdapter.OnActionListener() {
            @Override
            public void onNavigateToDetails(int position) {
                navigateToDetails(mAdapter.getData().get(position));
            }
        });
        int spanCount = 2;
        int spacing = getResources().getDimensionPixelSize(R.dimen.itemOffset);
        mBinding.rvData.setLayoutManager(new GridLayoutManager(GalleryActivity.this, spanCount));
        GalleryGridAdapter.GridSpacingItemDecoration itemDecoration = new GalleryGridAdapter.GridSpacingItemDecoration(spanCount,spacing,true);
        mBinding.rvData.addItemDecoration(itemDecoration);
        mBinding.rvData.setHasFixedSize(true);
        mBinding.rvData.setAdapter(mAdapter);
    }

    private void updateUi(){
        if(mArticle!=null && mArticle.getPhotos()!=null && mArticle.getPhotos().size()>0){
            mAdapter.setData(mArticle.getPhotos());
            mAdapter.notifyDataSetChanged();
        }
    }


    private void navigateToDetails(Photo photo){
        Intent intent = new Intent(GalleryActivity.this, GalleryDetailsActivity.class);
        Bundle arguments = new Bundle();
        arguments.putString(ARG_PHOTO,Utils.serialize(photo));
        intent.putExtras(arguments);
        startActivity(intent);
    }

}
