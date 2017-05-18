package com.yingdi.baseproject.base;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * WangYingDi
 */
public class MeasureUtil {

    private static WindowManager mWindowManager;
    private static Resources mResources;

    public static void init(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mResources = context.getResources();
    }

    /**
     * View X 坐标
     *
     * @param view
     * @return
     * @author D
     * @time 2014-10-15 下午4:28:57
     */
    public static int getViewPoX(View view) {
        return getViewLocation(view)[0];
    }

    /**
     * View Y 坐标
     *
     * @param view
     * @return
     * @author D
     * @time 2014-10-15 下午4:29:35
     */
    public static int getViewPoY(View view) {
        return getViewLocation(view)[1];
    }

    /**
     * 获取view 坐标
     *
     * @param view
     * @return [0]: x<br/>
     * [1]: y
     * @author D
     * @time 2014-7-23 下午2:17:33
     */
    private static int[] getViewLocation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    /**
     * 获取View 宽度
     *
     * @param view
     * @return
     * @author D
     * @time 2014-10-15 下午4:31:18
     */
    public static int getViewWidth(View view) {
        return getViewMeasure(view)[0];
    }

    /**
     * 获取View 高度
     *
     * @param view
     * @return
     * @author D
     * @time 2014-10-15 下午4:32:01
     */
    public static int getViewHeight(View view) {
        return getViewMeasure(view)[1];
    }

    /**
     * 获取控件宽高
     *
     * @param view
     * @return [0]:contentWidth [1]:contentHeight
     * @author D
     * @time 2014-7-23 下午2:20:32
     */
    private static int[] getViewMeasure(View view) {
        int measure[] = new int[2];
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        measure[0] = view.getMeasuredWidth();
        measure[1] = view.getMeasuredHeight();
        return measure;
    }

    /**
     * 获取屏幕宽度
     *
     * @param mContext
     * @return
     * @author D
     * @time 2014-10-15 下午4:33:56
     */
    public static int getScreenWidth() {
        return getScreenMeasure()[0];
    }

    /**
     * 获取屏幕高度
     *
     * @param mContext
     * @return
     * @author D
     * @time 2014-10-15 下午4:34:47
     */
    public static int getScreenHeight() {
        return getScreenMeasure()[1];
    }

    /**
     * 屏幕宽高
     *
     * @param mContext
     * @return [0]:contentWidth [1]:contentHeight
     * @author D
     * @time 2014-7-23 下午2:44:09
     */
    private static int[] getScreenMeasure() {
        int measure[] = new int[2];

        int w = 0;
        int h = 0;

        Display display = mWindowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        w = metrics.widthPixels;
        h = metrics.heightPixels;

        measure[0] = w;
        measure[1] = h;
        return measure;
    }

    /**
     * 屏幕密度
     *
     * @param mContext
     * @return
     * @author D
     * @time 2014-7-23 下午2:46:41
     */
    public static float getScreenDensity() {
        Display display = mWindowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.density;
    }

    /**
     * 屏幕DPI
     *
     * @param mContext
     * @return
     * @author D
     * @time 2014-7-23 下午2:49:25
     */
    public static int getScreendensityDpi() {
        Display display = mWindowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.densityDpi;
    }

    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     * @author D
     * @time 2014-7-23 下午4:41:28
     */
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = mResources.getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

	/*
     public int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	*/

    /**
     * 获取屏幕尺寸 ， 5.0英寸 、4.7 英寸 - uncheck
     *
     * @param mContext
     * @return
     * @author D
     * @time 2014-7-23 下午4:57:16
     */
    public static double getScreenSizeInInch() {
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }

}
