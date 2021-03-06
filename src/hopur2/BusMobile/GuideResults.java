package hopur2.BusMobile;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GuideResults extends Activity {
	private WebGuideParser wgp;
	private String url;
    @Override
	public void onCreate(Bundle icicle){
    	super.onCreate(icicle);
    	WindowManager winMan = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	    
	    if (winMan != null)
	    {
	        int orientation = winMan.getDefaultDisplay().getOrientation();
	        
	        if (orientation == 0) {
	            // Portrait
	            setContentView(R.layout.guideresult);
	        }
	        else if (orientation == 1) {
	            // Landscape
	            setContentView(R.layout.guideresult);
	        }            
	    }
    	url = getIntent().getStringExtra("hopur2.BusMobile.GuideResults.URL");
    	wgp = new WebGuideParser(url);
    	TextView subheading =(TextView) findViewById(R.id.heading2);
    	subheading.setText(wgp.getSubHeading());
    	TextView tblhead1 = (TextView) findViewById(R.id.tableheading1);
    	tblhead1.setText(wgp.getTblHead1());
    	ArrayList<String[]> tablerows1 = wgp.getTable1();
    	TableLayout table1 = (TableLayout) findViewById(R.id.table1);
    	if(tablerows1!=null){

	    	for(int i = 0; i<tablerows1.size();i++){
	    		String[] row = tablerows1.get(i);
	    		TableRow tRow = new TableRow(this);
	    		tRow.setPadding(5, 5, 5, 5);
	    		for(int j =0; j!=row.length;j++){
	    			TextView tv = new TextView(this);
	    			tv.setText(row[j]);
	    			tv.setPadding(5, 5, 5, 5);
	    			tv.setTextColor(R.color.black);
	    			tRow.addView(tv);
	    		}
	    		table1.addView(tRow);
	    	}
    	}
    	TextView tblhead2 = (TextView) findViewById(R.id.tableheading2);
    	tblhead2.setText(wgp.getTblHead2());
    	tablerows1 = wgp.getTable2();
    	if(tablerows1!=null){
	    	TableLayout table2 = (TableLayout) findViewById(R.id.table2);
	    	for(int i = 0; i<tablerows1.size();i++){
	    		String[] row = tablerows1.get(i);
	    		TableRow tRow = new TableRow(this);
	    		tRow.setPadding(5, 5, 5, 5);
	    		for(int j =0; j!=row.length;j++){
	    			TextView tv = new TextView(this);
	    			tv.setText(row[j]);
	    			tv.setPadding(5, 5, 5, 5);	    			
	    			tv.setTextColor(R.color.black);
	    			tRow.addView(tv);
	    		}
	    		table2.addView(tRow);
	    	}
    	}
    	
    }
}
