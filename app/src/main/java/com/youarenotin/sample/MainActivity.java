package com.youarenotin.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.youarenotin.adhesionindicator.AdhesionIndicator;
import com.youarenotin.adhesionindicator.viewpager.ScrollerViewPager;

public class MainActivity extends AppCompatActivity {

    private Toolbar tb;
    private AdhesionIndicator indicator;
    private ScrollerViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb = (Toolbar) findViewById(R.id.toolBar);
        tb.setTitle("");
        tb.setBackgroundResource(R.color.colorPrimary);
        setSupportActionBar(tb);

        indicator = (AdhesionIndicator) findViewById(R.id.indicator);
        viewpager = (ScrollerViewPager) findViewById(R.id.vp);
        indicator.setViewPager(viewpager);
    }
}
