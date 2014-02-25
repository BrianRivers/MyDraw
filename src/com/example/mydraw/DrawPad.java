package com.example.mydraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawPad extends View implements Serializable {
	
	//Array Lists
	private List<Dpath> path;

	
	private List<Shape> shapes;
	
	//Setting variables
	private DrawType drawType;
	private int drawSize;
	private int colorPicked;
	private String fileName;
	private Paint.Style paintStyle;
	private Paint backgroundPaint;
	
	 
	//For Saving the image
	private int canvasWidth;
	private int canvasHeight;
	private Bitmap saveBitmap;
	private boolean toggleSave;
	
	
	
	class Shape{
		
		public void draw(Canvas canvas){}
	}
	
	
	class Dpath extends Shape {
		private Path path;
		private Paint paint;
		private DrawType shapeType;
		
		Dpath(Path _path, Paint _paint){
			path = _path;
			paint = _paint;
			shapeType = DrawType.LINE;
		}
		
		public Path getPath(){
			return path;
		}
		
		public Paint getPaint(){
			return paint;
		}
		
		public DrawType getShapeType(){
			return shapeType;
		}
		
		public void draw(Canvas canvas){
			canvas.drawPath(path, paint);
		}
		
		public void makeLine(float xPoint, float yPoint){
			path.lineTo(xPoint, yPoint);
		}
		
		public void makeMove(float xPoint, float yPoint){
			path.moveTo(xPoint, yPoint);
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
		private DrawType shapeType;
		
		Circle(float _xCircle, float _yCircle, float _radius, Paint _paint){
			super.xPoint = _xCircle;
			super.yPoint = _yCircle;
			radius = _radius;
			super.paint = _paint;
			shapeType = DrawType.CIRCLE;
			
		}

		public float getRadius(){
			return radius;
		}
		public DrawType getShapeType(){
			return shapeType;
		}
		
		public void draw(Canvas canvas){
			canvas.drawCircle(super.xPoint, super.yPoint, radius, super.paint);
		}
		
	}
	
	class Rectangle extends Shape{
		
		private Rect rect;
		private Paint paint;
		private DrawType shapeType;
		
		Rectangle(Rect _rect, Paint _paint){
			paint = _paint;
			rect = _rect;
			shapeType = DrawType.RECTANGLE;
		}
		
		public Rect getRect(){
			return rect;
		}
		
		public Paint getPaint(){
			return paint;
		}
		
		public DrawType getShapeType(){
			return shapeType;
		}
		
		public void draw(Canvas canvas){
			canvas.drawRect(rect, paint);
		}
	}
	
	
	
	
	public enum DrawType { LINE, CIRCLE, SQUARE, POINT, RECTANGLE }
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
        drawSize = 5;
        colorPicked = Color.WHITE;
        toggleSave = false;
        paintStyle = Paint.Style.STROKE;
 
		path = new ArrayList<Dpath>();
		shapes = new ArrayList<Shape>();
		
		backgroundPaint = new Paint();
		backgroundPaint.setColor(Color.GRAY);
		backgroundPaint.setStyle(Paint.Style.FILL);
		path.add(new Dpath(new Path(), new Paint(setPaint(colorPicked, paintStyle, Paint.Cap.ROUND,drawSize))));
		shapes.add(path.get(0));
    }
 
    public void setDraw(DrawType _drawType){
    	drawType = _drawType;
    	resetPath();
    }
    
    public void setDrawSize(int _drawSize){
    	drawSize = _drawSize;
    	resetPath();
    }
    
    public void setColor(int _colorPicked){
    	colorPicked = _colorPicked;
    	resetPath();
    }
    
    public void setPaintStyle(Paint.Style _paintStyle){
    	paintStyle = _paintStyle;
    	resetPath();
    }
    
    public void setDrawBackgroundColor(int color){
    	backgroundPaint.setColor(color);
    	invalidate();
    }
    
    public void reDraw(){
    	resetPath();
    	invalidate();
    }
    
    public void undoLast(){
    	Log.i("Test Event", "<------------- Entered undoLast ------------- >");
    	Shape shape = new Shape();
    	if(!(shapes.size()-20 < 0)){
    		Log.i("Test Event", "<------------- Enter undoLast  IF------------- >");
    		//shapes.trimToSize();
        	invalidate();
        	//resetPath();
    	}
    	
    	Log.i("Test Event", "<------------- Exit undoLast ------------- >");
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
    	case RECTANGLE:
    		readyRectangle(event, tempX, tempY);
    		break;
    	}
		invalidate();
		return true;
	}
    public void resetPath(){
    	shapes.add(new Dpath(new Path(), new Paint(setPaint(colorPicked, paintStyle, Paint.Cap.ROUND,drawSize))));
    }
    public void readyRectangle(MotionEvent event, float tempX, float tempY){
    	shapes.add(new Rectangle(new Rect((int)tempX, (int)tempY,(int) (tempX + drawSize),(int)( tempY + drawSize)), new Paint(setPaint(colorPicked, paintStyle, Paint.Cap.ROUND,drawSize))));
    }
    public void readyCircle(MotionEvent event, float tempX, float tempY){
    	shapes.add(new Circle(tempX, tempY, drawSize, new Paint(setPaint(colorPicked, paintStyle, Paint.Cap.ROUND,drawSize))));
    	
    }
    
    public void readyPath(MotionEvent event, float tempX, float tempY){
    	
    	Log.i("Test Event", "<-------------" + path.size() + "------------- >");
    	
    	switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	        Log.i("Test Event", "<------------- DOWN ------------- >");
	        ((Dpath)shapes.get(shapes.size() -1)).makeMove(tempX, tempY);
	        break;
	    case MotionEvent.ACTION_MOVE:
	    	Log.i("Test Event", "<------------- MOVE ------------- >");
	    	((Dpath)shapes.get(shapes.size() -1)).makeLine(tempX, tempY);
	        break;
	    case MotionEvent.ACTION_UP:
	    	Log.i("Test Event", "<------------- UP ------------- >");
	        break;
		}
    }
    
    public void clearCanvas(){
    	path.clear();
    	shapes.clear();
    	path.add(new Dpath(new Path(), new Paint(setPaint(colorPicked, paintStyle, Paint.Cap.ROUND,drawSize))));
    	shapes.add(path.get(0));
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
    		
    		canvas.drawPaint(backgroundPaint);
    		
    		
    		for(int count = 0; count < shapes.size();count++){
    			shapes.get(count).draw(canvas);
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
    }

}
