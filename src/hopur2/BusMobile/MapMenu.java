package hopur2.BusMobile;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class MapMenu extends Activity {

	ImageButton kort2;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.mapsmenu);
		String sql = "SELECT DISTINCT number FROM route;";
		DataBaseManager dbm = DataBaseManager.getInstance();
		Cursor c = dbm.doQuery(sql);
		c.moveToFirst();
		while(!c.isAfterLast()){
			int routeNumber = c.getInt(0);
			createImageButton(routeNumber,this);
			c.moveToNext();
		}
		c.close();

	}
	private ImageButton createImageButton(final int routeNumber,final Context c)
	{
		ImageButton kort=null;
		R.id r = new R.id();
		try {
			kort =(ImageButton)findViewById(r.getClass().getField("kort"+routeNumber).getInt(r));
		    kort.setOnClickListener(new OnClickListener(){
		    	
			    public void onClick(View V)
		    	{
			    	KMLParser kml = new KMLParser(c,routeNumber);
				    GeoPoint[] gps = kml.parseRoute();
				    RouteOverlay mo = new RouteOverlay(gps);
				    Drawable drawable = c.getResources().getDrawable(R.drawable.stop);
				    PointsOverlay points = new PointsOverlay(drawable,c);
			    	points.addRoute(routeNumber);
		    		MapDisplay.routeOverlay = mo;
		    		MapDisplay.pointsOverlay = points;
		    		MapDisplay.srcGeoPoint = gps[0];
		    		Intent map = new Intent(c,hopur2.BusMobile.MapDisplay.class);
					try {
						c.startActivity(map);
					}
					catch(ActivityNotFoundException e){
						e.toString();
					}
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
	
}


