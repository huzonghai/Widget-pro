package com.ck.newssdk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ck.newssdk.R;
import com.ck.newssdk.base.BaseQuickAdapter;
import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.config.Configuration;
import com.ck.newssdk.ui.article.ArticlePresenter;
import com.ck.newssdk.ui.article.ArticleView;
import com.ck.newssdk.ui.article.MultipleItemAdapter;
import com.ck.newssdk.ui.base.LazyBaseFragment;
import com.ck.newssdk.ui.web.WebActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CkFragment extends LazyBaseFragment implements ArticleView.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private int topicId;
    private ArticlePresenter presenter;
    private SwipeRefreshLayout refresh;
    private RecyclerView recycler;
    private LinearLayout refreshHint;
    private TextView refreshHintTv;
    private MultipleItemAdapter adapter;
    private ProgressBar progressBar;

    private List<ArticleListBean> listBeans = new ArrayList<>();
    private TreeSet<Integer> rank = new TreeSet<>();
    private int max, min;

    private boolean isPrepared;
    private boolean isLoadedOnce;
    private static CkFragment f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topicId = getArguments() != null ? getArguments().getInt("topicId") : 0;
        presenter = new ArticlePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ck_fragment, container, false);
        refresh = v.findViewById(R.id.swipe_refresh);
        recycler = v.findViewById(R.id.ck_recycler_view);
        progressBar = v.findViewById(R.id.progress_bar);
        refreshHint = v.findViewById(R.id.refresh_hint);
        refreshHintTv = v.findViewById(R.id.refreshHint_tv);
        refresh.setColorSchemeResources(R.color.bg);
        isPrepared = true;

        refresh.setOnRefreshListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MultipleItemAdapter(listBeans);
        recycler.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, recycler);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List data = adapter.getData();
                ArticleListBean news = (ArticleListBean) data.get(position);
                jumpToWeb(news);
            }
        });

        initData();
        return v;
    }

    private void jumpToWeb(ArticleListBean news) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("linkurl", news.getLinkurl());
        intent.putExtra("sourceurl", news.getSourceurl());
        intent.putExtra("title", news.getTitle());
        intent.putExtra("articleid", news.getId());
        this.getActivity().startActivity(intent);
    }

    @Override
    public void onRefresh() {
        if (topicId == 114) {
            presenter.getRecommend(this.getActivity(), Configuration.CurrentCountry);
        } else {
            presenter.getArticleList(this.getActivity(), 0, Configuration.CurrentCountry, topicId, max, min);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        recycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (topicId == 114) {
                    presenter.getRecommend(getActivity(), Configuration.CurrentCountry);
                } else {
                    presenter.getArticleList(getActivity(), 1, Configuration.CurrentCountry, topicId, max, min);
                }
            }
        }, 1000);
    }

    @Override
    public void onGetArticle(List<ArticleListBean> articleListBean) {
        progressBar.setVisibility(View.GONE);
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            refreshHint(articleListBean.size() == 0 ? "No content" : articleListBean.size() + "");
            listBeans.addAll(0, articleListBean);
            adapter.notifyItemRangeChanged(0, articleListBean.size());
        } else if (adapter.isLoading()) {
            int size = listBeans.size();
            listBeans.addAll(articleListBean);
            adapter.notifyItemRangeChanged(size, listBeans.size());
            adapter.loadMoreComplete();
        } else {
            listBeans.addAll(articleListBean);
            adapter.notifyDataSetChanged();
            if (!isLoadedOnce) {
                isLoadedOnce = true;
            }
        }
        for (ArticleListBean b : articleListBean) {
            rank.add(b.getId());
        }
        max = rank.last();
        min = rank.first();
    }

    @Override
    public void fail(String msg) {
        Log.e("Ck", "fail: -->> " + msg);
    }

    private void refreshHint(String message) {
        refreshHint.setVisibility(View.VISIBLE);
        refreshHintTv.setText(message);
        refreshHint.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshHint != null) {
                    refreshHint.setVisibility(View.GONE);
                }
            }
        }, 700);
    }

    @Override
    public void initData() {
        //true true false
        if (!isPrepared || !isVisible || isLoadedOnce) {
            return;
        }
        if (topicId == 114) {
            presenter.getRecommend(this.getActivity(), Configuration.CurrentCountry);
        } else {
            presenter.getArticleList(this.getActivity(), 0, Configuration.CurrentCountry, topicId, 0, 0);
        }
    }

    @Override
    protected void lazyLoad() {
        initData();
    }

    public static CkFragment newInstance(int topicId) {
        f = new CkFragment();
        Bundle b = new Bundle();
        b.putInt("topicId", topicId);
        f.setArguments(b);
        return f;
    }
}
