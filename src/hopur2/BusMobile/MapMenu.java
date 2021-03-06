package hopur2.BusMobile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

// Klasi sem birtir valmynd yfir kort.
public class MapMenu extends Activity {

	ImageButton kort2;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager winMan = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	    
	    if (winMan != null)
	    {
	        int orientation = winMan.getDefaultDisplay().getOrientation();
	        
	        if (orientation == 0) {
	            // Portrait
	            setContentView(R.layout.mapsmenu_vert);
	        }
	        else if (orientation == 1) {
	            // Landscape
	            setContentView(R.layout.mapsmenu_hor);
	        }            
	    }
		String sql = "SELECT DISTINCT number FROM route;";
		DataBaseManager dbm = DataBaseManager.getInstance();
		Cursor c = dbm.doQuery(sql);
		c.moveToFirst();
		while(!c.isAfterLast()){
			int routeNumber = c.getInt(0);
			createImageButton(routeNumber);
			c.moveToNext();
		}
		c.close();

	}
	private ImageButton createImageButton(final int routeNumber)
	{
		ImageButton kort=null;
		R.id r = new R.id();
		try {
			kort =(ImageButton)findViewById(r.getClass().getField("kort"+routeNumber).getInt(r));
		    kort.setOnClickListener(new OnClickListener(){
		    	
			    public void onClick(View V)
		    	{
			    	showMap(routeNumber);
		    	}
		    });
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
		}
		return kort;
	}
	private  void showMap(int routeNumber){
		Intent map = new Intent(this,hopur2.BusMobile.MapDisplay.class);
		map.putExtra("hopur2.BusMobile.MapDisplay.ROUTE_NUMBER", routeNumber);
		try {
			this.startActivity(map);
		}
		catch(ActivityNotFoundException e){
			e.toString();
		}
	}
	
}


