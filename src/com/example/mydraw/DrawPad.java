package com.example.mydraw;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawPad extends View {
	
	private List<Float> xPosition;
	private List<Float> yPosition;
	private Paint pictureLine;
	private Path path;
	
	public DrawPad(Context context) {
        this(context, null, 0);
    }

    public DrawPad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        pictureLine = new Paint();
		pictureLine.setColor(Color.WHITE);
		pictureLine.setStyle(Paint.Style.STROKE);
		
		xPosition = new ArrayList<Float>();
		yPosition = new ArrayList<Float>();
		
		path = new Path();
    }

    @Override
	public boolean onTouchEvent(MotionEvent event){
    	
    	//xPosition.add(event.getX());
    	//yPosition.add(event.getY());
    	
    	float tempX = event.getX();
    	float tempY = event.getY();
    	
		switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	        Log.i("Test Event", "<------------- DOWN ------------- >");
	        path.moveTo(tempX, tempY);
	        break;
	    case MotionEvent.ACTION_MOVE:
	    	Log.i("Test Event", "<------------- MOVE ------------- >");
	    	//Log.i("Test Event", "<------------- " + xPosition + " , " + yPosition + " ------------- >");
	    	path.lineTo(tempX, tempY);
	        break;
	    case MotionEvent.ACTION_UP:
	    	Log.i("Test Event", "<------------- UP ------------- >");
	    	//Log.i("Test Event", "<------------- " + xPosition + " , " + yPosition + " ------------- >");
	    	///path.reset();
	        break;
		}
		
		invalidate();
		return true;
	}
    
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	
    	//for (int count = 0; count < xPosition.size(); count++){
    		//canvas.drawPoint(xPosition.get(count), yPosition.get(count), pictureLine);
    		canvas.drawPath(path, pictureLine);
    	//}
    	
    }

}
