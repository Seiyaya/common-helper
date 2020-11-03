package xyz.seiyaya.common.helper;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * http相关工具类
 */
@Slf4j
public class HttpHelper {

    private PoolingHttpClientConnectionManager cm = null;

    private static HttpHelper httpUtils;

    static {
        try {
            httpUtils = new HttpHelper();
        } catch (Exception e) {
            log.error("初始化异常",e);
        }
    }

    private HttpHelper() throws Exception {
        init();
    }

    public static HttpHelper getHttpUtils() {
        return httpUtils;
    }

    public String sendPostJson(String url, String params) {
        String resultJson = "";
        CloseableHttpClient client = HttpClients.custom().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost;
        try {
            httpPost = new HttpPost(url);
            httpPost.addHeader("Content-type","application/json");
            httpPost.setEntity(new StringEntity(params,"utf-8"));
            response = client.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                resultJson = EntityUtils.toString(response.getEntity(),"utf-8");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            release(response);
        }
        return resultJson;
    }

    public void init() throws Exception {
        log.info("初始化httpUtils");
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }}, new SecureRandom());
        LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(20);
    }

    public CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        return httpClient;
    }

    public byte[] sendGetByte(String url) {
        log.info("请求方式->get 请求的url->{}", url);
        CloseableHttpClient client = getHttpClient();
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet();
        try {
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();
            httpGet.setURI(uri);
            log.info("real url ->{}",httpGet.getURI().toString());
            response = client.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK || status == HttpStatus.SC_NOT_MODIFIED) {
                return EntityUtils.toByteArray(response.getEntity());
            } else {
                log.error("响应失败，状态码：" + status);
            }
        } catch (Exception e) {
            log.error("发送get请求失败", e);
            throw new RuntimeException(e);
        } finally {
            release(response);
        }
        return null;
    }

    /**
     * 释放连接
     * @param response
     */
    private void release(CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

    public String sendGet(String url) {
        return sendGet(url, null, StandardCharsets.UTF_8.toString(),null);
    }

    public String sendGet(String url, DBParam params) {
        return sendGet(url, params, StandardCharsets.UTF_8.toString(),null);
    }

    public String sendGet(String url, DBParam params,DBParam headers) {
        return sendGet(url, params, StandardCharsets.UTF_8.toString(),headers);
    }

    public String sendGet(String url, DBParam params, String encoding, DBParam headers) {
        log.info("请求方式->get 请求的url->{} 请求的参数->{}", url, params);
        String resultJson = null;
        CloseableHttpClient client = getHttpClient();
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet();
        try {
            if(headers != null && !headers.isEmpty()){
                headers.forEach((key,value)-> httpGet.addHeader(key,value.toString()));
            }
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key).toString());
                }
            }
            URI uri = builder.build();
            httpGet.setURI(uri);
            log.info("real url ->{}",httpGet.getURI().toString());
            response = client.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                resultJson = EntityUtils.toString(response.getEntity(), encoding);
            } else {
                log.error("响应失败，状态码：" + status);
            }
        } catch (Exception e) {
            log.error("发送get请求失败", e);
            throw new RuntimeException(e);
        } finally {
            release(response);
        }
        return resultJson;
    }

    /**
     * 发送post请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public String sendPost(String url, DBParam params,DBParam headers){
        log.info("请求方式->post 请求的url->{} 请求的参数->{} 请求头->{}", url, params,headers);
        String resultJson = "";
        CloseableHttpClient client = getHttpClient();
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);
            List<NameValuePair> urlParams = Lists.newArrayList();
            params.forEach((k,v)->{
                urlParams.add(new BasicNameValuePair(k, v.toString()));
            });
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(urlParams, "utf-8");

            HttpPost finalHttpPost = httpPost;
            headers.forEach((k, v)->{
                finalHttpPost.addHeader(k,v.toString());
            });

            httpPost.setEntity(formEntity);
            response = client.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                resultJson = EntityUtils.toString(response.getEntity(),"utf-8");
            } else {
                log.error("响应失败，状态码：" + status);
            }
        }catch (Exception e){
            log.error("",e);
            throw new RuntimeException(e);
        }finally {
            release(response);
        }
        return resultJson;
    }
}
