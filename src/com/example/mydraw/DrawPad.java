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
	
	private List<Float> xPointPositions;
	private List<Float> yPointPositions;
	private List<Paint> paintLine;
	private List<Paint> paintPoint;
	private List<Path> path;
	
	private DrawType drawType;
	private DrawSize drawSize;
	private int colorPicked;
	private String fileName;
	
	private int canvasWidth;
	private int canvasHeight;
	private Bitmap saveBitmap;
	private boolean toggleSave;
	
	
	
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
        
        Path initialPath = new Path();
        Paint initialPaint = new Paint();
        
        Log.i("Test Event", "<-------------" + Color.WHITE + "------------- >");
        
        initialPaint.setColor(colorPicked);
        initialPaint.setStyle(Paint.Style.STROKE);
        initialPaint.setStrokeCap(Cap.ROUND);
        initialPaint.setStrokeWidth(drawSize.getSize());
	
		
		paintLine = new ArrayList<Paint>();
		paintPoint = new ArrayList<Paint>();
		
		path = new ArrayList<Path>();
		
		paintLine.add(initialPaint);
		paintPoint.add(initialPaint);
		path.add(initialPath);
		
		xPointPositions = new ArrayList<Float>();
		yPointPositions = new ArrayList<Float>();
		xPointPositions.add(Float.valueOf(-10));
		yPointPositions.add(Float.valueOf(-10));
		
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
    
    public void addDrawing(){

      Paint tempPaint = new Paint();
      
	  tempPaint.setColor(colorPicked);
      tempPaint.setStyle(Paint.Style.STROKE);
      tempPaint.setStrokeCap(Cap.ROUND);
      tempPaint.setStrokeWidth(drawSize.getSize());
      
      Log.i("Test Event", "<-------------" + tempPaint.getStrokeWidth() + "------------- >");
      
      paintLine.add(tempPaint);
      paintPoint.add(tempPaint);
      path.add(new Path());
      xPointPositions.add(Float.valueOf(-10));
      yPointPositions.add(Float.valueOf(-10));
      
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
	        path.get(path.size() -1 ).moveTo(tempX, tempY);
	        break;
	    case MotionEvent.ACTION_MOVE:
	    	Log.i("Test Event", "<------------- MOVE ------------- >");
	    	path.get(path.size() -1 ).lineTo(tempX, tempY);
	        break;
	    case MotionEvent.ACTION_UP:
	    	Log.i("Test Event", "<------------- UP ------------- >");
	        break;
		}
    }
    
    public void readyPoint(MotionEvent event,  float tempX, float tempY){
    		xPointPositions.add(tempX);
    		yPointPositions.add(tempY);
    		paintPoint.add(paintPoint.get(paintPoint.size()-1));
    	
    }
    
    public void clearCanvas(){
    	path.clear();
    	paintLine.clear();
    	paintPoint.clear();
    	xPointPositions.clear();
    	yPointPositions.clear();
    	
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

    		for (int count = 0; count < path.size(); count++){
    			canvas.drawPath(path.get(count), paintLine.get(count));
    		}
    		
    		for(int count = 0; count < xPointPositions.size(); count++){
    			canvas.drawPoint(xPointPositions.get(count), yPointPositions.get(count), paintPoint.get(count));
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
