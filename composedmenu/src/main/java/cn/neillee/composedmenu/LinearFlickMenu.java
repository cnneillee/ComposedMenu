package cn.neillee.composedmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * 作者：Neil on 2017/5/25 13:11.
 * 邮箱：cn.neillee@gmail.com
 */

public class LinearFlickMenu extends ComposedMenu {
    public LinearFlickMenu(Context context) {
        this(context, null);
    }

    public LinearFlickMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearFlickMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void updateItem(int position) {
        View view = getChildAt(position);
        MenuItem item = mItems.get(position);
        if (view == null || item == null) return;
        ((ImageView) view).setImageDrawable(item.getIcon());
    }

    @Override
    protected void addItemView(MenuItem item) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(item.getIcon());
        this.addView(imageView, item.getPosition() + 1);
    }

    @Override
    protected void toggleMenu() {
        final int itemCount = getChildCount() - 1;
        int gap = mRadius / itemCount;
        for (int i = 1; i <= itemCount; i++) {
            final View itemView = getChildAt(i);
            itemView.setVisibility(View.VISIBLE);
            int it = gap * i;

            int yFlag = 1;

            if (mPosition == Position.LEFT_BOTTOM
                    || mPosition == Position.RIGHT_BOTTOM)
                yFlag = 1;
            else if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.RIGHT_TOP)
                yFlag = -1;

            AnimationSet set = new AnimationSet(true);
            TranslateAnimation transAnim = null;
            AlphaAnimation alphaAnim = null;
            if (mCurrentStatus == Status.STATUS_OPEN) {// 要关闭
                transAnim = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, it * yFlag);
                alphaAnim = new AlphaAnimation(1, 0);
            } else {// 要打开
                transAnim = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, it * yFlag,
                        Animation.RELATIVE_TO_SELF, 0);
                alphaAnim = new AlphaAnimation(0, 1);
            }
            transAnim.setDuration(250);
            transAnim.setFillAfter(true);
            alphaAnim.setDuration(100);
            alphaAnim.setFillAfter(true);
            transAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.STATUS_CLOSE) {
                        itemView.setVisibility(View.GONE);
                    }
                }
            });
            set.addAnimation(transAnim);
            set.addAnimation(alphaAnim);
            itemView.startAnimation(set);

            final int pos = i;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null)
                        mOnMenuItemClickListener.onMenuItemClick(
                                LinearFlickMenu.this, itemView, pos);
                    menuItemAnim(pos);
                    changeMenuStatus();
                }
            });
        }
        changeMenuStatus();
    }

    @Override
    protected void menuItemAnim(int position) {
        AnimationSet bigScaleSet = (AnimationSet) AnimationUtils
                .loadAnimation(getContext(), R.anim.arc_menu_big_scale_anim);
        AnimationSet smallScaleSet = (AnimationSet) AnimationUtils
                .loadAnimation(getContext(), R.anim.arc_menu_small_scale_anim);
        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.startAnimation(i == position ? bigScaleSet : smallScaleSet);
            view.setFocusable(false);
            view.setClickable(false);
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void setCenterButtonIcon(Drawable icon) {
        ImageView iv = (ImageView) mCenterButton.
                findViewById(R.id.id_button);
        if (iv != null) iv.setImageDrawable(icon);
    }

    @Override
    public void setCenterButtonIcon(int resId) {
        setCenterButtonIcon(getContext()
                .getResources().getDrawable(resId));
    }

    @Override
    public void addItem(MenuItem item, int position) {
        if (item.mParent != this) {
            throw new IllegalArgumentException("MenuItem belongs to a different LinearFlickMenu.");
        } else {
            this.mItems.add(position, item);
            item.setPosition(position);
            int count = this.mItems.size();

            for (int i = position + 1; i < count; ++i) {
                this.mItems.get(i).setPosition(i);
            }
            this.addItemView(item);
        }
    }

    @Override
    protected int getCenterButtonLayoutId() {
        return R.layout.rotating_arc_menu_center_button;
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        // do nothing
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) return;
        layoutCenterButton();

        int itemCount = getChildCount() - 1;
        int gap = mRadius / itemCount;
        for (int i = 1; i <= itemCount; i++) {
            View itemView = this.getChildAt(i);
            itemView.setVisibility(View.GONE);
            int it = gap * i;
            int il = 0;

            int iWidth = itemView.getMeasuredWidth();
            int iHeight = itemView.getMeasuredHeight();

            int deltaRadius = (mCenterButton.getMeasuredWidth() - itemView.getMeasuredWidth()) / 2;
            il = il + deltaRadius;

            if (mPosition == Position.RIGHT_BOTTOM ||
                    mPosition == Position.RIGHT_TOP) {
                il = getMeasuredWidth() - iWidth - il;
            }
            if (mPosition == Position.LEFT_BOTTOM
                    || mPosition == Position.RIGHT_BOTTOM)
                it = getMeasuredHeight() - it - iHeight;
            Log.e("TAG", String.format("[%d]%d,%d,%d,%d", i, il, it, il + iWidth, it + iHeight));

            itemView.layout(il, it, il + iWidth, it + iHeight);
        }
    }

    private void layoutCenterButton() {
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
}
