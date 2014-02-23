package com.example.mydraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.R.bool;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class DrawPad extends View {
	
	//Array Lists
	private List<Circle> circles;
	private List<Point> points;
	private List<Paint> paintLine;
	private List<Dpath> path;
	
	//Setting variables
	private DrawType drawType;
	private DrawSize drawSize;
	private int colorPicked;
	private String fileName;
	private int radiusSize;
	
	 
	//For Saving the image
	private int canvasWidth;
	private int canvasHeight;
	private Bitmap saveBitmap;
	private boolean toggleSave;
	
	
	
	class Shape{}
	
	class Dpath extends Shape {
		private Path path;
		private Paint paint;
		
		Dpath(Path _path, Paint _paint){
			path = _path;
			paint = _paint;
		}
		
		public Path getPath(){
			return path;
		}
		
		public Paint getPaint(){
			return paint;
		}
	}
	
	class Point extends Shape{
		private float xPoint;
		private float yPoint;
		private Paint paint;
		
		Point(){};
		
		Point(float _xPoint, float _yPoint,  Paint _paint){
			xPoint = _xPoint;
			yPoint = _yPoint;
			paint = _paint;
		}
		
		public float getXPoint(){
			return xPoint;
		}
		public float getYPoint(){
			return yPoint;
		}	
		public Paint getPaint(){
			return paint;
		}
		
	}
	
	class Circle extends Point{
		
		private float radius;

		
		Circle(float _xCircle, float _yCircle, float _radius, Paint _paint){
			super.xPoint = _xCircle;
			super.yPoint = _yCircle;
			radius = _radius;
			super.paint = _paint;
		}

		public float getRadius(){
			return radius;
		}
		
	}
	
	
	
	
	public enum DrawType { LINE, CIRCLE, SQUARE, POINT }
	public enum DrawSize { 
		SMALL(5), MEDIUM(10), LARGE(20);
		
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
        drawSize = DrawSize.SMALL;
        colorPicked = Color.WHITE;
        toggleSave = false;
        radiusSize = 0;
        

        
        Log.i("Test Event", "<-------------" + Color.WHITE + "------------- >");
        
 
		
		paintLine = new ArrayList<Paint>();
		path = new ArrayList<Dpath>();
		points = new ArrayList<Point>();
		circles = new ArrayList<Circle>();
		
		path.add(new Dpath(new Path(), new Paint(setPaint(colorPicked, Paint.Style.STROKE, Paint.Cap.ROUND,drawSize.getSize()))));
    }
 
    public void setDraw(DrawType _drawType){
    	drawType = _drawType;
    }
    
    public void setDrawSize(DrawSize _drawSize){
    	drawSize = _drawSize;
    	addDrawing();
    	
    }
    
    public void setColor(int _colorPicked){
    	colorPicked = _colorPicked;
    	addDrawing();
    }
    
    public void setRadius(int _radiusSize){
    	radiusSize = _radiusSize;
    }
    public void addDrawing(){

      Paint tempPaint = new Paint();
      
	  tempPaint.setColor(colorPicked);
      tempPaint.setStyle(Paint.Style.STROKE);
      tempPaint.setStrokeCap(Cap.ROUND);
      tempPaint.setStrokeWidth(drawSize.getSize());
      
      Log.i("Test Event", "<-------------" + tempPaint.getStrokeWidth() + "------------- >");
      
      paintLine.add(tempPaint);
      
      path.add(new Dpath(new Path(), tempPaint));
    
      
    }
    
    public Paint setPaint(int color, Paint.Style stroke, Paint.Cap capType, int strokeWidth){
    	Paint tempPaint = new Paint();
    	 tempPaint.setColor(color);
         tempPaint.setStyle(stroke);
         tempPaint.setStrokeCap(capType);
         tempPaint.setStrokeWidth(strokeWidth);
    	return tempPaint;
    }
    
    public void savePicture(boolean _toggleSave, String _fileName){
    	 Log.i("Test Event", "<------------- Entered SavePicture ------------- >");
    	toggleSave = _toggleSave;
    	fileName = _fileName;
    	invalidate();
    	 Log.i("Test Event", "<------------- Exit SavePicture ------------- >");
    	
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	canvasWidth = w;
    	canvasHeight = h;
    	super.onSizeChanged(w, h, oldw, oldh);
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event){
	
    	float tempX = event.getX();
    	float tempY = event.getY();
    	
    	switch(drawType){
    	case LINE:
    		readyPath(event, tempX, tempY);
    		break;
    	case CIRCLE:
    		readyCircle(event, tempX, tempY);
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
    
    public void readyCircle(MotionEvent event, float tempX, float tempY){
    	circles.add(new Circle(tempX, tempY, radiusSize, new Paint(setPaint(colorPicked, Paint.Style.FILL_AND_STROKE, Paint.Cap.ROUND,drawSize.getSize()))));
    	
    }
    
    public void readyPath(MotionEvent event, float tempX, float tempY){
    	
    	Log.i("Test Event", "<-------------" + path.size() + "------------- >");
    	
    	switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	        Log.i("Test Event", "<------------- DOWN ------------- >");
	        path.get(path.size()-1).getPath().moveTo(tempX, tempY);
	        break;
	    case MotionEvent.ACTION_MOVE:
	    	Log.i("Test Event", "<------------- MOVE ------------- >");
	    	path.get(path.size()-1).getPath().lineTo(tempX, tempY);
	        break;
	    case MotionEvent.ACTION_UP:
	    	Log.i("Test Event", "<------------- UP ------------- >");
	        break;
		}
    	
    	path.add(new Dpath(path.get(path.size()-1).getPath(), new Paint(setPaint(colorPicked, Paint.Style.STROKE, Paint.Cap.ROUND,drawSize.getSize()))));
    }
    
    public void readyPoint(MotionEvent event,  float tempX, float tempY){
    		points.add(new Point(tempX, tempY, new Paint(setPaint(colorPicked, Paint.Style.FILL_AND_STROKE, Paint.Cap.ROUND,drawSize.getSize()))));
    }
    
    public void clearCanvas(){
    	path.clear();
    	paintLine.clear();
    	circles.clear();
    	points.clear();
    	
    	addDrawing();
    	invalidate();
    	
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	
    		if(toggleSave){
    			 Log.i("Test Event", "<------------- Entered first IF ------------- >");
    			saveBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
    			canvas.setBitmap(saveBitmap);
    		}
    		for(int count = 0; count < circles.size(); count++){
    			canvas.drawCircle(circles.get(count).getXPoint(), circles.get(count).getYPoint(), circles.get(count).getRadius(), circles.get(count).getPaint());
    		}

    		for (int count = 0; count < path.size(); count++){
    			canvas.drawPath(path.get(count).getPath(), path.get(count).getPaint());
    		}
    		
    		for(int count = 0; count < points.size(); count++){
    			canvas.drawPoint(points.get(count).getXPoint(), points.get(count).getYPoint(), points.get(count).getPaint());
    		}
    		
    		
    		
    		if(toggleSave){
    			try {
    				 Log.i("Test Event", "<------------- Entered secodn If ------------- >");
					saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("/data/data/com.example.mydraw/" + fileName + ".jpg")));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			toggleSave = false;
    			invalidate();
    		}
    		//Will be used to draw out points.  Need to translate point arraylist into an array?
    		//canvas.drawPoints(pointPositions, pictureLine);
    	
    }

}
