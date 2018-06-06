package com.app.appellahealth.adapters;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.appellahealth.R;
import com.app.appellahealth.commons.BindingUtils;
import com.app.appellahealth.commons.LetterDrawable;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.RowArticleHeaderDetailviewBinding;
import com.app.appellahealth.databinding.RowArticleHeaderListingviewBinding;
import com.app.appellahealth.databinding.RowArticleItemBinding;
import com.app.appellahealth.models.Article;

import java.util.ArrayList;
import java.util.List;


public class ItemListArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ItemListArticleAdapter.class.getSimpleName();

    private List<Article> data = new ArrayList<>();
    private OnActionListener onActionListener;
    private boolean isDetailView = false;
    private static final int typeHeaderListingView = 1;
    private static final int typeHeaderDetailView = 2;
    private static final int typeItem = 3;
    private int contentPaneHeight = 0;
    private String moreInfo = "";

    public ItemListArticleAdapter(boolean isDetailView, String moreInfo, int contentPaneHeight) {
        this.isDetailView = isDetailView;
        this.moreInfo = moreInfo;
        this.contentPaneHeight = contentPaneHeight;
    }

    public interface OnActionListener {
        public void onNavigateToDetails(int position);
        public void onNavigateToVideo(int position);
        public void onNavigateToGallery(int position);
    }

    public ItemListArticleAdapter() {
    }

    public void setDetailView(boolean detailView) {
        isDetailView = detailView;
    }

    public void setContentPaneHeight(int contentPaneHeight) {
        this.contentPaneHeight = contentPaneHeight;
    }

    public List<Article> getData() {
        return data;
    }

    public void setData(List<Article> data) {
        this.data = data;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }


    @Override
    public int getItemCount() {
        return (data != null) ? data.size() : 0;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (!isDetailView) {
                return typeHeaderListingView;
            } else {
                return typeHeaderDetailView;
            }
        }
        return typeItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {

        switch (type) {
            case typeHeaderListingView: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_article_header_listingview, parent, false);
                return new HeaderListingViewHolder(v);

            }
            case typeHeaderDetailView: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_article_header_detailview, parent, false);
                return new HeaderDetailViewHolder(v);

            }
            case typeItem: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_article_item, parent, false);
                return new ItemHolder(v);

            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Article dataItem = data.get(position);

        final boolean isFirstIndex = (position == 0) ? true : false;
        final boolean isLastIndex = (position == getItemCount() - 1) ? true : false;
        boolean shouldHideSeparator = false;
        if(
                isFirstIndex
                        &&
                        (
                                dataItem.getCategoryId() == Article.Category.news ||
                                dataItem.getCategoryId() == Article.Category.events ||
                                dataItem.getCategoryId() == Article.Category.photoGallery
                        )

        ){
            shouldHideSeparator = true;
        }
        if(isLastIndex){
                    shouldHideSeparator = true;
        }

        Log.d(TAG, "onBindViewHolder: "+position+" "+shouldHideSeparator);

        switch (holder.getItemViewType()) {
            case typeHeaderListingView:
                HeaderListingViewHolder headerListingViewHolder = (HeaderListingViewHolder) holder;
                if (dataItem.getType() == null || dataItem.getType().equals(Article.Type.image)) {
                    headerListingViewHolder.getBinding().ivPlay.setVisibility(View.GONE);
                    if (dataItem.getImageType().equals(Article.ImageType.half)) {
                        headerListingViewHolder.getBinding().clRoot.getLayoutParams().height = contentPaneHeight / 2;
                        headerListingViewHolder.getBinding().clRoot.requestLayout();
                    } else if (dataItem.getImageType().equals(Article.ImageType.full)) {
                        headerListingViewHolder.getBinding().clRoot.getLayoutParams().height = contentPaneHeight;
                        headerListingViewHolder.getBinding().clRoot.requestLayout();
                    }
                } else if (dataItem.getType() != null && dataItem.getType().equals(Article.Type.video)) {
                    headerListingViewHolder.getBinding().ivPlay.setVisibility(View.VISIBLE);
                    if (dataItem.getImageType().equals(Article.ImageType.half)) {
                        headerListingViewHolder.getBinding().clRoot.getLayoutParams().height = contentPaneHeight / 2;
                        headerListingViewHolder.getBinding().clRoot.requestLayout();
                    } else if (dataItem.getImageType().equals(Article.ImageType.full)) {
                        headerListingViewHolder.getBinding().clRoot.getLayoutParams().height = contentPaneHeight;
                        headerListingViewHolder.getBinding().clRoot.requestLayout();
                    }

                }
                headerListingViewHolder.getBinding().vSeparator.setVisibility(isLastIndex ? View.GONE : View.VISIBLE);

                headerListingViewHolder.getBinding().tvTitle.setText(dataItem.getTitle());
                BindingUtils.loadImageWithPlaceholder(headerListingViewHolder.getBinding().ivThumbnail, dataItem.getThumbnailImage(), LetterDrawable.getRect(Utils.px100dp, Utils.px100dp, Color.WHITE, false, true, null, dataItem.getTitle().substring(0, 1), Color.GRAY));
                headerListingViewHolder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onActionListener.onNavigateToDetails(position);
                    }
                });

                headerListingViewHolder.getBinding().vSeparator.setVisibility(shouldHideSeparator ? View.GONE : View.VISIBLE);
                break;
            case typeHeaderDetailView:
                HeaderDetailViewHolder headerDetailViewHolder = (HeaderDetailViewHolder) holder;
                BindingUtils.loadImageWithPlaceholder(headerDetailViewHolder.getBinding().ivThumbnail, dataItem.getThumbnailImage(), LetterDrawable.getRect(Utils.px100dp, Utils.px100dp, Color.WHITE, false, true, null, dataItem.getTitle().substring(0, 1), Color.GRAY));
                headerDetailViewHolder.getBinding().tvTitle.setText(dataItem.getTitle());
                Utils.setHTMLData(headerDetailViewHolder.getBinding().tvDescription, dataItem.getDescription());
                if (getItemCount() > 1) {
                    headerDetailViewHolder.getBinding().tvInfo.setText(moreInfo);
                    headerDetailViewHolder.getBinding().tvInfo.setVisibility(View.VISIBLE);
                } else {
                    headerDetailViewHolder.getBinding().tvInfo.setVisibility(View.GONE);
                }
                if (dataItem.getType() == null || dataItem.getType().equals(Article.Type.image)) {
                    headerDetailViewHolder.getBinding().ivPlay.setVisibility(View.GONE);
                    headerDetailViewHolder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(dataItem.getPhotos()!=null && dataItem.getPhotos().size()>0){
                                onActionListener.onNavigateToGallery(position);
                            }
                        }
                    });
                } else if (dataItem.getType() != null && dataItem.getType().equals(Article.Type.video)) {
                    headerDetailViewHolder.getBinding().ivPlay.setVisibility(View.VISIBLE);
                    headerDetailViewHolder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onActionListener.onNavigateToVideo(position);
                        }
                    });
                }
                headerDetailViewHolder.getBinding().vSeparator.setVisibility(shouldHideSeparator ? View.GONE : View.VISIBLE);
                break;
            case typeItem:
                ItemHolder itemHolder = (ItemHolder) holder;
                itemHolder.getBinding().tvTitle.setText(dataItem.getTitle());
                itemHolder.getBinding().tvDate.setText(dataItem.getPublishDate() == null ? "" : Utils.DateUtils.getFormattedDate(Utils.DateUtils.DATETIME_FORMAT4, Utils.DateUtils.TIMESTAMPZONE_FORMAT, dataItem.getPublishDate(), Utils.DateUtils.TZ.UTC2LOC));
                BindingUtils.loadImageWithPlaceholder(itemHolder.getBinding().ivThumbnail, dataItem.getThumbnailImage(), LetterDrawable.getRect(Utils.px100dp, Utils.px100dp, Color.WHITE, false, true, null, dataItem.getTitle().substring(0, 1), Color.GRAY));
                itemHolder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onActionListener.onNavigateToDetails(position);
                    }
                });

                if (dataItem.getType() == null || dataItem.getType().equals(Article.Type.image)) {
                    itemHolder.getBinding().ivPlay.setVisibility(View.GONE);
                } else if (dataItem.getType() != null && dataItem.getType().equals(Article.Type.video)) {
                    itemHolder.getBinding().ivPlay.setVisibility(View.VISIBLE);
                }
                itemHolder.getBinding().vSeparator.setVisibility(shouldHideSeparator ? View.GONE : View.VISIBLE);
                break;
        }
    }

    public static class HeaderListingViewHolder extends RecyclerView.ViewHolder {
        private RowArticleHeaderListingviewBinding binding;

        public HeaderListingViewHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        public RowArticleHeaderListingviewBinding getBinding() {
            return binding;
        }
    }

    public static class HeaderDetailViewHolder extends RecyclerView.ViewHolder {
        private RowArticleHeaderDetailviewBinding binding;

        public HeaderDetailViewHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        public RowArticleHeaderDetailviewBinding getBinding() {
            return binding;
        }
    }


    public static class ItemHolder extends RecyclerView.ViewHolder {
        private RowArticleItemBinding binding;

        public ItemHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        public RowArticleItemBinding getBinding() {
            return binding;
        }
    }

}
