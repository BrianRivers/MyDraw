package com.example.mydraw;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class DrawPad extends View {

	public DrawPad(Context context) {
        this(context, null, 0);
    }

    public DrawPad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
