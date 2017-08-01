package com.talentsprint.android.esa.views;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Vasavi Jeeri on 31-03-2016.
 */
public class OpenSansStrongTextView extends TextView {
    public OpenSansStrongTextView(Context context) {
        super(context);
        setFont();
    }

    public OpenSansStrongTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public OpenSansStrongTextView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        setTypeface(font, Typeface.BOLD);
        setLineSpacing(10, 1);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setPadding(getPaddingLeft(), 7, getPaddingRight(), getPaddingBottom());
        }
    }
}