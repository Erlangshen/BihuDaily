package com.lk.bihu.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.lk.bihu.R;

/**
 * Created by liu kun on 2017/10/23 0023.
 */

public class SubscriberHandler extends Handler {
    public static final int SEND_MESSAGE = 11;
    public static final int CANCEL_MESSAGE = 22;
    private SubscriberHandlerListener listener;
    private Context context;
    private boolean cancelable;
    private ProgressDialog pd;

    public SubscriberHandler(SubscriberHandlerListener listener, Context context, boolean cancelable) {
        this.listener = listener;
        this.context = context;
        this.cancelable = cancelable;

    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SEND_MESSAGE:
                if (pd == null) {
                    pd = new ProgressDialog(context);
                    pd.setMessage(context.getString(R.string.request_data_waiting));
                    pd.setCancelable(cancelable);
                    if (cancelable)
                        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                listener.cancelRequest();
                            }
                        });
                    if (!pd.isShowing())
                        pd.show();
                }
                break;
            case CANCEL_MESSAGE:
                if (pd != null) {
                    pd.dismiss();
                    pd = null;
                }
                break;
        }
    }
}
