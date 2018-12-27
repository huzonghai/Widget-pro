package com.ck.newssdk.ui.article;

import android.widget.ImageView;

import com.ck.newssdk.R;
import com.ck.newssdk.base.BaseMultiItemQuickAdapter;
import com.ck.newssdk.base.BaseViewHolder;
import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.utils.Iml;

import java.util.List;

public class MultipleItemAdapter extends BaseMultiItemQuickAdapter<ArticleListBean, BaseViewHolder> {

    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int BIG_IMG = 3;
    public static final int THREE_IMG = 4;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MultipleItemAdapter(List<ArticleListBean> data) {
        super(data);
        addItemType(TEXT, R.layout.item_text_news);
        addItemType(IMG, R.layout.item_pic_news);
        addItemType(BIG_IMG, R.layout.item_big_pic_news);
        addItemType(THREE_IMG, R.layout.item_three_pics_news);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleListBean item) {
        helper.setText(R.id.tv_title, item.getTitle());
        switch (helper.getItemViewType()) {
            case TEXT:
//                helper.setText(R.id.tv_title, item.getTitle());
//                break;
            case IMG:
//                Iml.displayImage(item.getImgs()[0], (ImageView) helper.getView(R.id.iv_img));
//                break;
            case BIG_IMG:
                Iml.displayImage(item.getImgs()[0], (ImageView) helper.getView(R.id.iv_img));
                break;
            case THREE_IMG:
                Iml.displayImage(item.getImgs()[0], (ImageView) helper.getView(R.id.iv_img1));
                Iml.displayImage(item.getImgs()[1], (ImageView) helper.getView(R.id.iv_img2));
                Iml.displayImage(item.getImgs()[2], (ImageView) helper.getView(R.id.iv_img3));
                break;
            default:
        }

    }

}
