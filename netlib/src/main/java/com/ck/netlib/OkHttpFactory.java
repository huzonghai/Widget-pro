package com.ck.netlib;


import com.ck.netlib.utils.App;
import com.ck.netlib.utils.Config;


import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

enum OkHttpFactory {
    INSTANCE;

    private final OkHttpClient okHttpClient;

    private static final int TIMEOUT_READ = 50;
    private static final int TIMEOUT_WRITE = 50;
    private static final int TIMEOUT_CONNECTION = 50;

    OkHttpFactory() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        File cacheFile = new File(App.getCacheUri(), "NetCache");
        Cache cache = new Cache(cacheFile, 50 * 1024 * 1024);

        AuthInterceptor interceptor = new AuthInterceptor();

        try {
            KeyStore trustStore;
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactoryImp ssl = new SSLSocketFactoryImp(KeyStore.getInstance(KeyStore.getDefaultType()));
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return HttpsURLConnection.getDefaultHostnameVerifier().verify("api-data.coolook.org", session);
                }
            };
            builder.sslSocketFactory(ssl.getSSLContext().getSocketFactory(), ssl.getTrustManager()).hostnameVerifier
                    (hostnameVerifier);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (HttpsFactroy.getSSLSocketFactory(App.context)!=null) {
//            builder.sslSocketFactory(Objects.requireNonNull(HttpsFactroy.getSSLSocketFactory(App.context)));
//            builder.hostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//        }

        builder.cache(cache).addNetworkInterceptor(interceptor);

        builder.retryOnConnectionFailure(true)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS);

        okHttpClient = builder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
