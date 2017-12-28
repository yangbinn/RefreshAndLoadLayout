package com.youngbin.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void onLinear(View view) {
        ListActivity.startActivity(this, LayoutType.LINNEAR);
    }

    public void onGrid(View view) {
        ListActivity.startActivity(this, LayoutType.GRID);
    }

    public void onStaggered(View view) {
        ListActivity.startActivity(this, LayoutType.STAGGERED);
    }

}
