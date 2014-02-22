package com.example.mydraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.example.mydraw.DrawPad.DrawType;

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

public class MainActivity extends Activity implements OnTouchListener, OnClickListener {
	
	DrawPad mainCanvas;
	HashMap<String, Button> buttonMap;
	
	LinearLayout typeLayout;
	LinearLayout widthLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initViews();
		
		
		mainCanvas.setOnTouchListener(this);
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
			toggleButtonMenu();
			break;
		case R.id.bottonRightCornerButton:
			mainCanvas.clearCanvas();
			break;
		case R.id.pointButton:
			mainCanvas.setDraw(DrawType.POINT);
			break;
		case R.id.lineButton:
			mainCanvas.setDraw(DrawType.LINE);
			break;
	}
		
	}
	
	public void initViews(){
		
		buttonMap = new HashMap<String, Button>();
		mainCanvas = (DrawPad) findViewById(R.id.mainCanvas);
		
		buttonMap.put("topLeftCornerButton", (Button)findViewById(R.id.topLeftCornerButton));
		buttonMap.put("bottomRightCornerButton", (Button)findViewById(R.id.bottonRightCornerButton));
		buttonMap.put("widthSmallButton", (Button)findViewById(R.id.widthSmallButton));
		buttonMap.put("widthMediumButton", (Button)findViewById(R.id.widthMediumButton));
		buttonMap.put("widthLargeButton", (Button)findViewById(R.id.widthLargeButton));
		buttonMap.put("pointButton", (Button)findViewById(R.id.pointButton));
		buttonMap.put("circleButton", (Button)findViewById(R.id.circleButton));
		buttonMap.put("lineButton", (Button)findViewById(R.id.lineButton));
		
		typeLayout = (LinearLayout) findViewById(R.id.typeLayout);
		widthLayout = (LinearLayout) findViewById(R.id.widthLayout);
		
		for(Entry<String,Button> entry: buttonMap.entrySet()){
			entry.getValue().setOnClickListener(this);
		}
		
		
	}
	
	public void toggleButtonMenu(){
		
		if(typeLayout.getVisibility() == View.VISIBLE) {
			typeLayout.setVisibility(View.GONE);
			widthLayout.setVisibility(View.GONE);
		}else{
			typeLayout.setVisibility(View.VISIBLE);
			widthLayout.setVisibility(View.VISIBLE);
		}
			
	}

}
