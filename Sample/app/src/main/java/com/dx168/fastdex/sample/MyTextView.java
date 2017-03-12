package com.dx168.fastdex.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by tong on 17/3/12.
 */
public class MyTextView extends TextView {
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setText(R.string.s3);
    }
}
