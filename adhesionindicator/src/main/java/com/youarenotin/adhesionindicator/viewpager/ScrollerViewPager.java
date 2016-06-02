package com.youarenotin.adhesionindicator.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * 作者：lubo on 5/31 0031 14:42
 * 邮箱：lubo_wen@126.com
 */
public class ScrollerViewPager extends ViewPager {
    private static final String TAG = ScrollerViewPager.class.getSimpleName();
    private int mDuration = 1000;

    public ScrollerViewPager(Context context) {
        super(context);
        setScrollSpeed();
    }

    public ScrollerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScrollSpeed();
    }

    public void setScrollSpeed() {
        setScrollSpeed(mDuration);
    }

    public void setScrollSpeed(int duration) {
        setScrollSpeedUsingRefection(duration);
    }

    private void setScrollSpeedUsingRefection(int duration) {
        try {
            Field filed_mScroller = ViewPager.class.getDeclaredField("mScroller");
            filed_mScroller.setAccessible(true);//viewpager.class中该全局变量是private的
            FixedSpeedScroller mScroller = new FixedSpeedScroller(getContext(), new DecelerateInterpolator());
            mScroller.setFixedSpeedScroller(duration);
            filed_mScroller.set(this, mScroller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 要替换viewpager.class中 mScroller
     */
    public static class FixedSpeedScroller extends Scroller {
        private int mDuration = 1000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        public void setFixedSpeedScroller(int duration) {
            this.mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            this.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
