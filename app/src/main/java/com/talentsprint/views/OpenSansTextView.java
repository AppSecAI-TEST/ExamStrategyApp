package com.talentsprint.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Vasavi Jeeri on 31-03-2016.
 */
public class OpenSansTextView extends TextView {
    public OpenSansTextView(Context context) {
        super(context);
        setFont();
    }

    public OpenSansTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public OpenSansTextView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
        setLineSpacing(10, 1);
    }
}