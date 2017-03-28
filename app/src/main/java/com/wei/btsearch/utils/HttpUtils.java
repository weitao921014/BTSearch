package com.wei.btsearch.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wei on 17-3-6.
 */
public class HttpUtils {
    /**
     * 返回指定url的字符串，默认为utf-8编码
     *
     * @param urlstr url地址
     * @return 解析到的网页字符，默认为utf-8编码
     * @throws IOException
     */
    public static String getURLString(String urlstr) throws IOException {
        return getURLString(urlstr, "utf-8");
    }

    /**
     * 返回指定url的字符串
     *
     * @param urlstr  url地址
     * @param charset 字符编码的名称
     * @return 解析到的网页字符
     * @throws IOException
     */
    public static String getURLString(String urlstr, String charset) throws IOException {
        byte[] data = new byte[1024 * 1024 * 3];
        int read = 0;

        HttpURLConnection connection = (HttpURLConnection) new URL(urlstr).openConnection();
        connection.setDoInput(true);
        connection.setReadTimeout(3000);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");
        connection.connect();
        InputStream stream = connection.getInputStream();
        int c;
        while ((c = stream.read(data, read, data.length - read)) != -1) {
            read += c;
        }
//        System.out.println(read);
        stream.close();
        return new String(data, 0, read, charset);
    }

    /**
     * The tool is good to use
     * @param url url
     * @return string
     * @throws IOException
     */
    public static String getURLStringByOk(String url) throws IOException {
        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

}
