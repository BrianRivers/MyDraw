package com.example.mydraw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.example.mydraw.DrawPad.DrawSize;
import com.example.mydraw.DrawPad.DrawType;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener, OnClickListener, OnColorChangedListener {
	
	private DrawPad mainCanvas;
	private HashMap<String, Button> buttonMap;
	
	private LinearLayout typeLayout;
	private LinearLayout widthLayout;
	private LinearLayout colorPickerLayout;
	private RelativeLayout sizeInputLayout;
	
	private final int POSITION_BACK = -500;
	private final int POSTION_FORWARD = 500;
	
	private EditText strokeEditText;
	
	private ColorPicker colorPicker;
	private OpacityBar opacityBar;
	private SVBar svBar;
	private SaturationBar saturationBar;
	private ValueBar valueBar;
	private boolean offScreen;
	
	private int valueCheck;
	
	private RadioGroup strokeFillRadio;
	
	AlertDialog.Builder alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initViews();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ( keyCode == KeyEvent.KEYCODE_MENU ) {
	    	buttonMap.get("topLeftCornerButton").performClick();
	        return true;
	    }
	    if ( keyCode == KeyEvent.KEYCODE_BACK ) {
	    	toggleView(colorPickerLayout);
	    	mainCanvas.setColor(colorPicker.getColor());
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.clearAll:
				mainCanvas.clearCanvas();
				break;
			case R.id.strokeWidth:
				toggleButtonMenu(R.id.strokeWidth);
				break;
			case R.id.setBackground:
				toggleButtonMenu(R.id.setBackground);
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {return false;}
	
	@Override
	protected void onPause() {
		
		try {
			FileOutputStream outputCanvas = new FileOutputStream(new File("/data/data/com.example.mydraw/canvas"));
			ObjectOutputStream os = new ObjectOutputStream(outputCanvas);
			os.writeObject(mainCanvas);
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		
		try {
			FileInputStream fis = new FileInputStream(new File("/data/data/com.example.mydraw/canvas"));
			ObjectInputStream is = new ObjectInputStream(fis);
			DrawPad mainCanvas = (DrawPad) is.readObject();
			is.close();
			mainCanvas.reDraw();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		Log.i("Test Click Event", "< ----------------- I Was Clicked! ----------------->");
		switch(v.getId()){
		case R.id.topLeftCornerButton:
			toggleButtons();
			break;
		case R.id.rectangleButton:
			mainCanvas.setDraw(DrawType.RECTANGLE);
			buttonMap.get("topLeftCornerButton").performClick();
			break;
		case R.id.lineButton:
			mainCanvas.setDraw(DrawType.LINE);
			buttonMap.get("topLeftCornerButton").performClick();
			break;
		case R.id.circleButton:
			 mainCanvas.setDraw(DrawType.CIRCLE);
			 buttonMap.get("topLeftCornerButton").performClick();
			Log.i("Test Click Event", "< ----------------- " + valueCheck + " ----------------->");
			break;
		case R.id.colorButton:
			buttonMap.get("topLeftCornerButton").performClick();
			toggleView(colorPickerLayout);
			break;
		case R.id.saveButton:
			break;
		case R.id.undoButton:
			mainCanvas.clearCanvas();
			buttonMap.get("topLeftCornerButton").performClick();
			break;
		case R.id.settingsButton:
			buttonMap.get("topLeftCornerButton").performClick();
			toggleView(sizeInputLayout);
			break;
		case R.id.button1:
			toggleView(sizeInputLayout);
			mainCanvas.setDrawSize(Integer.parseInt(strokeEditText.getText().toString()));
			if(strokeFillRadio.getCheckedRadioButtonId() == R.id.strokeRadioButton)
				mainCanvas.setPaintStyle(Paint.Style.STROKE);
			else if(strokeFillRadio.getCheckedRadioButtonId() == R.id.fillRadioButton)
				mainCanvas.setPaintStyle(Paint.Style.FILL);
			break;
	}
		
	}
	
	public void initViews(){
		Log.i("Test Click Event", "< ----------------- Enter initViews() ----------------->");
		buttonMap = new HashMap<String, Button>();
		mainCanvas = (DrawPad) findViewById(R.id.mainCanvas);
		
		buttonMap.put("topLeftCornerButton", (Button)findViewById(R.id.topLeftCornerButton));
		buttonMap.put("saveButton", (Button)findViewById(R.id.saveButton));
		buttonMap.put("colorButton", (Button)findViewById(R.id.colorButton));
		buttonMap.put("undoButton", (Button)findViewById(R.id.undoButton));
		buttonMap.put("pointButton", (Button)findViewById(R.id.rectangleButton));
		buttonMap.put("circleButton", (Button)findViewById(R.id.circleButton));
		buttonMap.put("lineButton", (Button)findViewById(R.id.lineButton));
		buttonMap.put("setSizeButton", (Button)findViewById(R.id.button1));
		buttonMap.put("settingsButton", (Button)findViewById(R.id.settingsButton));
		
		offScreen = false;
		
		typeLayout = (LinearLayout) findViewById(R.id.typeLayout);
		widthLayout = (LinearLayout) findViewById(R.id.widthLayout);
		colorPickerLayout = (LinearLayout) findViewById(R.id.colorPickerLayout);
		sizeInputLayout = (RelativeLayout) findViewById(R.id.sizeInputLayout);
		strokeEditText = (EditText) findViewById(R.id.editText1);
		strokeFillRadio = (RadioGroup) findViewById(R.id.strokeRadioGroup);
		
		colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
		opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
		svBar = (SVBar) findViewById(R.id.svbar);
		saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
		valueBar = (ValueBar) findViewById(R.id.valuebar);
		
		mainCanvas.setOnTouchListener(this);
		colorPicker.setOnColorChangedListener(this);
		colorPicker.setOnClickListener(this);
		colorPicker.addOpacityBar(opacityBar);
		colorPicker.addSVBar(svBar);
		colorPicker.addOpacityBar(opacityBar);
		colorPicker.addSaturationBar(saturationBar);
		colorPicker.addValueBar(valueBar);
		resetLayoutPosition(POSITION_BACK);
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
		case R.id.colorButton:
		case R.id.setBackground:
			if(colorPickerLayout.getVisibility() == View.VISIBLE) {
				colorPickerLayout.setVisibility(View.GONE);
				if(R.id.colorButton == toggle)
					mainCanvas.setColor(colorPicker.getColor());
				else
					mainCanvas.setDrawBackgroundColor(colorPicker.getColor());
				
			}else{
				colorPickerLayout.setVisibility(View.VISIBLE);
			}
		break;
		case R.id.settingsButton:
		case R.id.button1:
		case R.id.strokeWidth:
			if(sizeInputLayout.getVisibility() == View.VISIBLE) {
				sizeInputLayout.setVisibility(View.GONE);
			}else{
				sizeInputLayout.setVisibility(View.VISIBLE);
			}
			break;
		}
		
		
			
	}
	
	public void toggleView(View view){
		if(view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
			
		}else{
			view.setVisibility(View.VISIBLE);	
		}
	}
	
	public void toggleButtons(){
		if (offScreen){
			resetLayoutPosition(POSITION_BACK);
			offScreen = false;
		}else{
			resetLayoutPosition(POSTION_FORWARD);
			offScreen = true;
		}
	}
	
	public void resetLayoutPosition(float position){
		typeLayout.animate().translationXBy(position).withLayer();
		widthLayout.animate().translationXBy(-position).withLayer();

	}

	@Override
	public void onColorChanged(int color) {
		// TODO Auto-generated method stub
		Log.i("Test Click Event", "< ----------------- "+ colorPicker.getColor() + " ----------------->");
		colorPicker.setOldCenterColor(colorPicker.getColor());
	}

}
