package com.zspgame.candycrush;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_empty);

        ARouter.getInstance()
                .build(App.SHORT_VIDEO_PATH)
                .navigation();
        overridePendingTransition(0,0);
        finish();
    }
}
