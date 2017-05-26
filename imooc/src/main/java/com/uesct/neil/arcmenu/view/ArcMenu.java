package com.uesct.neil.arcmenu.view;

/**
 * 说明：自定义ViewGroup：
 * 1、自定义属性
 * a、attr.xml
 * b、在布局文件中使用
 * c、在自定义控件中进行读取
 * 2、onMeasure
 * 3、onLayout
 * 4、设置主按钮的旋转动画
 * 为menuItem设置平移动画与旋转动画
 *
 * @author Neil
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.uesct.neil.arcmenu.R;

public class ArcMenu extends ViewGroup implements
        android.view.View.OnClickListener {

    private static final int LEFT_TOP = 0;
    private static final int LEFT_BOTTOM = 1;
    private static final int RIGHT_TOP = 2;
    private static final int RIGHT_BOTTOM = 3;

    /**
     * 菜单所处的位置
     */
    private Position mPosition = Position.LEFT_TOP;
    /**
     * 菜单的半径
     */
    private int mRadius = 100;
    /**
     * 菜单的状态（打开、闭合）
     */
    private Status mCurrentStatus = Status.STATUS_CLOSE;
    /**
     * 中心菜单的按钮
     */
    private View mCenterButton;

    public IOnArcMenuItemClickListener getmIOnMenuItemClickListener() {
        return mIOnMenuItemClickListener;
    }

    public void setmOnMenuItemClickListener(
            IOnArcMenuItemClickListener mIOnMenuItemClickListener) {
        this.mIOnMenuItemClickListener = mIOnMenuItemClickListener;
    }

    private IOnArcMenuItemClickListener mIOnMenuItemClickListener;

    public enum Status {
        STATUS_CLOSE, STATUS_OPEN
    }

    public enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            // 测量child
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutCenterButton();

            int count = getChildCount();//
            for (int i = 1; i < count; i++) {
                int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2)
                        * (i - 1)));
                int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2)
                        * (i - 1)));
                View view = getChildAt(i);
                view.setVisibility(View.GONE);
                int cHight = view.getMeasuredHeight();
                int cWidth = view.getMeasuredWidth();
                // 在右上，右下
                if (mPosition == Position.RIGHT_BOTTOM
                        || mPosition == Position.RIGHT_TOP) {
                    cl = getMeasuredWidth() - cWidth - cl;
                }
                // 在左下，右下
                if (mPosition == Position.LEFT_BOTTOM
                        || mPosition == Position.RIGHT_BOTTOM) {
                    ct = getMeasuredHeight() - cHight - ct;
                }
                view.layout(cl, ct, cl + cWidth, ct + cHight);
            }
        }
    }

    /**
     * 定位主菜单按钮
     */
    private void layoutCenterButton() {

        mCenterButton = getChildAt(0);
        mCenterButton.setOnClickListener(this);
        int l = 0;
        int t = 0;
        int width = mCenterButton.getMeasuredWidth();
        int height = mCenterButton.getMeasuredHeight();
        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        mCenterButton.layout(l, t, l + height, t + width);
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                100, getResources().getDisplayMetrics());
        // 获取自定义属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ArcMenu, defStyleAttr, 0);
        int pos = a.getInt(R.styleable.ArcMenu_position, 3);

        switch (pos) {
            case LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                        getResources().getDisplayMetrics()));
        a.recycle();
    }

    @Override
    public void onClick(View v) {
        mCenterButton = findViewById(R.id.id_button);
        if (mCenterButton == null) {
            mCenterButton = getChildAt(0);
        }
        rotateCenterButton(v, 0f, 360f, 300);
        toogleMenu(150);
    }

    /**
     * 菜单的打开与闭合
     *
     * @param duration
     */
    private void toogleMenu(int duration) {
        int count = getChildCount();
        for (int i = 1; i < count; i++) {
            final View view = getChildAt(i);
            view.setVisibility(View.VISIBLE);
            // end 00
            // start
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2)
                    * (i - 1)));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2)
                    * (i - 1)));
            int xflag = 1;
            int yflag = 1;
            if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.LEFT_TOP) {//	|| mPosition == Position.RIGHT_BOTTOM
                xflag = -1;
            }
            if (mPosition == Position.RIGHT_TOP || mPosition == Position.LEFT_TOP) {//mPosition == Position.LEFT_TOP||
                yflag = -1;
            }
            AnimationSet set = new AnimationSet(true);
            Animation tranAnim = null;

            if (mCurrentStatus == Status.STATUS_CLOSE) {// 需要打开
                tranAnim = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
                view.setClickable(true);
                view.setFocusable(true);
            } else {// 需要关闭
                tranAnim = new TranslateAnimation(0, xflag * cl, 0, yflag * ct);
                view.setClickable(false);
                view.setFocusable(false);
            }
            tranAnim.setFillAfter(true);
            tranAnim.setDuration(duration);
            tranAnim.setStartOffset((i * 100) / count);
            tranAnim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.STATUS_CLOSE) {
                        view.setVisibility(View.GONE);
                    }
                }
            });
            Animation rotaAnim = new RotateAnimation(0f, 720f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotaAnim.setFillAfter(true);
            rotaAnim.setDuration(duration);
            set.addAnimation(rotaAnim);
            set.addAnimation(tranAnim);
            view.startAnimation(set);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("会发觉舒服撒卡");
                    if (mIOnMenuItemClickListener != null) {
                        mIOnMenuItemClickListener.onClick(view, pos);
                    }
                    menuItemAnim(pos);
                    changeMenuStatus();
                }
            });
        }
        changeMenuStatus();
    }

    /**
     * 添加menuItem的点击动画
     *
     * @param pos
     */
    private void menuItemAnim(int pos) {
        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (i == pos) {
                view.startAnimation(animBigScale(300));
            } else {
                view.startAnimation(animSmallScale(300));
            }
            view.setFocusable(false);
            view.setClickable(false);
            view.setVisibility(View.GONE);
        }
    }

    private Animation animBigScale(int i) {
        AnimationSet set = new AnimationSet(true);
        Animation anim = new ScaleAnimation(1, 4f, 1, 4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        Animation animA = new AlphaAnimation(1f, 0f);
        anim.setDuration(i);
        animA.setDuration(i);
        set.addAnimation(anim);
        set.addAnimation(animA);
        return set;
    }

    private Animation animSmallScale(int i) {
        AnimationSet set = new AnimationSet(true);
        Animation anim = new ScaleAnimation(1, 0f, 1, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        Animation animA = new AlphaAnimation(1f, 0f);
        anim.setDuration(i);
        animA.setDuration(i);
        set.addAnimation(anim);
        set.addAnimation(animA);
        return set;
    }

    /**
     * 切换菜单状态
     */
    private void changeMenuStatus() {
        Log.e("TAG", "changeMenuStatus");
        if (mCurrentStatus == Status.STATUS_CLOSE) {// 需要打开
            mCurrentStatus = Status.STATUS_OPEN;
        } else {// 需要关闭
            mCurrentStatus = Status.STATUS_CLOSE;
        }
    }

    private void rotateCenterButton(View v, float start, float end, int duration) {
        RotateAnimation anim = new RotateAnimation(start, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }
}
