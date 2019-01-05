package com.ck.widget;



import java.io.Closeable;
import java.net.HttpURLConnection;

public class IOTil {
    public static void close(HttpURLConnection httpURLConnection) {
        if (httpURLConnection != null) {
            try {
                httpURLConnection.disconnect();
            } catch (Exception e) {
                ;
            }

        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
               ;
            }

        }
    }

}
