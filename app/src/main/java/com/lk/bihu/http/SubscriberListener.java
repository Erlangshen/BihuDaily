package com.lk.bihu.http;

/**
 * Created by liu kun on 2017/10/23 0023.
 */

public interface SubscriberListener<T> {
    void onNext(T t);
}
