package com.youarenotin.sample;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.common.collect.Lists;
import com.youarenotin.adhesionindicator.AdhesionIndicator;
import com.youarenotin.adhesionindicator.viewpager.ScrollerViewPager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements GuideFragment.OnFragmentInteractionListener {

    private Toolbar tb;
    private AdhesionIndicator indicator;
    private ScrollerViewPager viewpager;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb = (Toolbar) findViewById(R.id.toolBar);
        tb.setTitle("粘连式指示器");
        tb.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb);

        indicator = (AdhesionIndicator) findViewById(R.id.indicator);
        viewpager = (ScrollerViewPager) findViewById(R.id.vp);
        viewpager.setScrollSpeed(500);
        fragments = new ArrayList<Fragment>();
        for (int i = 0 ; i <getTitles().size() ; i++){
            GuideFragment fragment = GuideFragment.newInstance("", "");
            Bundle bundle = new Bundle();
            bundle.putInt("data", getFragmentBg().get(i));
            bundle.putString("title",getTitles().get(i));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        Adapter adapter = new Adapter(getSupportFragmentManager(),fragments);
        viewpager.setAdapter(adapter);
        indicator.setViewPager(viewpager);
    }

    private List<String> getTitles(){
        return Lists.newArrayList("推荐","娱乐","体育","游戏");
    }
    private List<Integer> getFragmentBg(){
        return  Lists.newArrayList(R.drawable.bg2, R.drawable.bg1, R.drawable.bg3, R.drawable.bg4);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
