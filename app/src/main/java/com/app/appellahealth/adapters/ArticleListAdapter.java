package com.app.appellahealth.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.appellahealth.commons.Constants;
import com.app.appellahealth.fragments.ArticleListFragment;

/**
 * Created by mehuljoisar on 04/05/18.
 */

public class ArticleListAdapter extends FragmentStatePagerAdapter {

    private int mCategoryId,mArticleId,mTotalArticles;

    public ArticleListAdapter(FragmentManager fm) {
        super(fm);
    }

    public ArticleListAdapter(FragmentManager fm,int categoryId,int articleId,int totalArticles) {
        super(fm);
        mCategoryId = categoryId;
        mArticleId = articleId;
        mTotalArticles = totalArticles;
    }


    @Override
    public Fragment getItem(int position) {
        return ArticleListFragment.newInstance(mCategoryId,Constants.getArticleIdByIndex(position));
    }

    @Override
    public int getCount() {
        return mTotalArticles;
    }
}
