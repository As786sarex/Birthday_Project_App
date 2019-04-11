package com.wildcardenter.myfab.for_jahan.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class DialogStyledTextView extends AppCompatTextView {
    public DialogStyledTextView(Context context) {
        super(context);
        setFont();

    }

    public DialogStyledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public DialogStyledTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    public void setFont() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Salsbury.ttf");
        setTypeface(tf);
    }
}
