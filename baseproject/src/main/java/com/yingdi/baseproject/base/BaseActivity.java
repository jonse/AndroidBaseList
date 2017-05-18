package com.yingdi.baseproject.base;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yingdi.baseproject.R;


/**
 * 基础Activity
 */
public class BaseActivity extends FragmentActivity{
    private Context mContext;
    protected RelativeLayout mActionBarView;
    protected TextView mBack;
    private TextView mTitle;
    private TextView mRightText;
    private TextView mCancel;
    protected InputMethodManager inputMethodManager;
    private long exitTime = 0;
    private ImageView mRightImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBarColor();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getActionBar();
        mContext = this;
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            mActionBarView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.action_bar_layout, null);
            mTitle = (TextView) mActionBarView.findViewById(R.id.title);
            mBack = (TextView) mActionBarView.findViewById(R.id.back);
            mRightText = ((TextView) mActionBarView.findViewById(R.id.right_text));
            mRightImage = ((ImageView) mActionBarView.findViewById(R.id.right_image));
            mCancel = ((TextView) mActionBarView.findViewById(R.id.cancel));
            mBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            lp.height = getResources().getDimensionPixelOffset(R.dimen.element_margin_44dp);
            actionBar.setCustomView(mActionBarView, lp);
            actionBar.hide();
        }
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void setStatusBarColor() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#89d663"));
        }
//清除全屏模式下的 Flag
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mChildView, new OnApplyWindowInsetsListener() {
                @Override
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    return insets;
                }
            });
            ViewCompat.setFitsSystemWindows(mChildView, true);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * set title from string
     */
    public void setTitle(int stringId) {
        if (mTitle != null) {
            mTitle.setText(getString(stringId));
        }

    }

    public void setTitle(String string) {
        if (mTitle != null) {
            mTitle.setText(string);
        }

    }

    /**
     * set backtext from string
     */
    public void setBackText(int stringId) {
        if (mBack != null) {
//            mBack.setText(getString(stringId));
        }

    }

    public void setBackText(String string) {
        if (mBack != null) {
//            mBack.setText(string);
        }

    }

    public void setRightImage(int imageId) {
        mRightImage.setImageResource(imageId);
    }

    public void setRightImageClickListener(View.OnClickListener listener) {
        if (mRightImage != null) {
            mRightImage.setOnClickListener(listener);
        }
    }

    public void setRightText(String string) {
        if (mRightText != null) {
            mRightText.setText(string);
        }
    }

    public void setRightTextVisible(int id) {
        if (mRightText != null) {
            mRightText.setVisibility(id);
        }
    }

    public void setRightTextColor(int colorid) {
        if (mRightText != null) {
            mRightText.setTextColor(getResources().getColor(colorid));
        }
    }

    public void setRightTextColor(String color) {
        if (mRightText != null) {
            mRightText.setTextColor(Color.parseColor(color));
        }
    }

    public void setRightText(int stringid) {
        if (mRightText != null) {
            mRightText.setText(getString(stringid));
        }
    }

    public void setRightTextClickListener(View.OnClickListener listener) {
        if (mRightText != null) {
            mRightText.setOnClickListener(listener);
        }
    }

    public void setCancelVisible() {
        if (mCancel != null) {
            mCancel.setVisibility(View.VISIBLE);
            mBack.setVisibility(View.GONE);
        }
    }

    public void setBackVisible() {
        if (mCancel != null) {
            mBack.setVisibility(View.VISIBLE);
            mCancel.setVisibility(View.GONE);
        }
    }

    public void setCancelListener(View.OnClickListener listener) {
        if (mCancel != null) {
            mCancel.setOnClickListener(listener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mTitle != null) {
            int actionBtnWidth = getResources().getDimensionPixelSize(R.dimen.action_bar_height) * menu.size();
            mTitle.setPadding(actionBtnWidth, 0, 0, 0);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
//        if (this instanceof MainActivity) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                Toastor.getInstance().showShortToast(getString(R.string.exit_app));
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
//            }
//        } else {
//            super.onBackPressed();
//        }
        super.onBackPressed();
    }

    protected void showSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            if (getCurrentFocus() != null)
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }
}
