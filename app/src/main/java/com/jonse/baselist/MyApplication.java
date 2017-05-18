package com.jonse.baselist;

import android.app.Application;

import com.jonse.baselist.http.ATService;
import com.yingdi.baseproject.base.MeasureUtil;
import com.yingdi.baseproject.base.Toastor;


/**
 * Created by WangYingDi on 2017/2/23.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MeasureUtil.init(this);
        ATService.init(this);
        Toastor.init(this);
    }
}
