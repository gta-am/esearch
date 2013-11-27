package service;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * desc:
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-11-27
 * Time: 上午9:17
 */
public class HttpClientServiceImpl {

    //cookie保持策略，这里用默认的。
    private static final BasicCookieStore cookieStore = new BasicCookieStore();
    public static final CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    public static final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
        public String handleResponse(
                final HttpResponse response) throws ClientProtocolException, IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }
    };

    //单例
    static HttpClientServiceImpl instance = null;
    //网关
    public String url;
    //登陆的参数
    public List <NameValuePair> nvps;
    //提交的编码方式
    public String encoded;
    public HttpGet httpget;
    public HttpPost httpost;
    private HttpClientServiceImpl(){}
    private static HttpClientServiceImpl getInstance(String url, List<NameValuePair> params){
        if(instance ==null){
            instance = new HttpClientServiceImpl();
        }
        instance.setUrl(url);
        instance.setNvps(params);
        instance.setHttpget(new HttpGet(url));
        instance.setHttpost(new HttpPost(url));
        return instance;
    }

    public static HttpClientServiceImpl getString(String url){
        instance.url = url;
        return instance;
    }

    public List<NameValuePair> getNvps() {
        return nvps;
    }

    public void setNvps(List<NameValuePair> nvps) {
        this.nvps = nvps;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public HttpGet getHttpget() {
        return httpget;
    }

    public void setHttpget(HttpGet httpget) {
        this.httpget = httpget;
    }

    public HttpPost getHttpost() {
        return httpost;
    }

    public void setHttpost(HttpPost httpost) {
        this.httpost = httpost;
    }

    public String post() {
        String response = "";
        try {
            response = httpclient.execute(httpost,responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public String get() {
        String response = "";
        try {
            response = httpclient.execute(httpget,responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
