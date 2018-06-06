package com.app.appellahealth.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import com.app.appellahealth.R;
import com.app.appellahealth.commons.Prefs;
import com.app.appellahealth.databinding.ActivityHomeBinding;
import com.app.appellahealth.fragments.AccountFragment;
import com.app.appellahealth.fragments.ArticleListFragment;
import com.app.appellahealth.fragments.HelpDeskFragment;
import com.app.appellahealth.models.Article;

/**
 * Created by mehuljoisar on 01/05/18.
 */

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private ActivityHomeBinding mBinding;

    private View.OnClickListener mCommonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mBinding.drawerLayout.closePane();
            switch (view.getId()) {
                case R.id.tvNews:
                    replaceFragment(ArticleListFragment.newInstance(Article.Category.news));
                    break;
                case R.id.tvNursing:
                    replaceFragment(ArticleListFragment.newInstance(Article.Category.nursing));
                    break;
                case R.id.tvHR:
                    replaceFragment(ArticleListFragment.newInstance(Article.Category.humanResource));
                    break;
                case R.id.tvIT:
                    replaceFragment(ArticleListFragment.newInstance(Article.Category.informationTechnology));
                    break;
                case R.id.tvEvents:
                    replaceFragment(ArticleListFragment.newInstance(Article.Category.events));
                    break;
                case R.id.tvGallery:
                    replaceFragment(ArticleListFragment.newInstance(Article.Category.photoGallery));
                    break;
                case R.id.tvDining:
                    replaceFragment(ArticleListFragment.newInstance(Article.Category.dining));
                    break;
                case R.id.tvHelpDesk:
                    replaceFragment(new HelpDeskFragment());
                    break;
                case R.id.tvAccount:
                    replaceFragment(new AccountFragment());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(HomeActivity.this, R.layout.activity_home);
    }

    @Override
    public void init() {
        mBinding.llSections.tvNews.setOnClickListener(mCommonClickListener);
        mBinding.llSections.tvNursing.setOnClickListener(mCommonClickListener);
        mBinding.llSections.tvHR.setOnClickListener(mCommonClickListener);
        mBinding.llSections.tvIT.setOnClickListener(mCommonClickListener);
        mBinding.llSections.tvEvents.setOnClickListener(mCommonClickListener);
        mBinding.llSections.tvGallery.setOnClickListener(mCommonClickListener);
        mBinding.llSections.tvDining.setOnClickListener(mCommonClickListener);
        mBinding.llSections.tvHelpDesk.setOnClickListener(mCommonClickListener);
        mBinding.llSections.tvAccount.setOnClickListener(mCommonClickListener);
        mBinding.ivDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBinding.drawerLayout.isOpen()){
                    mBinding.drawerLayout.closePane();
                }
                else{
                    mBinding.drawerLayout.openPane();
                }
            }
        });
        mBinding.llSections.tvNews.performClick();
    }

    @Override
    public void setupToolbar() {

        setSupportActionBar(mBinding.toolbar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mBinding.drawerLayout.setSliderFadeColor(Color.TRANSPARENT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        transaction.commit();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        storeContentPaneHeight();
    }

    private void storeContentPaneHeight(){
        ViewTreeObserver vto = mBinding.flContent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBinding.flContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = mBinding.flContent.getMeasuredHeight();
                Prefs prefs = new Prefs(getApplicationContext());
                prefs.editPrefs();
                prefs.putInt(Prefs.KEY_content_pane_height,height);
                prefs.commitPrefs();
            }
        });
    }
}
