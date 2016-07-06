package com.lk.bihu.http;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.lk.bihu.interfaces.AsyncTaskCallBack;


public class RequestAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private String url;
	private String msg="";
	private AsyncTaskCallBack callBack;
	private ProgressDialog dialog;
	
	public RequestAsyncTask(Context context, String url, AsyncTaskCallBack callBack) {
		super();
		this.context = context;
		this.url = url;
		this.callBack = callBack;
	}
	public RequestAsyncTask(Context context, String url, String msg,
			AsyncTaskCallBack callBack) {
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
	protected String doInBackground(Void... arg0) {
		return HttpClientUtil.getJson(url);
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (dialog!=null&&dialog.isShowing()) {
			dialog.dismiss();
		}
		callBack.post(result);
	}
}
