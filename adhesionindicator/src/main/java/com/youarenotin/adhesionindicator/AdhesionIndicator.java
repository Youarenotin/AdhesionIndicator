package com.youarenotin.adhesionindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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
    private ArrayList<TextView> tabs;
    private SpringView springView;
    private float headXOffset = 0.6f;
    private float footXOffset = 1 - headXOffset;
    private float acceleration = 0.5f;

    public AdhesionIndicator(Context context) {
        super(context);
    }

    public AdhesionIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        textColor = R.color.default_text_color;
        selected_textColor = R.color.default_text_color_selected;
        indicatorBgColor = R.color.default_indicator_bg;
        textSize = getResources().getDimension(R.dimen.default_text_size);
        radiusMax = getResources().getDimension(R.dimen.default_radius_max);
        radiusMin = getResources().getDimension(R.dimen.default_radius_min);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AdhesionIndicator);
        textColor = a.getResourceId(R.styleable.AdhesionIndicator_textcolor, textColor);
        selected_textColor = a.getResourceId(R.styleable.AdhesionIndicator_selectedtextcolor, selected_textColor);
        textSize = a.getDimension(R.styleable.AdhesionIndicator_textsize, textSize);
        textBgColor = a.getResourceId(R.styleable.AdhesionIndicator_textbg, 0);
        indicatorBgColor = a.getResourceId(R.styleable.AdhesionIndicator_indicatorcolor, indicatorBgColor);
        indicatorBgColors = a.getResourceId(R.styleable.AdhesionIndicator_indicatorcolors, 0);
        radiusMax = a.getDimension(R.styleable.AdhesionIndicator_radiusmax, radiusMax);
        radiusMin = a.getDimension(R.styleable.AdhesionIndicator_radiusmin, radiusMin);
        a.recycle();

        radiusOffset = radiusMax - radiusMin;
    }

    public void setViewPager(ViewPager vp) {
        viewPager = vp;
        initView();
        setUpListener();
    }

    private void setUpListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when the current page is scrolled, either as part
            // of a programmatically initiated smooth scroll or a user initiated touch scroll.
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < tabs.size() - 1) {
                    //radius head
                    float radiusOffsetHead = 0.5f;
                    if (positionOffset < radiusOffsetHead) {
                        springView.getHeadPoint().setRadius(radiusMin);
                    } else {
                        float radiusHead = radiusMin + radiusOffset * ((positionOffset - radiusOffsetHead) / radiusOffsetHead);
                        springView.getHeadPoint().setRadius(radiusHead);
                    }
                    //radius foot
                    float radiusOffsetFoot = 0.5f;
                    if (positionOffset < radiusOffsetFoot) {
                        float radiusFoot = radiusMin + radiusOffset * (1 - positionOffset / radiusOffsetFoot);
                        springView.getFootPoint().setRadius(radiusFoot);
                    } else {
                        springView.getFootPoint().setRadius(radiusMin);
                    }

                    //x head
                    float headX = 1.0f;
                    if (positionOffset < headXOffset) {
                        headX = (float) (Math.atan((positionOffset / headXOffset) * acceleration * 2 - acceleration) + Math.atan(acceleration) / (2 * Math.abs(acceleration)));
                    } else {
                        headX = 1.0f;
                    }
                    springView.getHeadPoint().setX(getTabX(position) + headX / 1.0f * getPositionDistance(position));
                    //x foot
                    float footX = 1.0f;
                    if (positionOffset > footXOffset) {
                        footX = (float) (Math.atan(((positionOffset - footXOffset) / 1 - footXOffset) * 2 * acceleration - acceleration)+Math.atan(acceleration)/2*Math.atan(acceleration));
                    } else {
                        footX = 1.0f;
                    }
                    springView.getFootPoint().setX(getTabX(position)+(1.0f-footX)*getPositionDistance(position));

                    //reset radius
                    if (positionOffset==0){
                        springView.getHeadPoint().setRadius(radiusMin);
                        springView.getFootPoint().setRadius(radiusMax);
                    }

                } else {
                    springView.getFootPoint().setRadius(radiusMin);
                    springView.getHeadPoint().setRadius(radiusMax);
                    springView.getHeadPoint().setX(getTabX(position));
                    springView.getFootPoint().setX(getTabX(position));
                }

                springView.postInvalidate();
            }

            //This method will be invoked when a new page becomes selected. Animation is not
            //necessarily complete.
            @Override
            public void onPageSelected(int position) {
                for (TextView tv : tabs) {
                    tv.setTextColor(textColor);
                }
                tabs.get(position).setTextColor(selected_textColor);
            }

            //Called when the scroll state changes. Useful for discovering when the user
            //begins dragging, when the pager is automatically settling to the current page,
            //or when it is fully stopped/idle.
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private float getPositionDistance(int position) {
        return tabs.get(position + 1).getX() - tabs.get(position).getX();
    }

    private float getTabX(int position) {
        return tabs.get(position).getX() + tabs.get(position).getWidth() / 2;
    }

    private void initView() {
        addCircleView();
        addTabContainerView();
        addTabItem();
    }

    private void addCircleView() {
        springView = new SpringView(getContext());
        springView.setIndicatorColor(getResources().getColor(indicatorBgColor));
        addView(springView);
    }

    private void addTabContainerView() {
        container = new LinearLayout(getContext());
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setGravity(Gravity.CENTER);
        addView(container);
    }

    private void addTabItem() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        tabs = new ArrayList<>();
        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            TextView tv = new TextView(getContext());
            if (viewPager.getAdapter().getPageTitle(i) != null) {
                tv.setText(viewPager.getAdapter().getPageTitle(i));
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            tv.setTextColor(textColor);
            tv.setGravity(Gravity.CENTER);
            if (textBgColor != 0) {
                tv.setBackgroundResource(textBgColor);
            }
            tv.setLayoutParams(params);
            tv.setClickable(true);
            final int finalI = i;
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(finalI, true);
                }
            });
            tabs.add(tv);
            container.addView(tv);
        }
    }
}
