package com.geejoe.edgeslidingback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by JoeLee on 2017/6/3 0003 09:02.
 */

public class EdgeSlidingBackActivity extends AppCompatActivity implements EdgeSlidingBackLayout.OnSlidingBackListener{

    private boolean enableSlidingBack = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (enableSlidingBack) {
            EdgeSlidingBackLayout edgeSlidingBackLayout = new EdgeSlidingBackLayout(this);
            edgeSlidingBackLayout.bindActivity(this);
        }
    }

    protected void setEnableSlidingBack(boolean enableSlidingBack) {
        this.enableSlidingBack = enableSlidingBack;
    }

    @Override
    public void onSlidingBack() {

    }

}
