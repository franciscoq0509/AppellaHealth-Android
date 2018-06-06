package com.app.appellahealth.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.app.appellahealth.R;
import com.app.appellahealth.adapters.ArticleListAdapter;
import com.app.appellahealth.adapters.ItemListArticleAdapter;
import com.app.appellahealth.commons.Constants;
import com.app.appellahealth.databinding.ActivityArticleDetailsBinding;
import static com.app.appellahealth.commons.Constants.*;

/**
 * Created by mehuljoisar on 03/05/18.
 */

public class ArticleDetailsActivity extends BaseActivity {

    private static final String TAG = ArticleDetailsActivity.class.getSimpleName();

    private ActivityArticleDetailsBinding mBinding;
    private ArticleListAdapter mAdapter;

    private int mCategoryId = 0;
    private int mArticleId = 0;

    private boolean hasNewData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }


    @Override
    public void init() {
        hasNewData = true;
    }

    @Override
    public void setupToolbar() {
        setSupportActionBar(mBinding.toolbar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        hasNewData = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: "+hasNewData);
        if(hasNewData){
            hasNewData = false;
            updateUi();
        }
    }

    private void updateUi(){
        initReceivedInfo();
        initAdapter();
    }

    private void initReceivedInfo(){
        Bundle arguments = getIntent().getExtras();
        if(arguments != null)
        {
            mCategoryId = arguments.getInt(ARG_CATEGORY_ID);
            mArticleId = arguments.getInt(ARG_ARTICLE_ID);
        }
    }

    private void initAdapter(){
        final int totalArticles = Constants.articleList.size();
        mBinding.vpArticles.setOffscreenPageLimit(5);
        mAdapter = new ArticleListAdapter(getSupportFragmentManager(),mCategoryId,mArticleId,totalArticles);
        mBinding.vpArticles.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mBinding.vpArticles.setCurrentItem(Constants.getIndexOfArticleById(mArticleId));
    }

}
