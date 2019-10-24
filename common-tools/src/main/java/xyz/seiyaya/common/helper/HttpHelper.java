package xyz.seiyaya.common.helper;

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
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Lists;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http相关工具类
 */
@Slf4j
public class HttpHelper {
    private static final String ENCODING = "UTF-8";

    private PoolingHttpClientConnectionManager cm = null;

    private static HttpHelper httpUtils = new HttpHelper();

    private HttpHelper() {
        init();
    }

    public static HttpHelper getHttpUtils() {
        return httpUtils;
    }

    public void init() {
        log.info("初始化httpUtils");
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
        }

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
            httpGet.releaseConnection();
            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("",e);
                }
            }
        }
        return null;
    }

    public String sendGet(String url) {
        return sendGet(url, null, ENCODING);
    }

    public String sendGet(String url, DBParam params) {
        return sendGet(url, params, ENCODING);
    }

    public String sendGet(String url, DBParam params, String encoding) {
        log.info("请求方式->get 请求的url->{} 请求的参数->{}", url, params);
        String resultJson = null;
        CloseableHttpClient client = getHttpClient();
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet();
        try {
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
            httpGet.releaseConnection();
            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("",e);
                }
            }
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
            formEntity.setContentType("Content-Type:application/json");

            HttpPost finalHttpPost = httpPost;
            headers.forEach((k, v)->{
                finalHttpPost.setHeader(k,v.toString());
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
            if(httpPost != null){
                httpPost.releaseConnection();
            }
            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("",e);
                }
            }
        }
        return resultJson;
    }
}
