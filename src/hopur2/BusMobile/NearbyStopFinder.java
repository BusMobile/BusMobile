package hopur2.BusMobile;

import android.content.Context;
import android.database.Cursor;

public class NearbyStopFinder {
	private DataBaseManager db;
	private double lon1,lon2;
	private double lat1, lat2;
	private String[][] locations;
	private String[] buses;
	
	public NearbyStopFinder (Context context, double lon1,double lon2, double lat1, double lat2) {
		
	    
		this.lon1 = lon1;
	    this.lon2 = lon2;
	    this.lat1 = lat1;
	    this.lat2 = lat2;
		this.db = DataBaseManager.getInstance();     
	    this.locations = this.findBusStops();
		
	} //FindBusStopsData endar
	
	public NearbyStopFinder (double gpsLon,double gpsLat) {
		lon1 = gpsLon - 0.008939;
		lon2 = gpsLon + 0.008939;
		lat1 = gpsLat - 0.004505;
		lat2 = gpsLat + 0.004505;
		this.db = DataBaseManager.getInstance();     
	    this.locations = this.findBusStops();
		
	} 
	
	public String[][] getBusStops() {
		return this.locations;
	}
	
	public String[][] findBusStops () {		
		Cursor c = db.doQuery("SELECT ShortName, _id, long,lat FROM busStop WHERE lat BETWEEN "+
				(Double.toString(lat1)+" and "+Double.toString(lat2))+
				" and long BETWEEN "+(Double.toString(lon1)+ " and "+ Double.toString(lon2))+ ";");
	String[][] locations = new String[c.getCount()][4];	
	int n=0;
	c.moveToFirst(); 
	while (!c.isAfterLast()){
    	locations[n][0]= c.getString(0); //Name
    	locations[n][1]= c.getString(1); //_id
    	locations[n][2]= c.getString(2); //long
    	locations[n][3]= c.getString(3); //lat
    	c.moveToNext();
    	n++;
	} 
	c.close(); 
		
	return locations;
	}//getBusStops ends
	
	public String[] findBuses (String busStopId) {
		Cursor c = db.doQuery("SELECT DISTINCT Route_id FROM StopsAT WHERE BusStop_id = "+
				busStopId+" and DayType=1"+";");
		//Cursor c = db.doQuery("SELECT DISTINCT Route_id FROM StopsAT WHERE BusStop_id = 90000295 and DayType=1"+";");
		
		buses = new String[c.getCount()];
		int n = 0;
		c.moveToFirst();
		while (!c.isAfterLast()){
				buses[n] = c.getString(0);	
				c.moveToNext();
				n++;
			
		}
		c.close();
		
		return buses;
	}
}
