package hopur2.BusMobile;

import java.util.Calendar;
import android.content.Context;
import android.database.Cursor;


public class BusStop {

	 
	String[] routes, nextbus, nameandloc;
	int[] coordinates;
	private int busstopID;

	
	
	public BusStop(Context context, int busstopID)
	{
		
		
	       
	    this.busstopID=busstopID;
	    this.routes = this.getRoutes();
	    this.nextbus = this.getNextBus();
	    this.nameandloc = this.getnameandloc();
	    
	}
	
	
	//finnur leiðir sem stoppa á þessari tilteknu stöð og setur þær í routes array	
	public String[] getRoutes()
	{
		DataBaseManager db = DataBaseManager.getInstance();
		
		String routesfyrirspurn="SELECT DISTINCT r.number, r.name "+
		"FROM busstop bs, stopsat sa, route r "+
		"WHERE bs._id="+Integer.toString(busstopID)+" AND sa.BusStop_id=bs._id AND r._id=sa.Route_id;";
		
		
		Cursor c = db.doQuery(routesfyrirspurn);
		String[] routes = new String[c.getCount()+2];
		c.moveToFirst();
		int n=0;
		
		while (!c.isAfterLast())
		{
		routes[n]=c.getString(0)+" "+c.getString(1);
		n++;
		c.moveToNext();
		}
		
		c.close();
		return routes;
	}

	//finnur nafn og hnit þessarar tilteknu stoppistöðvar
	public String[] getnameandloc()
	{
	DataBaseManager db = DataBaseManager.getInstance();
		
	String[] nameandloc = new String[3];
	
	String coordinatesfyrirspurn = "SELECT name, easting, northing from busstop where _id="+Integer.toString(busstopID)+";";
	
	Cursor c=db.doQuery(coordinatesfyrirspurn);
	c.moveToFirst();
	nameandloc[0]=c.getString(0);
	nameandloc[1]=c.getString(0);
	
	nameandloc[2]=c.getString(1);
	
	c.close();

	return nameandloc;	
	}
 
	//finnur næstu vagna sem stöðva á þessari stoppstöð
	public String[] getNextBus()
	{
		String endofline = System.getProperty("line.separator"); 
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DAY_OF_WEEK);
		int daytype=1;
	
		if (today ==1) {daytype=3;};
		if (today>1 && today< 6) {daytype=1;};
		if (today == 7) {daytype=2;};
		
		String nextbusfyrirspurn = 
			"SELECT DISTINCT r.number, r.name, strftime('%M', sa.firsttime ), strftime('%M','now'), sa.frequency "+
			"FROM busstop bs, stopsat sa, route r "+
			"WHERE bs._id="+Integer.toString(busstopID)+" AND sa.BusStop_id=bs._id AND r._id=sa.Route_id AND sa.DayType="+Integer.toString(daytype)+" AND time('now')>time(sa.firsttime) AND time('now')<time(sa.lasttime) "+ 
			"ORDER BY r._id;";
			
	
		//skilar töflu á forminu:
		// route number | route name | mínútu hluti firsttime | mínútuhluti núna | stopsat frequency
		
		DataBaseManager db = DataBaseManager.getInstance();
		Cursor c = db.doQuery(nextbusfyrirspurn);
		String[] nextbus = new String[(c.getCount()+2)];
		
		int n=0; 
		
		if (c.getCount()==0)
		{
			nextbus[0]="Strætó gengur ekki þessa stundina";
		}
		
	
		else
		{
			c.moveToFirst();
			
			 while (!c.isAfterLast())
			{
				
			int frequency = c.getInt(4);
			int now = c.getInt(3);
			int firsttime=c.getInt(2);
			int min=60;
			
			
			if (frequency==15)
				{
					int secondtime=(firsttime+15)%60;
					int thirdtime=(firsttime+30)%60;
					int fourthtime=(firsttime+45)%60;
					
			
					if ((firsttime-now)>1){min=firsttime-now;}
					if ((secondtime-now)>1 && (secondtime-now)<min){min=secondtime-now;}
					if ((thirdtime-now)>1 && (thirdtime-now)<min){min=thirdtime-now;}
					if ((fourthtime-now)>1 && (fourthtime-now)<min){min=fourthtime-now;}	
					
					if (min==60) {min=Math.min(Math.min(firsttime, secondtime), Math.min(thirdtime, fourthtime)); min = (60+min)-now;}

					nextbus[n]="Leið "+c.getString(0)+
					" kemur eftir "+Integer.toString(min)+" mínútur";
				}
			
			if (frequency==30)
				{
					int secondtime=(firsttime+30)%60;
					
					if ((firsttime-now)>1){min=firsttime-now;}
					if ((secondtime-now)>1 && (secondtime-now)<min){min=secondtime-now;}
					
					if (min==60) {min=Math.min(firsttime, secondtime); min=(60+min)-now;}
					nextbus[n]="Leið "+c.getString(0)+
					" kemur eftir "+Integer.toString(min)+" mínútur";
					
					
				}
			
				else
				{
				min=0;
				if (firsttime<now) {
					min=now-firsttime;
					nextbus[n]="Leið "+c.getString(0)+
					" kemur eftir "+Integer.toString(min)+" mínútur";
					
					} 
				else {
					min=(firsttime+frequency)-now;
					nextbus[n]="Leið "+c.getString(0)+" kemur eftir "+Integer.toString(min)+" mínútur";
					};
				
				
				
				
				}
				
				//nextbus[n]="Leið "+c.getString(0)+" ";
				//nextbus[n]=Integer.toString(today);
				n++;
				c.moveToNext();
			}
			
			
			
		}
		c.close();
		return nextbus;
	}
}
