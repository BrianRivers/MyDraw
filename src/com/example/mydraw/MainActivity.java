package com.example.mydraw;

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
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener, OnClickListener, OnColorChangedListener {
	
	private DrawPad mainCanvas;
	private HashMap<String, Button> buttonMap;
	
	private LinearLayout typeLayout;
	private LinearLayout widthLayout;
	private LinearLayout canvasLayout;
	private LinearLayout colorPickerLayout;
	
	private ColorPicker colorPicker;
	private OpacityBar opacityBar;
	private SVBar svBar;
	private SaturationBar saturationBar;
	private ValueBar valueBar;
	
	private int valueCheck;
	
	AlertDialog.Builder alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initViews();
		mainCanvas.setOnTouchListener(this);
		
		
		colorPicker.setOnColorChangedListener(this);
		colorPicker.setOnClickListener(this);
		colorPicker.addOpacityBar(opacityBar);
		colorPicker.addSVBar(svBar);
		colorPicker.addOpacityBar(opacityBar);
		colorPicker.addSaturationBar(saturationBar);
		colorPicker.addValueBar(valueBar);
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
		case R.id.circleButton:
			initAlert();
			alert.setTitle("Desired radius?");
			alert.show();
			Log.i("Test Click Event", "< ----------------- " + valueCheck + " ----------------->");
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
		colorPickerLayout = (LinearLayout) findViewById(R.id.colorPickerLayout);
		
		colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
		opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
		svBar = (SVBar) findViewById(R.id.svbar);
		saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
		valueBar = (ValueBar) findViewById(R.id.valuebar);
	
		initAlert();
		for(Entry<String,Button> entry: buttonMap.entrySet()){
			entry.getValue().setOnClickListener(this);
		}
		
		
		
		Log.i("Test Click Event", "< ----------------- Exit initViews() ----------------->");
	}
	
	public void initAlert(){
		alert = new AlertDialog.Builder(this);
		
		alert.setTitle("Title");

		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		alert.setView(input);

		// Set up the buttons
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        valueCheck = Integer.parseInt(input.getText().toString());
		        mainCanvas.setRadius(valueCheck);
		        mainCanvas.setDraw(DrawType.CIRCLE);
				toggleButtonMenu(R.id.topLeftCornerButton);
		    }
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});
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
			if(colorPickerLayout.getVisibility() == View.VISIBLE) {
				colorPickerLayout.setVisibility(View.GONE);
				mainCanvas.setColor(colorPicker.getColor());
			}else{
				colorPickerLayout.setVisibility(View.VISIBLE);
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
