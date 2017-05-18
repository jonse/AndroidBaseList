package com.jonse.baselist.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.yingdi.baseproject.base.volley.JsonPostFormRequest;
import com.yingdi.baseproject.base.volley.Request;
import com.yingdi.baseproject.base.volley.RequestQueue;
import com.yingdi.baseproject.base.volley.Response;
import com.yingdi.baseproject.base.volley.toolbox.ImageLoader;
import com.yingdi.baseproject.base.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * 网络服务
 *
 * @author Guo Bo
 */
public class ATService {

    public static final String TYPE_QQ = "3";
    public static final String TYPE_WECHAT = "2";
    public static final String METHOD_RESET_PWD = "reset_password";

    private static ATService sInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private ImageLoader.ImageCache mImageCache;

    public static boolean is3G = false;

    public static synchronized void init(Context context) {
        sInstance = new ATService(context.getApplicationContext());
    }

    public static synchronized ATService getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("DjService has not been initialized!");
        }
        return sInstance;
    }

    public static synchronized ImageLoader getImageLoader() {
        return getInstance().mImageLoader;
    }

    public <T> void request(Request<T> request) {
        mRequestQueue.add(request);
    }

    private ATService(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageCache = new LruImageCache();
        mImageLoader = new ImageLoader(mRequestQueue, mImageCache);
    }

    /**
     * 获取数据列表
     */
    public void getArticleList(String page, String page_size, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = "http://bxu2442150623.my3w.com/manager/public/index/Article/getArticleList?";
        url = HttpConfig.appendParam(url, "page", page);
        url = HttpConfig.appendParam(url, "page_size", page_size);
        request(new JsonPostFormRequest(Request.Method.GET, url, null, listener, errorListener));
    }


    public static class LruImageCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

        public LruImageCache() {
            super(getDefaultCacheSize());
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
//			if (url.contains("avatar")) {
//				return;
//			}
            put(url, bitmap);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }

        private static int getDefaultCacheSize() {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 4;
            return cacheSize;
        }
    }
}
