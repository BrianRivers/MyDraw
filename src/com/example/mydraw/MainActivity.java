package com.example.mydraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.example.mydraw.DrawPad.DrawSize;
import com.example.mydraw.DrawPad.DrawType;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener, OnClickListener, OnColorChangedListener {
	
	DrawPad mainCanvas;
	HashMap<String, Button> buttonMap;
	
	LinearLayout typeLayout;
	LinearLayout widthLayout;
	LinearLayout canvasLayout;
	
	ColorPicker colorPicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initViews();
		mainCanvas.setOnTouchListener(this);
		
		colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
		colorPicker.setOnColorChangedListener(this);
		colorPicker.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.clearAll:
				mainCanvas.clearCanvas();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {return false;}

	@Override
	public void onClick(View v) {
		Log.i("Test Click Event", "< ----------------- I Was Clicked! ----------------->");
		switch(v.getId()){
		case R.id.topLeftCornerButton:
			toggleButtonMenu(R.id.topLeftCornerButton);
			break;
		case R.id.bottonRightCornerButton:
			toggleButtonMenu(R.id.bottonRightCornerButton);
			break;
		case R.id.topRightCornerButton:
			 Log.i("Test Event", "<------------- Enter TopRightCorner OnClick ------------- >");
			mainCanvas.savePicture(true, "test");
			break;
		case R.id.pointButton:
			mainCanvas.setDraw(DrawType.POINT);
			toggleButtonMenu(R.id.topLeftCornerButton);
			break;
		case R.id.lineButton:
			mainCanvas.setDraw(DrawType.LINE);
			toggleButtonMenu(R.id.topLeftCornerButton);
			break;
		case R.id.widthSmallButton:
			mainCanvas.setDrawSize(DrawSize.SMALL);
			toggleButtonMenu(R.id.topLeftCornerButton);
			break;
		case R.id.widthMediumButton:
			mainCanvas.setDrawSize(DrawSize.MEDIUM);
			toggleButtonMenu(R.id.topLeftCornerButton);
			break;
		case R.id.widthLargeButton:
			mainCanvas.setDrawSize(DrawSize.LARGE);
			toggleButtonMenu(R.id.topLeftCornerButton);
			break;
	}
		
	}
	
	public void initViews(){
		Log.i("Test Click Event", "< ----------------- Enter initViews() ----------------->");
		buttonMap = new HashMap<String, Button>();
		mainCanvas = (DrawPad) findViewById(R.id.mainCanvas);
		
		
		buttonMap.put("topLeftCornerButton", (Button)findViewById(R.id.topLeftCornerButton));
		buttonMap.put("bottomRightCornerButton", (Button)findViewById(R.id.bottonRightCornerButton));
		buttonMap.put("topRightCornerButton", (Button)findViewById(R.id.topRightCornerButton));
		buttonMap.put("widthSmallButton", (Button)findViewById(R.id.widthSmallButton));
		buttonMap.put("widthMediumButton", (Button)findViewById(R.id.widthMediumButton));
		buttonMap.put("widthLargeButton", (Button)findViewById(R.id.widthLargeButton));
		buttonMap.put("pointButton", (Button)findViewById(R.id.pointButton));
		buttonMap.put("circleButton", (Button)findViewById(R.id.circleButton));
		buttonMap.put("lineButton", (Button)findViewById(R.id.lineButton));
		
		typeLayout = (LinearLayout) findViewById(R.id.typeLayout);
		widthLayout = (LinearLayout) findViewById(R.id.widthLayout);
		canvasLayout = (LinearLayout) findViewById(R.id.canvasLayout);
		
	
		
		for(Entry<String,Button> entry: buttonMap.entrySet()){
			entry.getValue().setOnClickListener(this);
		}
		
		
		
		Log.i("Test Click Event", "< ----------------- Exit initViews() ----------------->");
	}
	
	public void toggleButtonMenu(int toggle){
		
		switch(toggle){
		case R.id.topLeftCornerButton :
			if(typeLayout.getVisibility() == View.VISIBLE) {
				typeLayout.setVisibility(View.GONE);
				widthLayout.setVisibility(View.GONE);
			}else{
				typeLayout.setVisibility(View.VISIBLE);
				widthLayout.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.bottonRightCornerButton:
			if(colorPicker.getVisibility() == View.VISIBLE) {
				colorPicker.setVisibility(View.GONE);
				mainCanvas.setColor(colorPicker.getColor());
			}else{
				colorPicker.setVisibility(View.VISIBLE);
			}
		}
			
	}

	@Override
	public void onColorChanged(int color) {
		// TODO Auto-generated method stub
		Log.i("Test Click Event", "< ----------------- "+ colorPicker.getColor() + " ----------------->");
		colorPicker.setOldCenterColor(colorPicker.getColor());
	}

}
