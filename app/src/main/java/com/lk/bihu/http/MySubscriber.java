package com.lk.bihu.http;

import android.content.Context;
import android.widget.Toast;

import com.lk.bihu.R;

import rx.Subscriber;

/**
 * Created by liu kun on 2017/10/23 0023.
 */

public class MySubscriber<T> extends Subscriber<T> implements SubscriberHandlerListener {
    private SubscriberListener listener;
    private Context context;
    private SubscriberHandler handler;

    public MySubscriber(SubscriberListener listener,
                        Context context) {
        this.listener = listener;
        this.context = context;
        handler = new SubscriberHandler(this, context, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        showPd();
    }

    @Override
    public void onCompleted() {
        dismissPd();
    }

    @Override
    public void onError(Throwable e) {
        dismissPd();
        Toast.makeText(context, R.string.net_error_text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(T t) {
        listener.onNext(t);
    }

    @Override
    public void cancelRequest() {
        if (!this.isUnsubscribed())
            this.unsubscribe();
    }

    private void showPd() {
        handler.obtainMessage(SubscriberHandler.SEND_MESSAGE).sendToTarget();
    }

    private void dismissPd() {
        handler.obtainMessage(SubscriberHandler.CANCEL_MESSAGE).sendToTarget();
    }
}
