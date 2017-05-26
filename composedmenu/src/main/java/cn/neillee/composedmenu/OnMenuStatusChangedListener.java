package cn.neillee.composedmenu;

import android.view.ViewGroup;

/**
 * 作者：Neil on 2017/5/25 21:36.
 * 邮箱：cn.neillee@gmail.com
 */

public interface OnMenuStatusChangedListener {
    void onStatusChanged(ViewGroup parent, ComposedMenu.Status from, ComposedMenu.Status to);
}
