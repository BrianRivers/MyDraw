package com.example.mydraw;

import com.example.mydraw.DrawPad.DrawType;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class DrawButton extends Button {
	
	private Paint fontPaint;
	private Paint backgroundPaint;

	private String displayText;
	private int currentWidth;
	private int currentHeight;
	
	public DrawButton(Context context) {
		super(context);
		init(null);
	}

	public DrawButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public DrawButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
		
		
	}
	
	private void init(AttributeSet attrs) {
		String centerMessage = "Test";
		int buttonBackgroundColor = Color.DKGRAY;
		int buttonFontColor = Color.WHITE;
		DrawType shape = DrawType.LINE;
		

		Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Precious.ttf");
		Typeface face = Typeface.create(typeface, Typeface.NORMAL);

		if (attrs != null) {

			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.DrawButton);
			centerMessage = a.getString(R.styleable.DrawButton_centerMessage);
			// using the values above in the declaration for the default
			
			backgroundPaint = new Paint();
			backgroundPaint.setColor(a.getColor(R.styleable.DrawButton_buttonBackgroundColor, buttonBackgroundColor));
			backgroundPaint.setStyle(Paint.Style.FILL);
			
			
			
			///shape = a.getInt(R.styleable.DrawButton_buttonShape, defValue)
			fontPaint = new Paint();
			fontPaint.setColor(a.getColor(R.styleable.DrawButton_buttonFontColor, buttonFontColor));
			fontPaint.setStyle(Paint.Style.FILL);
			fontPaint.setAntiAlias(true);
			fontPaint.setTypeface(face);
			fontPaint.setTextAlign(Paint.Align.CENTER);
			// Don't forget this
			
			// compute 50sp size for font -- make sure font appears right size on
			// all devices.
			float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
			float spFontSize = 20;
			float correctSize = spFontSize * scaledDensity;
			
			
			fontPaint.setTextSize(correctSize);
			
			//rect = new RectF(currentWidth, currentHeight, currentWidth +10 , currentHeight + 10);
			displayText = centerMessage;
			a.recycle();
		}

	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		currentWidth = w;
		currentHeight = h;
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPaint(backgroundPaint);
		//canvas.drawPaint(backgroundPaint);
		// x coord for centering
		float xPos = currentWidth / 2f;
		// y coord for centers - only on baseline and above - ignores y part
		float yPos = (currentHeight  - fontPaint.ascent()) / 2f ; 
		//canvas.drawRoundRect(rect, 4, 4, backgroundPaint);
		canvas.drawText(displayText, xPos - 5, yPos, fontPaint);
		
		
		
	}
}
