package com.yingdi.baseproject.base.volley;


import com.yingdi.baseproject.base.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonPostFormRequest extends Request<JSONObject> {

    private Response.Listener<JSONObject> mListener;
    private Map<String, String> mRequestParams;

    public JsonPostFormRequest(int method, String url, Map<String, String> requestParams, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mRequestParams = requestParams;
        mListener = listener;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//            if (!StringUtils.isEmpty(response.headers.get("Set-Cookie"))) {
//                CookieInfo cookieInfo = new CookieInfo();
//                cookieInfo.cookie = response.headers.get("Set-Cookie");
//                LocalSaveHelper.cacheObject(cookieInfo);
//            }
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mRequestParams;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

}
