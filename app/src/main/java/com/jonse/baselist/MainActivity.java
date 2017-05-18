package com.jonse.baselist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yingdi.baseproject.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.baseRefreshListActivity)
    Button baseRefreshListActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.baseRefreshListActivity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.baseRefreshListActivity:
                startActivity(new Intent(MainActivity.this, BaseDemoListActivity.class));
                break;
        }
    }
}
