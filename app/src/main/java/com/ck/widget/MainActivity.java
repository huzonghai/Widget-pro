package com.ck.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.ck.netlib.ApiFactory;
import com.ck.netlib.beans.CategoryBean;
import com.ck.newssdk.Ck;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mB_loc, mB_test;
    private WebView mWebView;
    private static double lat = 39.90785913;
    private static double lon = 116.51006393;
    public static final String TAG = "Host_widget";
    private WebSettings mWebSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mB_loc = findViewById(R.id.bt_loc);
        mB_test = findViewById(R.id.bt_test);
        mWebView = findViewById(R.id.webview);
        mB_loc.setOnClickListener(this);
        mB_test.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra("url");
            if (!TextUtils.isEmpty(url)) {
                Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
                mWebSettings = mWebView.getSettings();
                mWebView.loadUrl(url);
                mWebSettings.setJavaScriptEnabled(true);
                mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
                mWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                Log.i(TAG, url);

            }

        }
//        getGPSLocation(this);


    }


    /**
     * Address[addressLines=[0:"中国北京市朝阳区十里堡京通快速路"],feature=京通快速路,admin=北京市,
     * sub-admin=null,locality=北京市,thoroughfare=京通快速路,postalCode=null,
     * countryCode=CN,countryName=中国,
     * hasLatitude=true,
     * latitude=39.907851799999996,hasLongitude=true,longitude=116.506665,
     * phone=null,url=null,extras=null]
     *
     * **/

    /**
     * Address[addressLines=[0:"Jing Tong Kuai Su Lu, ShiLiPu, Chaoyang Qu, Beijing Shi, China"],feature=Jing Tong Kuai Su Lu,admin=Beijing Shi,
     * sub-admin=null,locality=Beijing,thoroughfare=Jing Tong Kuai Su Lu,postalCode=null,
     * countryCode=CN,countryName=China,
     * hasLatitude=true,
     * latitude=39.907851799999996,hasLongitude=true,longitude=116.506665,
     * phone=null,url=null,extras=null]
     **/

// Address[addressLines=[0:"Blue Star Memorial Hwy, Norton, MA 02766, USA"],feature=Blue Star Memorial Highway,admin=Massachusetts,
// sub-admin=Bristol County,locality=Norton,thoroughfare=Blue Star Memorial Highway,postalCode=02766,
// countryCode=US,countryName=United States,hasLatitude=true,latitude=41.9872151,hasLongitude=true,longitude=-71.1655509,phone=null,url=null,extras=null]
//    Address[addressLines=[0:"Blue Star Memorial Hwy, Norton, MA 02766美国"],feature=Blue Star Memorial Highway,admin=Massachusetts,sub-admin=Bristol County,locality=Norton,thoroughfare=Blue Star Memorial Highway,postalCode=02766,countryCode=US,countryName=美国,hasLatitude=true,latitude=41.9872151,hasLongitude=true,longitude=-71.1655509,phone=null,url=null,extras=null]
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_loc:
                Ck.init(MainActivity.this, "us");
                Ck.open(MainActivity.this);

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
////                        String result = LocationUtils.getCountryName(lat,lon);
//                        Address address = LocationUtils.getAddress(41.985304, -71.166816);
//                        System.out.println(address.toString());
//                    }
//                }).start();
                break;
            case R.id.bt_test:
                ApiFactory.INSTANCE.getApiService().findTopicsByCode("ru")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CategoryBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(CategoryBean s) {
                                Log.i("CK", "onNext: -->> " + s);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("CK", "onError: -->> " + e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            default:
        }
    }

    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @SuppressLint("MissingPermission")
    public static void getGPSLocation(Context context) {
        double latitude = 0.0;
        double longitude = 0.0;

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //判断GPS是否启动
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //通过GPS获取位置
            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {
                //位置改变的一个监听
                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        //位置改变
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        //GPS状态变化时触发
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        //GPS开启时触发
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        //GPS禁用时触发
                    }
                };

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);

                //通过网络获取位置
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
            lat = latitude;
            lon = longitude;
            //最后打印出经度和纬度
            Log.e("TAG", "Lat:" + latitude + ";Lon=" + longitude);
        }
    }
}
