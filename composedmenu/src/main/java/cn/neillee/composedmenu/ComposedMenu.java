package cn.neillee.composedmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Neil on 2017/5/25 13:16.
 * 邮箱：cn.neillee@gmail.com
 */

public abstract class ComposedMenu extends ViewGroup {

    private static final int LEFT_TOP = 0;
    private static final int LEFT_BOTTOM = 1;
    private static final int RIGHT_TOP = 2;
    private static final int RIGHT_BOTTOM = 3;

    protected static final int DEFAULT_RADIUS = 150;
    protected static final int DEFAULT_POSITION = RIGHT_BOTTOM;

    protected int mRadius;
    protected View mCenterButton;
    protected Position mPosition;
    protected List<MenuItem> mItems;
    protected Status mCurrentStatus = Status.STATUS_CLOSE;
    protected OnMenuItemClickListener mOnMenuItemClickListener;
    protected OnMenuStatusChangedListener mOnMenuStatusChangedListener;

    public ComposedMenu(Context context) {
        this(context, null);
    }

    public ComposedMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComposedMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVariables(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void initVariables(Context context, AttributeSet attrs, int defStyleAttr) {
        mItems = new ArrayList<>();
        int cbResId = getCenterButtonLayoutId();
        mCenterButton = LayoutInflater.from(getContext()).inflate(
                cbResId, this, false);
        this.addView(mCenterButton, 0);
        mCenterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
        });

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ComposedMenu, defStyleAttr, 0);
        int pos = a.getInt(R.styleable.ComposedMenu_position, DEFAULT_POSITION);
        mRadius = (int) a.getDimension(R.styleable.ComposedMenu_radius, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS,
                        getResources().getDisplayMetrics()));
        a.recycle();

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

    protected abstract int getCenterButtonLayoutId();

    protected abstract void init(Context context, AttributeSet attrs, int defStyleAttr);

    protected abstract void toggleMenu();

    protected abstract void menuItemAnim(int position);

    protected abstract void updateItem(int position);

    protected abstract void addItemView(MenuItem item);

    public abstract void setCenterButtonIcon(Drawable icon);

    public abstract void setCenterButtonIcon(@DrawableRes int resId);

    public abstract void addItem(MenuItem item, int position);

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    public void setOnMenuStatusChangedListener(OnMenuStatusChangedListener listener) {
        this.mOnMenuStatusChangedListener = listener;
    }

    /**
     * 切换菜单状态
     */
    protected void changeMenuStatus() {
        Log.e("TAG", "changeMenuStatus");
        if (mCurrentStatus == Status.STATUS_CLOSE) {// 需要打开
            mCurrentStatus = Status.STATUS_OPEN;
            mOnMenuStatusChangedListener.onStatusChanged(
                    this, Status.STATUS_CLOSE, Status.STATUS_OPEN);
        } else {// 需要关闭
            mCurrentStatus = Status.STATUS_CLOSE;
            mOnMenuStatusChangedListener.onStatusChanged(
                    this, Status.STATUS_OPEN, Status.STATUS_CLOSE);
        }
    }

    public enum Status {
        STATUS_CLOSE, STATUS_OPEN
    }

    public enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(ViewGroup parent, View v, int pos);
    }

    public static final class MenuItem {
        public static final int INVALID_POSITION = -1;
        private Object mTag;
        private Drawable mIcon;
        private CharSequence mContentDesc;
        private int mPosition = -1;
        final ComposedMenu mParent;

        public MenuItem(ComposedMenu parent) {
            mParent = parent;
        }

        @Nullable
        public Object getTag() {
            return mTag;
        }

        @NonNull
        public MenuItem setTag(Object tag) {
            mTag = tag;
            return this;
        }

        @Nullable
        public Drawable getIcon() {
            return mIcon;
        }

        @NonNull
        public MenuItem setIcon(Drawable icon) {
            mIcon = icon;
            if (this.mPosition >= 0) {
                this.mParent.updateItem(this.mPosition);
            }
            return this;
        }

        @NonNull
        public MenuItem setIcon(@DrawableRes int resId) {
            Drawable drawable = mParent.getContext()
                    .getResources().getDrawable(resId);
            return this.setIcon(drawable);
        }

        public CharSequence getContentDesc() {
            return mContentDesc;
        }

        public void setContentDesc(CharSequence contentDesc) {
            mContentDesc = contentDesc;
        }

        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int position) {
            mPosition = position;
        }
    }
}
