package hopur2.BusMobile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class GuideUI extends Activity implements OnClickListener{
	Button landmarkbutton1,landmarkbutton2,resultButton;
	AutoCompleteTextView fromField,toField;
	TimePicker timePicker;
	DatePicker datePicker;
	RadioGroup timeTypeGroup;
	String timeType = "T";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	WindowManager winMan = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    	SpannableString content= new SpannableString("Kennileiti");;
	    if (winMan != null)
	    {
	        int orientation = winMan.getDefaultDisplay().getOrientation();
	        
	        if (orientation == 0) {
	            // Portrait
	            setContentView(R.layout.guidelayout_vert);
	    		content = new SpannableString("Kenni\nleiti");
	        }
	        else if (orientation == 1) {
	            // Landscape
	            setContentView(R.layout.guidelayout_hor);
	        }            
	    }
		timePicker = (TimePicker) findViewById(R.id.timepicker);
		timePicker.setIs24HourView(true);
		datePicker = (DatePicker) findViewById(R.id.datepicker);
		resultButton = (Button) findViewById(R.id.getResults);
		resultButton.setOnClickListener(this);
		landmarkbutton1 = (Button) findViewById(R.id.landmarkbutton1);
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		landmarkbutton1.setText(content);
		landmarkbutton2 = (Button) findViewById(R.id.landmarkbutton2);
		landmarkbutton2.setText(content);
		landmarkbutton1.setOnClickListener(this);
		landmarkbutton2.setOnClickListener(this);
		
		timeTypeGroup = (RadioGroup) findViewById(R.id.timetypegroup);
		
		timeTypeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(RadioGroup arg0, int checked) {
				if(checked==R.id.koma){
					timeType="F";
				}
				else
					timeType="T";
					
			}});

		String fromString = getIntent().getStringExtra("hopur2.BusMobile.GuideUI.FROM");
		String toString = getIntent().getStringExtra("hopur2.BusMobile.GuideUI.TO");

		fromField = (AutoCompleteTextView) findViewById(R.id.fromfield);
		fromField.setText(fromString);
		toField = (AutoCompleteTextView) findViewById(R.id.tofield);
		toField.setText(toString);


	}
	public void onClick(View v) {
		if(v==resultButton){
			
			String url = "http://www.straeto.is/leit?from=";
			
			try {
				String from =URLEncoder.encode(toField.getText().toString(),"UTF-8");
				url = url+from+"&from_auto_selected=true&to=";
				String to = URLEncoder.encode(fromField.getText().toString(),"UTF-8");
				url = url + to+"&to_auto_selected=true&arrival_hour=";
				url = url + timePicker.getCurrentHour()+"&arrival_minute=";
				url = url + timePicker.getCurrentMinute()+"&timetype=";
				url = url + timeType+"&day=" ;
				url = url + datePicker.getYear()+"-";
				url = url +	(datePicker.getMonth()+1)+"-";
				url = url + datePicker.getDayOfMonth()+"&date=&Staðfesta=Finna+leiðir";
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Intent guide = new Intent(this,hopur2.BusMobile.GuideResults.class);
			guide.putExtra("hopur2.BusMobile.GuideResults.URL", url);
			try {
				this.startActivity(guide);
			}
			catch(ActivityNotFoundException e){
				e.toString();
			}
			
		}
		else{
			Intent landmarkIntent = new Intent(this,hopur2.BusMobile.LandmarkMenu.class);
			landmarkIntent.putExtra("hopur2.BusMobile.GuideUI.FROM",fromField.getText().toString());
			landmarkIntent.putExtra("hopur2.BusMobile.GuideUI.TO",toField.getText().toString());
			if(v==landmarkbutton1)
				landmarkIntent.putExtra("hopur2.BusMobile.LandmarkMenu.IS_FROM", true);
			else
				landmarkIntent.putExtra("hopur2.BusMobile.LandmarkMenu.IS_FROM", false);

			try {
				this.startActivity(landmarkIntent);
			}
			catch(ActivityNotFoundException e){
				e.toString();
			}
		}

	}

}
