package com.lk.bihu.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 请求菜单列表返回的实体类
 */
public class AllThemeMainInfo implements Serializable{
    private int limit;
    private String[] subscribed;
    private List<ThemeMainInfo> others;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String[] getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(String[] subscribed) {
        this.subscribed = subscribed;
    }

    public List<ThemeMainInfo> getOthers() {
        return others;
    }

    public void setOthers(List<ThemeMainInfo> others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return "AllThemeMainInfo{" +
                "limit=" + limit +
                ", subscribed=" + Arrays.toString(subscribed) +
                ", others=" + others +
                '}';
    }
}
