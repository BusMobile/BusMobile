package hopur2.BusMobile;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

//Klasi sem birtir kort


public class MapDisplay extends MapActivity {
	
	private RouteOverlay routeOverlay;
	private PointsOverlay pointsOverlay;
	private GeoPoint srcGeoPoint;
	private MyLocationOverlay locationOverlay;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.maps);
		
		int routeNumber = this.getIntent().getIntExtra("hopur2.BusMobile.MapDisplay.ROUTE_NUMBER", 0);
		int zoom = this.getIntent().getIntExtra("hopur2.BusMobile.MapDisplay.ZOOM", 12);
		
	    Drawable drawable = this.getResources().getDrawable(R.drawable.stop);
	    pointsOverlay = new PointsOverlay(drawable,this);

		
		if(routeNumber!=0){
			KMLParser kml = new KMLParser(this,routeNumber);
		    GeoPoint[] gps = kml.parseRoute();
			DataBaseManager dbm = DataBaseManager.getInstance();
			Cursor curs = dbm.doQuery("SELECT DISTINCT color FROM Route WHERE number="+routeNumber+";");
			curs.moveToFirst();
			if(curs.getInt(0)==2);
		    	routeOverlay = new RouteOverlay(gps,Color.GREEN);
		    if(curs.getInt(0)==3)
		    	routeOverlay=new RouteOverlay(gps,Color.BLUE);
		    else
		    	routeOverlay = new RouteOverlay(gps);
		    
		    srcGeoPoint = gps[0];
	    	pointsOverlay.addRoute(routeNumber);
		}
		else{
			routeOverlay = new RouteOverlay(null);
			double lon = this.getIntent().getDoubleExtra("hopur2.BusMobile.MapDisplay.SRC_LON", -21.949332);
			double lat = this.getIntent().getDoubleExtra("hopur2.BusMobile.MapDisplay.SRC_LAT",64.140493);
			srcGeoPoint=new GeoPoint((int)(lat*1e6),(int)(lon*1e6));
			NearbyStopFinder fbsd = new NearbyStopFinder(lon,lat);
			String[][] busStops = fbsd.getBusStops();
			for(int i = 0;i!=busStops.length;i++)
			{
				GeoPoint gp = new GeoPoint((int) (Double.parseDouble(busStops[i][3])*1e6),(int) (Double.parseDouble(busStops[i][2])*1e6));
				pointsOverlay.addOverlay(new OverlayItem(gp,busStops[i][0],busStops[i][1]));
			}
		}
	    

	    
		MapView mpvw = (MapView) findViewById(R.id.myMapView1);
		
		locationOverlay = new MyLocationOverlay(this,mpvw);
		locationOverlay.enableMyLocation();
		
		pointsOverlay.setContext(this);
	    
		mpvw.setBuiltInZoomControls(true);
	    mpvw.getOverlays().add(routeOverlay);
	    mpvw.getOverlays().add(pointsOverlay);
	    mpvw.getOverlays().add(locationOverlay);
	    mpvw.getController().animateTo(srcGeoPoint);
	    mpvw.getController().setZoom(zoom);
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onResume(){
		locationOverlay.enableMyLocation();
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		locationOverlay.disableMyLocation();
		super.onPause();
	}
	
}


