package hopur2.BusMobile;



import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;




import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

/* Klasi sem finnur nálægar stanzstöðvar útfrá
 * GPS hnitum notanda.
 */

public class NearbyStopMenu extends Activity {
	
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1; // in Milliseconds
	private int textSize = 15;
	private LocationManager locationManager;
	private Location location;
	protected Button retrieveLocationButton;
	private NearbyStopFinder busStopsData;
	String [][] busStops;
	String cutOfBuses = "1";
	double lon1, lon2, gpsLon;
	double lat1, lat2, gpsLat;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findbusstoplayout);

		/* Vegna þess að emulator-inn er svo ónákvæmur þá 
		 * ákvað ég að hafa statískar breytur til þess að 
		 * herma eftir staðsetningu notanda
		 * 
		 */


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);      
        locationManager.requestLocationUpdates(
        		LocationManager.GPS_PROVIDER, 
        		MINIMUM_TIME_BETWEEN_UPDATES, 
        		MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
        		new MyLocationListener()
        );
        
     
        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);
        retrieveLocationButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showMap();
				//showCurrentLocation();
			}
		});
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		//this.gpsLon = location.getLongitude();
		//this.gpsLat = location.getLatitude();
		//this.gpsLon = -21.949332;
		//this.gpsLat = 64.140493;
		this.gpsLon =location.getLongitude();// -21.913657;
		this.gpsLat =location.getLatitude(); // 64.143264;
		lon1 = this.gpsLon - 0.008939;
		lon2 = this.gpsLon + 0.008939;
		lat1 = this.gpsLat - 0.004505;
		lat2 = this.gpsLat + 0.004505;
		
		//Þetta er kóðinn sem á að nota á Android símanum
//		lon1 = location.getLongitude() - 0.008939;
//		lon2 = location.getLongitude() + 0.008939;
//		lat1 = location.getLatitude() - 0.004505;
//		lat2 = location.getLatitude() + 0.004505; 
		busStopsData = new NearbyStopFinder(this,lon1,lon2,lat1,lat2);
		busStops  = busStopsData.getBusStops();
		
		displayBusStops(busStops);
        
        
		
    }    
    
    public void displayBusStops (String[][] busStops) {
    	TableLayout tblBusStops = (TableLayout)findViewById(R.id.title);
    	tblBusStops.setBackgroundColor(Color.rgb(255, 250, 250));
    	TableRow trHead = new TableRow(this);
    	trHead.findViewById(R.id.trHead);
    	TextView tvHead = new TextView(this);
    	tvHead.findViewById(R.id.names);
    	
    	//lista gögn
    	for (int i=0; i!=busStops.length; i++) {
    		TableRow tr = new TableRow(this);
    		//tr.findViewById(R.id.trDynamicData);
    		
    		if (i%2==0) {
    			//tr.setBackgroundColor(Color.rgb(255, 140, 0));
    		}
    		tr.setBackgroundDrawable(getResources().getDrawable(R.drawable.listitem_bg_selector));
    		//Nöfnin á stoppustöðunum 
    		Button tvBusStops = new Button(this);
    		//tvBusStops.findViewById(R.id.dynamicNames);
    		SpannableString content = new SpannableString(busStops[i][0]);
    		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
    		tvBusStops.setText(content);
    		tvBusStops.setTextColor(getResources().getColor(R.color.red));
    		tvBusStops.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg_selector));
    		tvBusStops.setTextSize(textSize);
    		tvBusStops.setGravity(Gravity.LEFT);
    		
    		
    		
    		//Bæta við hlustara á hverja stoppustöð
    		final int x = Integer.parseInt(busStops[i][1]);
    		tvBusStops.setOnClickListener(new View.OnClickListener() {
                public void onClick(View vx) {
                    Intent intent = new Intent(vx.getContext(), hopur2.BusMobile.BusStopDisplay.class);
                    try {
                    	startActivity(intent);
                    }
            		catch(ActivityNotFoundException e){
            			e.toString();
            		}
            		//hopur2.BusMobile.LeidarTafla.RouteID = Integer.parseInt(busList[route]);
            		hopur2.BusMobile.BusStopDisplay.busstopID = x;
                }
            });
    		
    		tr.addView(tvBusStops);
    		
    		//Fjarlægð á hverja stoppustð fyrir sig frá notenda
    		double lontmp = Double.parseDouble(busStops[i][2]);
    		double lattmp = Double.parseDouble(busStops[i][3]);
    		int tmp2 = calculateDistance(lontmp,lattmp, this.gpsLon, this.gpsLat);
    		
    		TextView dis = new TextView(this);
   // 		dis.findViewById(R.id.dynamicDistance);
    		dis.setText(Integer.toString(tmp2)+ "m");
    		dis.setPadding(0, 0, 15, 0);
    		dis.setGravity(05);//Gravity.RIGHT?
    		dis.setTextSize(textSize);
    		dis.setTextColor(Color.rgb(0, 0, 0));
    		tr.addView(dis);
    		
    		
    		
    		
    		//tblBusStops.addView(tr);
    		
    		//Listi yfir vagna sem stoppa á hverri stöð
    		final String busList[] = busStopsData.findBuses(busStops[i][1]);
   
    		
    	//	TableLayout tblInnerLayout = new TableLayout(this);
    //		tblInnerLayout.findViewById(R.id.tblInnerLayout);
    		TableRow tableRow = new TableRow(this);
    //		tableRow.findViewById(R.id.rowBuses);
    		for (int n=0; n!=busList.length; n++) {
    			final int route = n;
    			//klippa síðustu töluna af
    			String tmp = busList[n];
    			int routeid = Integer.parseInt(tmp);
    			int routenumber = routeid/10;
//    			tmp = tmp.substring(0, tmp.length()-1);
//    			
//    			if (n >= 1) {
//    				this.cutOfBuses = busList[n-1];
//    				this.cutOfBuses = this.cutOfBuses.substring(0, this.cutOfBuses.length()-1);
//    			}	
//    				
//    				
//					if (tmp.compareTo(this.cutOfBuses)== 0) {
//    					
//    				}
//    				
//    				else {
    			
    				
    			
    			
    			//Bæta hverjum vagni inná skjáinn
    			
    			
    			Button tvBuses= new Button(this);
    			//tvBuses.findViewById(R.id.dynamicBuses);
    			tvBuses.setWidth(40);
				try {
	    			R.drawable r = new R.drawable();
	    			int id;
					id = r.getClass().getField("button"+routenumber).getInt(r);
	    			Drawable d = this.getResources().getDrawable(id);
	    			tvBuses.setBackgroundDrawable(d);
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchFieldException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

    			//tvBuses.setTextSize(textSize);
    			//tvBuses.setGravity(05);
    			
    			//tvBuses.setTextColor(Color.rgb(0, 0, 0));
        		//tblDynamicBuses.addView(tvBuses);
				tr.addView(tvBuses);
        		//tableRow.addView(tvBuses);
    			//tr.addView(tvBuses);
        		//tr.addView(tblDynamicBuses);
        		
        		//bæta við hlustara á hvern vagn
        		tvBuses.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), hopur2.BusMobile.RouteDisplay.class);
                        try {
                        	startActivity(intent);
                        }
                		catch(ActivityNotFoundException e){
                			e.toString();
                		}
                		hopur2.BusMobile.RouteDisplay.RouteID = Integer.parseInt(busList[route]);
                    }
                }); 
    				
    				
    			//}
        		
    		} //for endar
    		//tr.addView(tblDynamicBuses);
    		//tblInnerLayout.addView(tableRow);
    		//tr.addView(tblInnerLayout);
    		tblBusStops.addView(tr);
    		
    		
    	}
    	
    } //displayBusStops ends
    private int calculateDistance (double lon, double lat ,double gpsLon, double gpsLat) {
    	//Nota Haversine formúluna
    	// R er radíus jaðar = 6371 km
    	//double gpsLon = -21.949332;
		//double gpsLat = 64.140493;
    	int R = 6371000;
    	double dlon = Math.toRadians(gpsLon - lon);
    	double dlat = Math.toRadians(gpsLat - lat);
    	double a = Math.sin(dlat/2) * Math.sin(dlat/2) + 
    			  (Math.cos(Math.toRadians(lat))) * 
    			  (Math.cos(Math.toRadians(gpsLat))) *
    			   Math.sin(dlon/2) * Math.sin(dlon/2);
    	double c = Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
    	int distance = (int)(R*c);
    	
    	return distance;
    }

	protected void showCurrentLocation() {	
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			String message = String.format(
					"Current Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
			);
			Toast.makeText(NearbyStopMenu.this, message,
					Toast.LENGTH_LONG).show();
		}

	} 
	private void showMap(){
		Intent map = new Intent(this,hopur2.BusMobile.MapDisplay.class);
		map.putExtra("hopur2.BusMobile.MapDisplay.SRC_LON",gpsLon);
		map.putExtra("hopur2.BusMobile.MapDisplay.SRC_LAT",gpsLat);
		map.putExtra("hopur2.BusMobile.MapDisplay.ZOOM",15);

		try {
			this.startActivity(map);
		}
		catch(ActivityNotFoundException e){
			e.toString();
		}
	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			String message = String.format(
					"New Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
			);
			Toast.makeText(NearbyStopMenu.this, message, Toast.LENGTH_LONG).show();
		}

		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(NearbyStopMenu.this, "Provider status changed",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(NearbyStopMenu.this,
					"Provider disabled by the user. GPS turned off",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(NearbyStopMenu.this,
					"Provider enabled by the user. GPS turned on",
					Toast.LENGTH_LONG).show();
		}

	}
    
}