package com.ck.newssdk.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class Download {
    private static Bitmap bitmap;

    public static Bitmap downloadToBitmap(String urlPath) {
        InputStream is = null;
        if (urlPath == null) {
            CKLogger.d(" null u");
            return null;
        }
        HttpURLConnection conn = null;
        File file = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(60000);
            conn.setDoInput(true);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
                return bitmap;
            } else {
                CKLogger.d(String.format(Locale.getDefault(), "code:%d info:%s", responseCode, conn.getHeaderFields().toString()));
            }
        } catch (Exception e) {
            CKLogger.d("download error" + e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            IOTil.close(conn);
        }
        return null;
    }


    public static File downloadFile(String urlPath, String downloadDir, String picname) {
        if (urlPath == null) {
            CKLogger.d(" null u");
            return null;
        }
        HttpURLConnection conn = null;
        File file = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(60000);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                int fileLength = conn.getContentLength();
                CKLogger.d("file length---->" + fileLength);
                file = new File(downloadDir, picname);
                if (file.exists()) {
                    if (file.length() == fileLength) {
                        CKLogger.d("ready exist file : " + file.getName());
                        return file;
                    } else {
                        boolean delete = file.delete();
                        CKLogger.d(String.format("ready exist file error delete %s", delete));
                    }
                }
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
                OutputStream out = new FileOutputStream(file);
                int size = 0;
                int len = 0;
                byte[] buf = new byte[1024];
                while ((size = bin.read(buf)) != -1) {
                    len += size;
                    out.write(buf, 0, size);
                }
                bin.close();
                out.close();
                if (file.length() == fileLength) {
                    CKLogger.d("finish download file : " + file.getName());
                    return file;
                } else {
                    if (file.exists()) {
                        boolean delete = file.delete();
                        CKLogger.d(String.format("file length error delete %s", delete));
                    }
                }
            } else {
                CKLogger.d(String.format(Locale.getDefault(), "code:%d info:%s", responseCode, conn.getHeaderFields().toString()));
            }
        } catch (Exception e) {
            CKLogger.d("download error" + e);
        } finally {
            try {
                if (conn != null) {
                    conn.getInputStream().close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            IOTil.close(conn);
        }
        return null;
    }
}
