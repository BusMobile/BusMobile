package hopur2.BusMobile;

import android.content.Context;
import android.database.Cursor;


public class Route {
	//databasehelper constructor
	
	String nafn; 
	String[][] tafla;
	String[] timabil; 
	private DataBaseManager db;
	private int dayType;
	public String dagur;
	private int routeID;
	private static final String TAG = "Route";
	
	public Route(Context context, int routeID,int dayType)
	{
 
	    this.dayType = dayType;
	    this.routeID = routeID;
	    
	    this.db = DataBaseManager.getInstance();
		this.timabil = this.getTimabil();
		this.nafn = this.getNafn();
		this.tafla = this.getLeidarTafla();
		if(dayType==3)
		{
			if(this.tafla!=null){
				this.dagur = "Sunnu- og helgidaga";
			}else{
				this.nafn = "Enginn akstur á sunnudögum og helgidögum. ";
				this.dagur = "";
			}
		}else if(dayType==2){

				if(this.tafla!=null)
					this.dagur = "Laugardaga";
				else{
					this.nafn = "Enginn akstur á laugardögum.";
					this.dagur ="";
				}
			

			
		}else{
			this.dagur = "Mánudaga - föstudaga";
		}
			
			
	}

	public String[] getTimabil()
	{
	 
		 String fyrirspurn = "Select StopsAT.firstTime, StopsAT.lastTime " 
		 					+"From StopsAT WHERE Route_id="+routeID
		 					+ " AND StopsAT.inTable=1 AND StopsAT.StopNumber=1 AND dayType ="+ dayType+ ";";
		 Cursor c = db.doQuery(fyrirspurn);
		
		//skilar töflunni: 
		//		firstTime	|	lastTime	
		// hver þverlína stendur fyrir einn hluta þverlínu í leiðarkerfi
		// 		06:42	|	18:12		
		 
		// býr til tímabils string array
		
		String[] timabil = new String[c.getCount()];
		
		int n=0;
		c.moveToFirst(); 
		while (!c.isAfterLast()){
			timabil[n]=c.getString(0)+" - "+c.getString(1); n++;
    		c.moveToNext();
		} 
		c.close(); 
		return timabil;
	}
	
	public String getNafn ( )
	{
		
		Cursor c = db.doQuery("select number, name from Route where Route._id="+Integer.toString(routeID)+";");
		c.moveToFirst();
		String nafn = "Leið "+c.getString(0)+": "+c.getString(1);
		c.close();
		return nafn;
	}
	
	public String[] getLeidirNofn()
	{
		//skilar nöfnum allra strætóleiða í string array
		Cursor c = db.doQuery("select number, name from Route;");
		
		c.moveToFirst();
		String[] LeidirNofn = new String[c.getCount()];
		int n=0;
		
	    while (!c.isAfterLast())
	    {
	    	String q = "Leið "+c.getString(0)+"\n"+" "+c.getString(1);
	    	LeidirNofn[n]= q;
	    	c.moveToNext();
	    	n++;
	    };
	    
	    c.close();
	    return LeidirNofn;
	}
	
	public String[][] getLeidarTafla()
	{
		String fjöldafyrirspurn = "SELECT MAX(count) FROM (SELECT COUNT(StopNumber) AS count " +
								"FROM StopsAT WHERE Route_id = "+Integer.toString(routeID)+
								" AND daytype = "+dayType +
								" GROUP BY StopNumber)";
		Cursor c= db.doQuery(fjöldafyrirspurn);
		c.moveToFirst();
		int count = c.getInt(0);
		//skilar string array me upplsingum fyrir leiartflu
		String LeidarTaflaFyrirspurn=
		"SELECT sa.StopNumber, bs.ShortName, sa.firstTime, sa.lastTime, sa.frequency "+
		"FROM StopsAT sa, busStop bs "+
		"WHERE sa.Route_id="+Integer.toString(routeID)+" AND dayType="+dayType+" AND sa.inTable=1 AND bs._id=sa.BusStop_id "+
		"ORDER BY sa.StopNumber, sa.firstTime; ";
		c.close();
		
		//sql fyrirspurn sem skilar 6 dlka tflu
		//	|	stopNumber	| 		bsname 	|	firstTime	|	lastTime	|	frequency

		
		c = db.doQuery(LeidarTaflaFyrirspurn);
		int columncount = c.getColumnCount();
		String[][] leidartafla=null;
		c.moveToFirst();
		if(count!=0){
			 leidartafla= new String[c.getCount()+1][10*(columncount+3)];
		
		
			int number, lastnumber,frequency;
			String name, firsttime, lasttime;
			lastnumber = 0;
			int dalkur = 0;
			int rod = -1;
			
			
			while(!c.isAfterLast()){
				number = c.getInt(0);
				name = c.getString(1);
				firsttime = c.getString(2);
				lasttime = c.getString(3);
				frequency = c.getInt(4);
				if(lastnumber!=number){
					dalkur = 0;
					rod++;
					leidartafla[rod][dalkur++] = name;
				}
				if(rod>0){
					int lastrow = rod - 1;
					String lastfirsttime = leidartafla[lastrow][dalkur];
					while(!isTime(lastfirsttime) && lastrow > 0)
					{
						lastrow--;
						lastfirsttime = leidartafla[lastrow][dalkur];
					}
					while(leidartafla[lastrow][dalkur+1]!=null&&isTime(lastfirsttime) && subtractTime(firsttime,lastfirsttime)> 30 )
					{
						leidartafla[rod][dalkur++] = "   ";
						lastfirsttime = leidartafla[lastrow][dalkur];
						while(!isTime(lastfirsttime)&&dalkur<6*(columncount+3)){
							leidartafla[rod][dalkur++] = "   ";
							lastfirsttime = leidartafla[lastrow][dalkur];
						}
						leidartafla[rod][dalkur++] = "   ";
						lastfirsttime = leidartafla[lastrow][dalkur];
					}
					int row = lastrow;
					while(row>=0&&isTime(lastfirsttime) && subtractTime(firsttime,lastfirsttime)<-60 )
					{
						for(int k = 0; k!=60/frequency+2;k++){
							int d = dalkur+k;
							String thiscolumn = leidartafla[row][d];
							leidartafla[row][d] = "   ";
							String nextcolumn = leidartafla[row][d+1];
							while(d<8*(columncount)+3 && thiscolumn !=null){
								leidartafla[row][d+1] = thiscolumn;
								d++;
								thiscolumn = nextcolumn;
								nextcolumn = leidartafla[row][d+1];
							}
						}
						if(row > 0) lastfirsttime = leidartafla[row-1][dalkur];
						row--;
					}

				}
				leidartafla[rod][dalkur++] = firsttime;
				if(!firsttime.equals(lasttime)){
					if(subtractTime(lasttime,firsttime)!=frequency){
						for(int k = 0;k<60/frequency;k++){
							leidartafla[rod][dalkur++] = ""+(k*frequency +timeToMinutes(firsttime))%60;
						}
					}
					leidartafla[rod][dalkur++] = lasttime;
				}
				
				c.moveToNext();
				lastnumber = number;
			}
		}
		return leidartafla;
	}

	
	private int timeToHours(String time){
		String hour = time.substring(0,2);
		if(hour.charAt(0)=='0')
		{
			hour = hour.substring(1);
		}
		return Integer.parseInt(hour);
	}
	private int timeToMinutes(String time){
		String minute = time.substring(3,5);
		if(minute.charAt(0)=='0')
		{
			minute =minute.substring(1);
		}
		return Integer.parseInt(minute);
	}
	private boolean isTime(String str){
		if(str == null)
			return false;
		else
			return str.matches("[0-2][0-9]:[0-5][0-9]");
	}
	private int subtractTime(String time1, String time2){
		int hours1 = timeToHours(time1);
		int min1 = timeToMinutes(time1);
		int hours2 = timeToHours(time2);
		int min2 = timeToMinutes(time2);
		return hours1*60+min1-hours2*60-min2;
	}
	
	
	
}
