package hopur2.BusMobile;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import 	android.util.Log;


public class Route {
	//databasehelper constructor
	
	String nafn; 
	String[][] tafla;
	String[] timabil; 
	private SQLiteDatabase db;
	private int dayType;
	public String dagur;
	private int routeID;
	private static final String TAG = "MyActivity";
	
	public Route(Context context, int routeID,int dayType)
	{
       	String dbPath = context.getDatabasePath("BusMobile").getPath();  
 
		DataBaseHelper dbh = new DataBaseHelper(context);
	       
	        try{
	        	//if(!dbh.checkDatabase()){
	        		dbh.getReadableDatabase();
	        		dbh.copyDatabase();
	        	//}
	        }catch(IOException IOe){}
	          
	    
	    this.dayType = dayType;
	    this.routeID = routeID;
	    
	    this.db = SQLiteDatabase.openDatabase(dbPath, null,SQLiteDatabase.OPEN_READONLY);
		this.timabil = this.getTimabil();
		this.nafn = this.getNafn();
		this.tafla = this.getLeidarTafla();
		if(dayType==3)
		{
			this.dagur = "Sunnu- og helgidaga";
		}else if(dayType==2){
			this.dagur = "Laugardaga";
		}else{
			this.dagur = "Mánudaga - föstudaga";
		}
			
			
		db.close();
	}

	public String[] getTimabil()
	{
	 
		 String fyrirspurn = "Select StopsAT.firstTime, StopsAT.lastTime " 
		 					+"From StopsAT WHERE Route_id="+routeID
		 					+ " AND StopsAT.inTable=1 AND StopsAT.StopNumber=1 AND dayType ="+ dayType+ ";";
		 Cursor c = db.rawQuery(fyrirspurn, null);
		
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
		
		Cursor c = db.rawQuery("select number, name from Route where Route._id="+Integer.toString(routeID)+";", null);
		c.moveToFirst();
		String nafn = "Leið "+c.getString(0)+": "+c.getString(1);
		c.close();
		return nafn;
	}
	
	public String[] getLeidirNofn()
	{
		//skilar nöfnum allra strætóleiða í string array
		Cursor c = db.rawQuery("select number, name from Route;", null);
		
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
		//skilar string array með upplýsingum fyrir leiðartöflu
		
		String LeidarTaflaFyrirspurn=
		"SELECT sa.StopNumber, bs.ShortName, sa.firstTime, strftime('%M',sa.firstTime), sa.lastTime, sa.frequency "+
		"FROM StopsAT sa, busStop bs "+
		"WHERE sa.Route_id="+Integer.toString(routeID)+" AND dayType="+dayType+" AND sa.inTable=1 AND bs._id=sa.BusStop_id "+
		"ORDER BY sa.StopNumber, sa.firstTime; ";
		
		//sql fyrirspurn sem skilar 6 dálka töflu
		//	|	stopNumber	| 		bsname 	|	firstTime	|	strftime('%M',sa.firstTime)	|	lastTime	|	frequency

		
		Cursor c = db.rawQuery(LeidarTaflaFyrirspurn, null);
		int columncount = c.getColumnCount();
		String[][] LeidarTafla = new String[c.getCount()+1][10*columncount];
		
		c.moveToFirst();
		
		int rod=-1;
		int dalkur=0; 
		int laststopnumber=0;
		int offset=0;
		while (!c.isAfterLast())
		{
			int thisstopnumber = c.getInt(0);
			int frequency = c.getInt(columncount-1);
			if (laststopnumber != thisstopnumber)
			{
				offset=0;
				rod++;
				for (int j=0;j!=(columncount-2);j++)
				{
						
						if(j==2)
						{
							int minutes = c.getInt(3);
							int k;
							for(k = 0; k!=60/frequency;k++)
							{
								LeidarTafla[rod][j+k] = Integer.toString((minutes+k*frequency)%60);
							}
							offset = offset+k-1;
							
						}
						else {
							LeidarTafla[rod][j+offset]=c.getString(j+1);
						}
					
				};
				dalkur=columncount-1;
			}
			else
			{
				for (int j=2;j!=columncount-1;j++)
				{
					if(j==3){
						int minutes = c.getInt(3);
						int k;
						for(k = 0; k!=60/frequency;k++)
						{
							LeidarTafla[rod][dalkur+offset+k-1] = ""+(minutes+k*frequency)%60;
						}
						offset = offset+k-1;
					}
					else{
						LeidarTafla[rod][dalkur+offset-1]=c.getString(j);
						Log.v(TAG, ""+rod+" "+dalkur+" "+offset+ " ");
						
					}
					dalkur++;
				}
				
			}
			laststopnumber=thisstopnumber;
			c.moveToNext();
			
			
		}
		
		c.close();
		return LeidarTafla;
	}
	
	
	
}
