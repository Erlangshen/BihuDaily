package com.lk.bihu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import static android.R.attr.scaleWidth;
import static android.R.attr.width;

/**
 * author Geek_Soledad.
 */
public class URLImageParser implements Html.ImageGetter {
    TextView mTextView;
    Context c;
    int width;

    public URLImageParser(TextView t, Context c) {
        this.c = c;
        this.mTextView = t;
        width = c.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();
        ImageLoader.getInstance().loadImage(source, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                int imageWidth = 0;
                if (loadedImage != null) {
                    imageWidth = loadedImage.getWidth();
                    if (imageWidth >= 50) {//小于50被视作头像，不必放大
                        // 计算缩放比例
                        float scaleWidth = ((float) (URLImageParser.this.width - dip2px(20))) / loadedImage.getWidth();
                        // 取得想要缩放的matrix参数
                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth, scaleWidth);
                        loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);
                    }
                    urlDrawable.bitmap = loadedImage;
                    urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                    mTextView.invalidate();
                    mTextView.setText(mTextView.getText()); // 解决图文重叠
                }
            }
        });
        return urlDrawable;
    }

    /**
     * dp转成px
     */
    public int dip2px(float dipValue) {
        float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
