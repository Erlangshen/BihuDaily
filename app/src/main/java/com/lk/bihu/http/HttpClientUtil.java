package com.lk.bihu.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClientUtil {

    private static HttpClient client;// client:浏览器
    // private static URL mUrl;// url地址
    private static HttpResponse response;// 响应结果

    public HttpClientUtil() {

    }

    public static void HttpUrlConnectionRequest(final String url,
                                                final Handler handler) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL u = new URL(url);
                    conn = (HttpURLConnection) u.openConnection();
                    // 设置请求超时的时间
                    conn.setReadTimeout(6000);
                    // 设置请求方法：GET：请求数据，POST：上传数据
                    conn.setRequestMethod("GET");
                    conn.connect();
                    InputStream in = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    Message msg = new Message();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    public static Bitmap getBitmapFormUrl(String url) {
        Bitmap bitmap = null;
        HttpClient httpClient = new DefaultHttpClient();
        // 设置超时时间
        HttpConnectionParams.setConnectionTimeout(new BasicHttpParams(), 6 * 1000);
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                bitmap = BitmapFactory.decodeStream(entity.getContent());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String getJson(String url) {
        HttpURLConnection conn = null;
        String data="";
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            // 获取默认代理主机ip
            String host = android.net.Proxy.getDefaultHost();
            // 获取端口
            int port = android.net.Proxy.getDefaultPort();
            if (host != null && port != -1) {
                // 封装代理连接主机IP与端口号。
                InetSocketAddress inetAddress = new InetSocketAddress(host,
                        port);
                // 根据URL链接获取代理类型，本链接适用于TYPE.HTTP
                java.net.Proxy.Type proxyType = java.net.Proxy.Type.valueOf(u
                        .getProtocol().toUpperCase());
                java.net.Proxy javaProxy = new java.net.Proxy(proxyType,
                        inetAddress);
                conn = (HttpURLConnection) u.openConnection(javaProxy); // 打开链接
            } else {
                /**** 代理end *****/
                conn = (HttpURLConnection) u.openConnection(); // 打开链接
            }
            conn.setRequestProperty("Content-Type",
                    "text/xml; charset=gb2312");
            conn.setConnectTimeout(8000);
            conn.setRequestMethod("GET");
            conn.connect();
            InputStreamReader isr=new InputStreamReader(conn.getInputStream(),"utf-8");
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            while((line=br.readLine())!=null){
                data+=line;
            }
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn!=null){
                conn.disconnect();
            }
        }
        return data;
    }

    /**
     * @param url
     * @return
     * @方法名称:loadImage
     * @描述: get请求得到InputStream格式的文件
     * @创建人：SunYuChang
     * @创建时间：2014-5-18 下午11:09:40
     * @备注：
     * @返回类型：InputStream
     */
    public static InputStream loadImage(String url) {
        // 生成一个client对象
        client = new DefaultHttpClient();
        // 生成一个指定URL地址的Get请求
        HttpGet requestGet = new HttpGet(url);
        // 生成HttpParams参数
        HttpParams params = new BasicHttpParams();
        // 设置连接超时和socket超时时间
        HttpConnectionParams.setConnectionTimeout(params, 8 * 1000);
        HttpConnectionParams.setSoTimeout(params, 8 * 1000);
        requestGet.setParams(params);
        try {
            // 执行Get请求,得到HttpResponse响应结果
            response = client.execute(requestGet);
            // 响应码为200,网络请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                // 取出响应结果中的HttpEntity实体
                HttpEntity entity = response.getEntity();
                // 将实体利用EntityUtils工具转换成字符串
                InputStream stream = entity.getContent();
                return stream;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
