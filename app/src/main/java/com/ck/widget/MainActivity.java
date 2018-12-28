package com.ck.widget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ck.netlib.ApiFactory;
import com.ck.netlib.beans.CategoryBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {

        ApiFactory.INSTANCE.getApiService().findTopicsByCode("ru")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CategoryBean s) {
                        Log.i("CK", "onNext: -->> "+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("CK", "onError: -->> "+e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
