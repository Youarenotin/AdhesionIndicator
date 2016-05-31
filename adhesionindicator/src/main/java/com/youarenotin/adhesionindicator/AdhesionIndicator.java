package com.youarenotin.adhesionindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 作者：lubo on 5/31 0031 15:14
 * 邮箱：lubo_wen@126.com
 */
public class AdhesionIndicator extends FrameLayout {
    private int textColor;
    private int selected_textColor;
    private int indicatorBgColor;
    private float textSize;
    private float radiusMax;
    private float radiusMin;
    private int textBgColor;
    private int indicatorBgColors;
    private float radiusOffset;
    private ViewPager viewPager;
    private Circle circleView;
    private LinearLayout container;

    public AdhesionIndicator(Context context) {
        super(context);
    }

    public AdhesionIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        textColor =R.color.default_text_color;
        selected_textColor = R.color.default_text_color_selected;
        indicatorBgColor = R.color.default_indicator_bg;
        textSize = getResources().getDimension(R.dimen.default_text_size);
        radiusMax = getResources().getDimension(R.dimen.default_radius_max);
        radiusMin = getResources().getDimension(R.dimen.default_radius_min);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AdhesionIndicator);
        textColor= a.getResourceId(R.styleable.AdhesionIndicator_textcolor, textColor);
        selected_textColor = a.getResourceId(R.styleable.AdhesionIndicator_selectedtextcolor, selected_textColor);
        textSize = a.getDimension(R.styleable.AdhesionIndicator_textsize, textSize);
        textBgColor = a.getResourceId(R.styleable.AdhesionIndicator_textbg, 0);
        indicatorBgColor = a.getResourceId(R.styleable.AdhesionIndicator_indicatorcolor, indicatorBgColor);
        indicatorBgColors = a.getResourceId(R.styleable.AdhesionIndicator_indicatorcolors, 0);
        radiusMax = a.getDimension(R.styleable.AdhesionIndicator_radiusmax, radiusMax);
        radiusMin = a.getDimension(R.styleable.AdhesionIndicator_radiusmin, radiusMin);
        a.recycle();

        radiusOffset = radiusMax-radiusMin;
    }

    public void setViewPager(ViewPager vp){
        viewPager = vp;
        initView();
        setUpListener();
    }

    private void setUpListener() {

    }

    private void initView() {
        addCircleView();
        addTabContainerView();
        addTabItem();
    }

    private void addCircleView() {
        SpringView springView    = new SpringView(getContext());
        springView.setIndicatorColor(getResources().getColor(indicatorBgColor));
        addView(springView);
    }

    private void addTabContainerView() {
        container = new LinearLayout(getContext());
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1.0f));
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setGravity(Gravity.CENTER);
        addView(container);
    }

    private void addTabItem() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        for (int i =0 ; i<viewPager.getAdapter().getCount(); i ++){
            TextView tv = new TextView(getContext());
            if (viewPager.getAdapter().getPageTitle(i)!=null){
                tv.setText(viewPager.getAdapter().getPageTitle(i));
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
            tv.setGravity(Gravity.CENTER);
            
        }
    }
}
