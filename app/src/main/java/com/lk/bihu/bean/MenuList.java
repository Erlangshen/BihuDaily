package com.lk.bihu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻菜单
 */

public class MenuList implements Serializable{
    private List<ThemeMainInfo> others;

    public List<ThemeMainInfo> getOthers() {
        return others;
    }

    public void setOthers(List<ThemeMainInfo> others) {
        this.others = others;
    }
}
