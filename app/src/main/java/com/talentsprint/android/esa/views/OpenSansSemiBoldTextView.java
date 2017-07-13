package com.talentsprint.android.esa.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Vasavi Jeeri on 31-03-2016.
 */
public class OpenSansSemiBoldTextView extends TextView {
    public OpenSansSemiBoldTextView(Context context) {
        super(context);
        setFont();
    }

    public OpenSansSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public OpenSansSemiBoldTextView(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/OpenSans-SemiBold.ttf");
        setTypeface(font, Typeface.BOLD);
        setLineSpacing(10, 1);
    }
}