package com.example.mydraw;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawPad extends View {
	
	private List<Float> xPointPositions;
	private List<Float> yPointPositions;
//	private List<Float> pointPositions;
	private Paint pictureLine;
	private Path path;
	private DrawType drawType;
	private DrawSize drawSize;
	
	public enum DrawType { LINE, CIRCLE, SQUARE, POINT }
	public enum DrawSize { 
		SMALL(1), MEDIUM(5), LARGE(10);
		
		private int drawSize;
		
		DrawSize(int _drawSize){
			drawSize = _drawSize;
		}
		
		public int getSize(){
			return drawSize;
		}
	}
	
	public DrawPad(Context context) {
        this(context, null, 0);
    }

    public DrawPad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        drawType = DrawType.LINE;
        
        pictureLine = new Paint();
		pictureLine.setColor(Color.WHITE);
		pictureLine.setStyle(Paint.Style.STROKE);
		pictureLine.setStrokeCap(Cap.ROUND);
		pictureLine.setStrokeWidth(DrawSize.SMALL.getSize());
		
		xPointPositions = new ArrayList<Float>();
		yPointPositions = new ArrayList<Float>();
		
		path = new Path();
		
		
    }
    
    public void setDraw(DrawType _drawType){
    	drawType = _drawType;
    }
    
    public void setDrawSize(DrawSize _drawSize){
    	drawSize = _drawSize;
    	pictureLine.setStrokeWidth(drawSize.getSize());
    }

    @Override
	public boolean onTouchEvent(MotionEvent event){
    	
    	//xPosition.add(event.getX());
    	//yPosition.add(event.getY());
    	
    	
    	float tempX = event.getX();
    	float tempY = event.getY();
    	
    	//Adds points to point Positions.  Will be used to drop into drawPoints(floats[], Paint paint).  No for loop needed?
    	
    	
    	switch(drawType){
    	case LINE:
    		readyPath(event, tempX, tempY);
    		break;
    	case CIRCLE:
    		break;
    	case POINT:
    		readyPoint(event, tempX, tempY);
    		break;
    	case SQUARE:
    		break;
    	}
		
		invalidate();
		return true;
	}
    
    public void readyPath(MotionEvent event, float tempX, float tempY){
    	
    	switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	        Log.i("Test Event", "<------------- DOWN ------------- >");
	        path.moveTo(tempX, tempY);
	        break;
	    case MotionEvent.ACTION_MOVE:
	    	Log.i("Test Event", "<------------- MOVE ------------- >");
	    	path.lineTo(tempX, tempY);
	        break;
	    case MotionEvent.ACTION_UP:
	    	Log.i("Test Event", "<------------- UP ------------- >");
	        break;
		}
    }
    
    public void readyPoint(MotionEvent event,  float tempX, float tempY){
    	
    	//Not sure if I should check for duplicates in array prior to adding.  Size vs Processing time.
    	//if(!xPointPositions.contains(tempX))
    		xPointPositions.add(tempX);
    	//if(!yPointPositions.contains(tempY))
    		yPointPositions.add(tempY);
    	
    }
    
    public void clearCanvas(){
    	path.reset();
    	xPointPositions.clear();
    	yPointPositions.clear();
    	invalidate();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	
    		
    		canvas.drawPath(path, pictureLine);
    		
    		for(int count = 0; count < xPointPositions.size(); count++){
    			canvas.drawPoint(xPointPositions.get(count), yPointPositions.get(count), pictureLine);
    		}
    		//Will be used to draw out points.  Need to translate point arraylist into an array?
    		//canvas.drawPoints(pointPositions, pictureLine);
    	
    }

}
