package com.longge.gateway.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;

import lombok.NonNull;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author roger yang
 * @date 9/16/2019
 */
public class OkHttpUtils {
    private static OkHttpClient okHttpClient;

    public static void setOkHttpClient(OkHttpClient client) {
        okHttpClient = client;
    }
    
    /**
     * GET Method begin---------------------------------
     */
    
    public static <T> T get(@NonNull String url, Class<T> clasz) {
        return get(url, null, null, clasz);
    }

    public static <T> T get(@NonNull String url, Map<String, String> queryParameter, Class<T> clasz) {
        return get(url, null, queryParameter, clasz);
    }

    public static <T> T get(@NonNull String url, Map<String, String> headerParameter, Map<String, String> queryParameter, Class<T> clasz) {
        Request.Builder builder = new Request.Builder();
        if (!isEmptyMap(headerParameter)) {
            builder.headers(Headers.of(headerParameter));
        }
        if (isEmptyMap(queryParameter)) {
            builder.url(url);
        } else {
            boolean hasQuery = false;
            try {
                hasQuery = new URL(url).getQuery().isEmpty();
            } catch (MalformedURLException e) {
                throw new RuntimeException("url is illegal");
            }
            StringBuilder sb = new StringBuilder(url);
            if (!hasQuery) {
                sb.append("?1=1");
            }
            queryParameter.forEach((k, v) -> {
                sb.append("&").append(k).append("=").append(v);
            });
            builder.url(sb.toString());
        }

        try (Response resp = okHttpClient.newCall(builder.build()).execute();) {
            return processResponse(resp, clasz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST Method With JSON begin---------------------------------
     */
    
    public static <T> T postJson(@NonNull String url, Class<T> clasz) {
        return postJson(url, null, null, clasz);
    }

    public static <T> T postJson(@NonNull String url, Map<String, String> headerParameter, Class<T> clasz) {
        return postJson(url, headerParameter, null, clasz);
    }
    
    public static <T> T postJson(@NonNull String url, Map<String, String> headerParameter, Object bodyObj, Class<T> clasz) {
        Request.Builder builder = new Request.Builder().url(url);
        if(!Objects.isNull(bodyObj)) {
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSONObject.toJSONString(bodyObj));
            builder.post(body);
        } else {
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}");
            builder.post(body);
        }
        if (!isEmptyMap(headerParameter)) {
            builder.headers(Headers.of(headerParameter));
        }
        try (Response resp = okHttpClient.newCall(builder.build()).execute();) {
            return processResponse(resp, clasz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * POST Method With Form begin---------------------------------
     */
    
    public static <T> T postForm(@NonNull String url, Class<T> clasz) {
        return postForm(url, null, null, clasz);
    }

    public static <T> T postForm(@NonNull String url, Map<String, String> headerParameter, Class<T> clasz) {
        return postForm(url, headerParameter, null, clasz);
    }
    
    public static <T> T postForm(@NonNull String url, Map<String, String> headerParameter, Map<String, String> parameters, Class<T> clasz) {
        Request.Builder builder = new Request.Builder().url(url);
        if(!Objects.isNull(parameters)) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            parameters.forEach((k, v) -> {
                formBuilder.add(k, v);
            });
            builder.post(formBuilder.build());
        }
        if (!isEmptyMap(headerParameter)) {
            builder.headers(Headers.of(headerParameter));
        }
        try (Response resp = okHttpClient.newCall(builder.build()).execute();) {
            return processResponse(resp, clasz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private static <T> T processResponse(Response resp, Class<T> clasz) throws IOException {
        if (resp.isSuccessful()) {
            if (Objects.equals(Void.class, clasz)) {
                return null;
            }
            String respStr = resp.body().string();
            if(Objects.equals(String.class, clasz)) {
                return (T)respStr;
            }
            return JSONObject.parseObject(respStr, clasz);
        }
        return null;
    }
    
    private static boolean isEmptyMap(Map<? extends Object, ? extends Object> map) {
        return Objects.isNull(map) || map.isEmpty();
    }
}
