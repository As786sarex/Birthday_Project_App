package com.wildcardenter.myfab.for_jahan.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class StyledTextView extends android.support.v7.widget.AppCompatTextView {


    public StyledTextView(Context context) {
        super(context);
        setFont();

    }

    public StyledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public StyledTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    public void setFont() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/DancingScript-Regular.otf");
        setTypeface(tf);
    }

}
