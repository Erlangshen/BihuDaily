package com.lk.bihu.interfaces;

import com.lk.bihu.bean.ThemeMainInfo;

/**
 * 点击菜单栏，回调接口（切换新闻内容）
 */
public interface MenuItemClickListener {
    /**点击"+"回调*/
    void addClick(ThemeMainInfo info);
    /**点击文字回调*/
    void tvClick(ThemeMainInfo info);
}
