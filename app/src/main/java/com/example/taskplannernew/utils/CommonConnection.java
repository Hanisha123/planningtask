package com.example.taskplannernew.utils;

import android.content.Context;
import android.text.TextUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;

import retrofit2.http.HTTP;

@SuppressWarnings("deprecation")
public class CommonConnection {
    Context context;

    public CommonConnection(Context context) {
        this.context = context;
    }

    public JSONObject GetHttpRequest(String url) {
        HttpClient client = new DefaultHttpClient();

        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:56.0) Gecko/20100101 Firefox/56.0");
        JSONObject jsonObject = null;
        try {
            HttpResponse response = client.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            if (responseCode == 200 && entity != null) {
                String json = EntityUtils.toString(entity);
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        return jsonObject;

    }

    public JSONObject PostHttpRequest(String url,
                                      List<NameValuePair> nameValuePair) {
        HttpClient client = new DefaultHttpClient();
        JSONObject jsonObject = null;
        HttpPost request = new HttpPost(url);
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePair, org.apache.http.protocol.HTTP.UTF_8));
            request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:56.0) Gecko/20100101 Firefox/56.0");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        try {
            HttpResponse response = client.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            if (responseCode == 200 && entity != null) {
                String json = EntityUtils.toString(entity);
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        return jsonObject;

    }

    public JSONObject PostHttpRequest(String url) {
        HttpClient client = new DefaultHttpClient();
        JSONObject jsonObject = null;
        HttpPost request = new HttpPost(url);
//        try {
//            request.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
//            request.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:56.0) Gecko/20100101 Firefox/56.0");
//        } catch (UnsupportedEncodingException e1) {
//            e1.printStackTrace();
//
        try {
            HttpResponse response = client.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            if (responseCode == 200 && entity != null) {
                String json = EntityUtils.toString(entity);
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    HttpURLConnection urlConnection = null;

    public String PostMethodWay(String URL_HOST, Map<String, String> hashMap) {
        String response = "";
        JSONObject jsonObject = null;
        try {
            if (urlConnection != null)
                urlConnection.disconnect();
            String URLPaTH = URL_HOST;
            //Logger.d("URLPaTH", "::" + URLPaTH);
            DefaultHttpClient httpclient = getNewHttpClient();

            HttpPost httpost = new HttpPost(URLPaTH);
            JSONObject holder = new JSONObject();
            for (Map.Entry entry : hashMap.entrySet()) {
                holder.put((String) entry.getKey(), entry.getValue());
            }
           // Logger.d("JSONObject", holder.toString());
            StringEntity se = new StringEntity(holder.toString());
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Accept", org.apache.http.protocol.HTTP.UTF_8);
            httpost.setHeader("Content-type", "application/json");
            httpost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:56.0) Gecko/20100101 Firefox/56.0");

            HttpResponse result = httpclient.execute(httpost);
            response = EntityUtils.toString(result.getEntity());
           // Logger.d("STATUS", "::" + response);
            if (TextUtils.isEmpty(response)) {
                response = "404";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            response = "404";
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            response = "500";
        } catch (EOFException e) {
            e.printStackTrace();
            PostMethodWay( URL_HOST, hashMap);
        } catch (IOException e) {
            e.printStackTrace();
            response = "404";
        } catch (JSONException e) {
            e.printStackTrace();
            response = "404";
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "500";
                }

            }
        }
        return response;
    }

    public DefaultHttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, org.apache.http.protocol.HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }


    }
}
