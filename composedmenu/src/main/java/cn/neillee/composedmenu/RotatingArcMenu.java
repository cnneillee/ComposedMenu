package cn.neillee.composedmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

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
public class RotatingArcMenu extends ComposedMenu {

    public RotatingArcMenu(Context context) {
        this(context, null);
    }

    public RotatingArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatingArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    protected int getCenterButtonLayoutId() {
        return R.layout.rotating_arc_menu_center_button;
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    @Override
    protected void toggleMenu() {
        Animation anim = AnimationUtils.loadAnimation(
                getContext(), R.anim.arc_menu_rotate_center_button);
        anim.setDuration(300);
        anim.setFillAfter(true);
        mCenterButton.startAnimation(anim);

        // 获取子菜单项数目
        int menuItemCount = getChildCount() - 1;
        double gap = Math.PI / 2 / (menuItemCount - 1);
        for (int i = 1; i <= menuItemCount; i++) {
            final View view = getChildAt(i);
            view.setVisibility(View.VISIBLE);
            // end 00
            // start
            int cl = (int) (mRadius * Math.cos(gap * (i - 1)));
            int ct = (int) (mRadius * Math.sin(gap * (i - 1)));
            int xFlag = 1;
            int yFlag = 1;
            if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.LEFT_TOP) {
                xFlag = -1;
            }
            if (mPosition == Position.RIGHT_TOP || mPosition == Position.LEFT_TOP) {
                yFlag = -1;
            }
            AnimationSet set = null;
            TranslateAnimation tranAnim = null;
            if (mCurrentStatus == Status.STATUS_CLOSE) {// 需要打开
                set = (AnimationSet) AnimationUtils.loadAnimation(
                        getContext(), R.anim.arc_menu_open_anim_set);
                tranAnim = new TranslateAnimation(xFlag * cl,
                        0, yFlag * ct, 0);
                view.setClickable(true);
                view.setFocusable(true);
            } else {// 需要关闭
                set = (AnimationSet) AnimationUtils.loadAnimation(
                        getContext(), R.anim.arc_menu_close_anim_set);
                tranAnim = new TranslateAnimation(0,
                        xFlag * cl, 0, yFlag * ct);
                view.setClickable(false);
                view.setFocusable(false);
            }
            tranAnim.setFillAfter(true);
            tranAnim.setDuration(300);
            tranAnim.setStartOffset((i * 100) / menuItemCount);
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
            set.addAnimation(tranAnim);
            view.startAnimation(set);
            final int pos = i - 1;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.onMenuItemClick(
                                RotatingArcMenu.this, view, pos);
                    }
                    menuItemAnim(pos);
                    changeMenuStatus();
                }
            });
        }
        changeMenuStatus();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) return;
        layoutCenterButton();

        // 获取子菜单项数目
        int menuItemCount = getChildCount() - 1;
        double gap = Math.PI / 2 / (menuItemCount - 1);
        for (int i = 1; i <= menuItemCount; i++) {
            int cl = (int) (mRadius * Math.cos(gap * (i - 1)));
            int ct = (int) (mRadius * Math.sin(gap * (i - 1)));
            View itemView = getChildAt(i);
            itemView.setVisibility(View.GONE);

            int cHeight = itemView.getMeasuredHeight();
            int cWidth = itemView.getMeasuredWidth();

            // 在右上，右下
            if (mPosition == Position.RIGHT_BOTTOM
                    || mPosition == Position.RIGHT_TOP) {
                cl = getMeasuredWidth() - cWidth - cl;
            }
            // 在左下，右下
            if (mPosition == Position.LEFT_BOTTOM
                    || mPosition == Position.RIGHT_BOTTOM) {
                ct = getMeasuredHeight() - cHeight - ct;
            }
            itemView.layout(cl, ct, cl + cWidth, ct + cHeight);
        }
    }

    /**
     * 定位主菜单按钮
     */
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

    /**
     * 添加menuItem的点击动画
     *
     * @param pos
     */
    protected void menuItemAnim(int pos) {
        AnimationSet bigScaleSet = (AnimationSet) AnimationUtils
                .loadAnimation(getContext(), R.anim.arc_menu_big_scale_anim);
        AnimationSet smallScaleSet = (AnimationSet) AnimationUtils
                .loadAnimation(getContext(), R.anim.arc_menu_small_scale_anim);
        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.startAnimation(i == pos ? bigScaleSet : smallScaleSet);
            view.setFocusable(false);
            view.setClickable(false);
            view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void updateItem(int position) {
        MenuItem item = mItems.get(position);
        ImageView iv = (ImageView) getChildAt(position + 1);
        if (item == null || iv == null) return;
        iv.setImageDrawable(item.getIcon());
    }

    @Override
    public void setCenterButtonIcon(Drawable icon) {
        if (mCenterButton != null) {
            ImageView imageView = (ImageView) mCenterButton.
                    findViewById(R.id.id_button);
            imageView.setImageDrawable(icon);
        }
    }

    @Override
    public void setCenterButtonIcon(int resId) {
        setCenterButtonIcon(getContext()
                .getResources().getDrawable(resId));
    }

    @Override
    protected void addItemView(MenuItem item) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(item.getIcon());
        this.addView(imageView, item.getPosition() + 1);
    }

    @Override
    public void addItem(MenuItem item, int position) {
        if (item.mParent != this) {
            throw new IllegalArgumentException("MenuItem belongs to a different TabLayout.");
        } else {
            item.setPosition(position);
            this.mItems.add(position, item);
            int count = this.mItems.size();

            for (int i = position + 1; i < count; ++i) {
                this.mItems.get(i).setPosition(i);
            }

            this.addItemView(item);
        }
    }
}
