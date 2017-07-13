package com.talentsprint.android.esa.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Vasavi Jeeri on 31-03-2016.
 */
public class LatoStrongTextView extends TextView {
    public LatoStrongTextView(Context context) {
        super(context);
        setFont();
    }

    public LatoStrongTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public LatoStrongTextView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Lato-Regular.ttf");
        setTypeface(font, Typeface.BOLD);
        setLineSpacing(10, 1);
    }
}