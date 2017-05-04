package com.lk.bihu.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.lk.bihu.interfaces.AsyncTaskCallBack;
import com.lk.bihu.interfaces.ImageAsyncTaskCallBack;

public class RequestImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {
    private Context context;
    private String url;
    private String msg="";
    private ImageAsyncTaskCallBack callBack;
    private ProgressDialog dialog;

    public RequestImageAsyncTask(Context context, String url, ImageAsyncTaskCallBack callBack) {
        super();
        this.context = context;
        this.url = url;
        this.callBack = callBack;
    }
    public RequestImageAsyncTask(Context context, String url, String msg,
                                 ImageAsyncTaskCallBack callBack) {
        super();
        this.context = context;
        this.url = url;
        this.msg = msg;
        this.callBack = callBack;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!TextUtils.isEmpty(msg)) {
            dialog=new ProgressDialog(context);
            dialog.setMessage(msg);
            dialog.show();
        }
    }
    @Override
    protected Bitmap doInBackground(Void... arg0) {
        return HttpClientUtil.getBitmapFormUrl(url);
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (dialog!=null&&dialog.isShowing()) {
            dialog.dismiss();
        }
        callBack.callBack(result);
    }
}