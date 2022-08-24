package io.pugg.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zhouq
 * @date 2018/02/27
 */
@Component
@Slf4j
public class HelperUtil {

    private static HttpClient httpClient;

    static {
        //HttpClient4.5版本后的参数设置
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        //客户端和服务器建立连接的timeout
        requestConfigBuilder.setConnectTimeout(30000);
        //从连接池获取连接的timeout
        requestConfigBuilder.setConnectionRequestTimeout(30000);
        //连接建立后，request没有回应的timeout。
        requestConfigBuilder.setSocketTimeout(30000);

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
        //连接建立后，request没有回应的timeout
        clientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(50);
        httpClient = clientBuilder.setConnectionManager(cm).build();
    }

    /**
     * check the param whether is null or ''
     *
     * @param params
     * @return boolean
     */
    public static Boolean isEmptyOrNull(Object... params) {
        if (params != null) {
            for (Object val : params) {
                if ("".equals(val) || val == null) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static Boolean isNotEmptyAndNull(Object... params) {
        return !isEmptyOrNull(params);
    }


    /**
     * merge byte[] head and byte[] tail ->byte[head+tail] rs
     *
     * @param head
     * @param tail
     * @return byte[]
     */
    public static byte[] byteMerrage(byte[] head, byte[] tail) {
        byte[] temp = new byte[head.length + tail.length];
        System.arraycopy(head, 0, temp, 0, head.length);
        System.arraycopy(tail, 0, temp, head.length, tail.length);
        return temp;
    }


    /**
     * get current method name
     *
     * @return
     */
    public static String currentMethod() {
        return new Exception("").getStackTrace()[1].getMethodName();
    }

    public static String httpClientPost(String url, String reqBodyStr, Map<String, Object> headerMap) {

        String responseStr = "";

        StringEntity stringEntity = new StringEntity(reqBodyStr, Charset.forName("UTF-8"));
        stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);
        //设置请求头
        for (Map.Entry<String, Object> entry :
                headerMap.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue().toString());
        }

        CloseableHttpResponse response = null;
        try {
            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            responseStr = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            log.error("{} error...", HelperUtil.currentMethod(), e);
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            return responseStr;
        } else {
            log.error("send requestbody:{} to {},response {}:{}", reqBodyStr, url, response.getStatusLine().getStatusCode(), responseStr);

        }
        return "";
    }


    public static String httpClientGet(String uri, Map<String, Object> paramMap, Map<String, Object> headerMap) {

        String responseStr = "";

        CloseableHttpResponse response = null;
        URIBuilder uriBuilder = null;
        try {
            //拼完整的请求url
            uriBuilder = new URIBuilder(uri);
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> entry :
                    paramMap.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            uriBuilder.setParameters(params);

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            //设置请求头
            for (Map.Entry<String, Object> entry :
                    headerMap.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue().toString());
            }
            response = (CloseableHttpResponse) httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            responseStr = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            log.error("{} error...", HelperUtil.currentMethod(), e);
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            return responseStr;
        } else {
            log.error("send to {},response {}:{}", uriBuilder, response.getStatusLine().getStatusCode(), responseStr);
        }
        return "";
    }


}
