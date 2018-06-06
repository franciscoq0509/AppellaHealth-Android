package com.app.appellahealth.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.appellahealth.R;
import com.app.appellahealth.activities.ArticleDetailsActivity;
import com.app.appellahealth.activities.GalleryActivity;
import com.app.appellahealth.activities.PrivacyActivity;
import com.app.appellahealth.adapters.ItemListArticleAdapter;
import com.app.appellahealth.commons.Constants;
import com.app.appellahealth.commons.Prefs;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.FragmentArticleListBinding;
import com.app.appellahealth.models.Article;
import com.app.appellahealth.webservices.api.RestClient;
import com.app.appellahealth.webservices.resp.RespAbstract;
import com.app.appellahealth.webservices.resp.RespArticle;
import com.app.appellahealth.webservices.resp.RespArticleListing;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.appellahealth.commons.Constants.*;

/**
 * Created by mehuljoisar on 03/05/18.
 */

public class ArticleListFragment extends BaseFragment {

    private static final String TAG = ArticleListFragment.class.getSimpleName();

    private FragmentArticleListBinding mBinding;
    private ItemListArticleAdapter mAdapter;

    private int mCategoryId = 0;
    private boolean isDetailView = false;
    private int mArticleId = 0;
    private String mMoreInfo = "";
    private int mContentPaneHeight = 0;

    public static ArticleListFragment newInstance(int categoryId)
    {
        ArticleListFragment fragment = new ArticleListFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(ARG_CATEGORY_ID, categoryId);
        arguments.putBoolean(ARG_IS_DETAIL_VIEW, false);
        fragment.setArguments(arguments);

        return fragment;
    }


    public static ArticleListFragment newInstance(int categoryId,int articleId)
    {
        ArticleListFragment fragment = new ArticleListFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(ARG_CATEGORY_ID, categoryId);
        arguments.putBoolean(ARG_IS_DETAIL_VIEW, true);
        arguments.putInt(ARG_ARTICLE_ID, articleId);
        fragment.setArguments(arguments);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void init() {
        mAdapter = new ItemListArticleAdapter();
        mContentPaneHeight = mApp.getReadablePrefs().getInt(Prefs.KEY_content_pane_height,0);
        mAdapter.setOnActionListener(new ItemListArticleAdapter.OnActionListener() {
            @Override
            public void onNavigateToDetails(int position) {
                Article article = mAdapter.getData().get(position);
                navigateToDetails(article.getNewsId());
            }
            @Override
            public void onNavigateToVideo(int position) {
                Article article = mAdapter.getData().get(position);
                recordDeepView(article.getNewsId());
                Utils.openVideo(getActivity(),article.getVideo());
            }

            @Override
            public void onNavigateToGallery(int position) {
                Article article = mAdapter.getData().get(position);
                if(article.getPhotos()!=null && article.getPhotos().size()>0){
                    recordDeepView(article.getNewsId());
                    navigateToGallery(article);
                }
            }

        });
        mBinding.rvData.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        mBinding.rvData.setHasFixedSize(true);
        mBinding.rvData.setAdapter(mAdapter);
        initReceivedInfo();
        initMoreInfo();
        initAdapter();
        initData(false);
    }

    @Override
    public void setupToolbar() {
    }


    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
        Log.d(TAG, "onResume: ");
        initData(true);
    }

    private void initReceivedInfo(){
        Bundle arguments = getArguments();
        if(arguments != null)
        {
            mCategoryId = arguments.getInt(ARG_CATEGORY_ID);
            isDetailView = arguments.getBoolean(ARG_IS_DETAIL_VIEW);
            if(isDetailView){
                mArticleId = arguments.getInt(ARG_ARTICLE_ID);
            }
        }
    }

    private void initMoreInfo(){
        mMoreInfo = getString(R.string.lbl_more);
        switch (mCategoryId){
            case Article.Category.news:
                mMoreInfo = mMoreInfo +" "+ getString(R.string.section_news);
                break;
            case Article.Category.nursing:
                mMoreInfo = mMoreInfo +" "+ getString(R.string.section_nursing) +" "+ getString(R.string.section_news);
                break;
            case Article.Category.humanResource:
                mMoreInfo = mMoreInfo +" "+ getString(R.string.section_hr) +" "+ getString(R.string.section_news);
                break;
            case Article.Category.informationTechnology:
                mMoreInfo = mMoreInfo +" "+ getString(R.string.section_it) +" "+ getString(R.string.section_news);
                break;
            case Article.Category.events:
                mMoreInfo = mMoreInfo +" "+ getString(R.string.section_events);
                break;
            case Article.Category.photoGallery:
                mMoreInfo = mMoreInfo +" "+ getString(R.string.section_gallery) +" "+ getString(R.string.section_news);
                break;
            case Article.Category.dining:
                mMoreInfo = mMoreInfo +" "+ getString(R.string.section_dining) +" "+ getString(R.string.section_news);
                break;
        }


    }

    private void initAdapter(){
        mAdapter.setDetailView(isDetailView);
        mAdapter.setMoreInfo(mMoreInfo);
        mAdapter.setContentPaneHeight(mContentPaneHeight);
    }

    private void initData(boolean displayLoader){
        if(!isDetailView){
            getArticleList(mCategoryId,displayLoader);
        }
        else{
            getArticleDetails(mArticleId,displayLoader);
        }
    }

    private void getArticleList(int catgoryId,boolean displayLoader) {
        Log.d(TAG, "getArticleList: ");
        if (!Utils.isNetwork(getActivity().getApplicationContext(), true)) {
            return;
        }
        updateLoader(getActivity(), displayLoader);

        RestClient.get(mApp.getAccessToken()).getArticleListing((catgoryId==Article.Category.news)?"":String.valueOf(catgoryId)).enqueue(new Callback<RespArticleListing>() {
            @Override
            public void onResponse(Call<RespArticleListing> call, Response<RespArticleListing> response) {
                if (!isAdded()) {
                    return;
                }
                updateLoader(getActivity(), false);
                if (response.code() == 200) {
                    RespArticleListing resp = response.body();
                    if (!resp.isSuccesful()) {
                        Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
                    } else {
                        Constants.articleList.clear();
                        Constants.articleList.addAll(resp.getArticleList());
                        updateUi(resp.getArticleList());

                    }
                }
                else if(response.code() == 401){
                    Utils.logout(getActivity());
                }
                else {
                    Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
                }
            }

            @Override
            public void onFailure(Call<RespArticleListing> call, Throwable t) {
                updateLoader(getActivity(), false);
                Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
            }
        });

    }

    private void getArticleDetails(int articleId,boolean displayLoader) {
        if (!Utils.isNetwork(getActivity().getApplicationContext(), true)) {
            return;
        }

        updateLoader(getActivity(), displayLoader);

        RestClient.get(mApp.getAccessToken()).getArticleDetails(articleId).enqueue(new Callback<RespArticle>() {
            @Override
            public void onResponse(Call<RespArticle> call, Response<RespArticle> response) {
                if (!isAdded()) {
                    return;
                }
                updateLoader(getActivity(), false);
                if (response.code() == 200) {
                    RespArticle resp = response.body();
                    if (!resp.isSuccesful()) {
                        Utils.showToast(getActivity().getApplicationContext(), resp.getArticle().getMessage());
                    } else {
                        List<Article> articleList = new ArrayList<Article>();
                        articleList.addAll(Constants.getMoreArticlesById(resp.getArticle().getNewsId()));
                        articleList.add(0,resp.getArticle());
                        updateUi(articleList);
                    }
                }
                else if(response.code() == 401){
                    Utils.logout(getActivity());
                }
                else {
                    Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
                }
            }

            @Override
            public void onFailure(Call<RespArticle> call, Throwable t) {
                updateLoader(getActivity(), false);
                Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
            }
        });

    }

    private void updateUi(List<Article> articleList){
        mAdapter.setMoreInfo(mMoreInfo);
        mAdapter.setData(articleList);
        mAdapter.notifyDataSetChanged();
    }

    private void navigateToDetails(int articleId){
        Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_CATEGORY_ID,mCategoryId);
        arguments.putInt(ARG_ARTICLE_ID,articleId);
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void navigateToGallery(Article article){
        Intent intent = new Intent(getActivity(), GalleryActivity.class);
        Bundle arguments = new Bundle();
        arguments.putString(ARG_ARTICLE,Utils.serialize(article));
        intent.putExtras(arguments);
        startActivity(intent);
    }

    private void recordDeepView(int newsId) {

        if (!Utils.isNetwork(getActivity(), true)) {
            return;
        }

        RestClient.get(mApp.getAccessToken()).recordDeepView(
                newsId
        ).enqueue(new Callback<RespAbstract>() {
            @Override
            public void onResponse(Call<RespAbstract> call, Response<RespAbstract> response) {
                updateLoader(getActivity(), false);
                if (response.code() == 200) {
                    RespAbstract resp = response.body();
                    if (!resp.isSuccesful()) {
                        Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
                    }
                }
                else if(response.code() == 401){
                    Utils.logout(getActivity());
                }
                else {
                    Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
                }
            }

            @Override
            public void onFailure(Call<RespAbstract> call, Throwable t) {
                Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
            }
        });
    }

}
