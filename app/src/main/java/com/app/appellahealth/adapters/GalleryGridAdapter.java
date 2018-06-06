package com.app.appellahealth.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.appellahealth.R;
import com.app.appellahealth.commons.BindingUtils;
import com.app.appellahealth.commons.LetterDrawable;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.RowGalleryItemBinding;
import com.app.appellahealth.models.Photo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehuljoisar on 15/05/18.
 */

public class GalleryGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Photo> data = new ArrayList<>();
    private OnActionListener onActionListener;

    public interface OnActionListener {
        public void onNavigateToDetails(int position);
    }

    public List<Photo> getData() {
        return data;
    }

    public void setData(List<Photo> data) {
        this.data = data;
    }

    public OnActionListener getOnActionListener() {
        return onActionListener;
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_gallery_item, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Photo dataItem = data.get(position);
        ItemHolder itemHolder = (ItemHolder) holder;
        if(position<2){
            itemHolder.getBinding().vHeader.setVisibility(View.GONE);
        }
        else{
            itemHolder.getBinding().vHeader.setVisibility(View.GONE);
        }
        BindingUtils.loadImageWithPlaceholder(itemHolder.getBinding().ivThumbnail, dataItem.getImage(), LetterDrawable.getRect(Utils.px100dp, Utils.px100dp, Color.WHITE, false, true, null, "", Color.GRAY));
        itemHolder.getBinding().ivThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onActionListener!=null){
                    onActionListener.onNavigateToDetails(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (data != null) ? data.size() : 0;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        private RowGalleryItemBinding binding;

        public ItemHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        public RowGalleryItemBinding getBinding() {
            return binding;
        }
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
