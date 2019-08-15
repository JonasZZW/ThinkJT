package org.noear.think.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.noear.solon.annotation.XNote;

import java.util.Map;

public class HttpUtils {
    private Connection _con;
    public HttpUtils(String url){
        _con = Jsoup.connect(url)
                .ignoreContentType(true)
                .maxBodySize(Integer.MAX_VALUE);
    }

    @XNote("设置请求头")
    public HttpUtils headers(Map<String,Object> headers){
        if (headers != null) {
            headers.forEach((k, v) -> {
                _con.header(k, v.toString());
            });
        }

        return this;
    }

    @XNote("设置提交数据")
    public HttpUtils data(Map<String,Object> data){
        if (data != null) {
            data.forEach((k, v) -> {
                _con.data(k, v.toString());
            });
        }

        return this;
    }

    @XNote("设置请求cookies")
    public HttpUtils cookies(Map<String,Object> cookies){
        if (cookies != null) {
            cookies.forEach((k, v) -> {
                _con.cookie(k, v.toString());
            });
        }

        return this;
    }

    @XNote("发起get请求，返回字符串")
    public String get() throws Exception{
        return _con.method(Connection.Method.GET).execute()
                .body();
    }

    @XNote("发起post请求，返回字符串")
    public String post() throws Exception{
        return _con.method(Connection.Method.POST).execute()
                .body();
    }

    @XNote("发起put请求，返回字符串")
    public String put() throws Exception{
        return _con.method(Connection.Method.PUT).execute()
                .body();
    }


    @XNote("发起get请求，返回字符串或文档")
    public Object get(boolean doc) throws Exception{
        if(doc){
            return _con.get();
        }else{
            return _con.method(Connection.Method.GET).execute()
                    .body();
        }
    }

    @XNote("发起post请求，返回字符串或文档")
    public Object post(boolean doc) throws Exception{
        if(doc){
            return _con.post();
        }else{
            return _con.method(Connection.Method.POST).execute()
                    .body();
        }
    }

    @XNote("发起put请求，返回字符串或文档")
    public Object put(boolean doc) throws Exception{
        if(doc){
            return _con.method(Connection.Method.PUT).execute()
                    .parse();
        }else{
            return _con.method(Connection.Method.PUT).execute()
                    .body();
        }
    }
}
