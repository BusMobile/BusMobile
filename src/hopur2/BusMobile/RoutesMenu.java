package hopur2.BusMobile;

import java.io.IOException;

import android.app.Activity;
import android.app.TabActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;

public class RoutesMenu extends TabActivity {
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routelayout);
		TabHost tabHost = getTabHost();  // The activity TabHost
		TabHost.TabSpec spec;

		Intent redIntent = new Intent().setClass(this, ColoredRoutes.class);
		redIntent.putExtra("hopur2.BusMobile.ColoredRoutes.COLOR",1);
		spec = tabHost.newTabSpec("red").setIndicator("Rauðar").setContent(redIntent);
		tabHost.addTab(spec);

		Intent greenIntent = new Intent().setClass(this, ColoredRoutes.class);
		greenIntent.putExtra("hopur2.BusMobile.ColoredRoutes.COLOR",2);
		spec = tabHost.newTabSpec("green").setIndicator("Grænar").setContent(greenIntent);
		tabHost.addTab(spec);

		Intent blueIntent = new Intent().setClass(this, ColoredRoutes.class);
		blueIntent.putExtra("hopur2.BusMobile.ColoredRoutes.COLOR",3);
		spec = tabHost.newTabSpec("blue").setIndicator("Bláar").setContent(blueIntent);
		tabHost.addTab(spec);


		//        DataBaseManager myDataBase = DataBaseManager.getInstance();
		//        Cursor c = myDataBase.doQuery("select number, name, _id from Route;");
		//        c.moveToFirst();
		//	    
		//		String[] LeidirNofn = new String[c.getCount()];
		//		final int[] LeidirID = new int[c.getCount()];
		//		int n=0;
		//		
		//	    while (!c.isAfterLast())
		//	    {
		//	    	String q = "Leið "+c.getString(0)+"\n"+" "+c.getString(1);
		//	    	LeidirNofn[n]= q;
		//	    	LeidirID[n]=c.getInt(2);
		//	    	c.moveToNext();
		//	    	n++;
		//	    };
		//	    
		//	    c.close();
		//
		//        lv =(ListView)findViewById(R.id.ListView01);
		//        
		//        ArrayAdapter<String> NofnAdapter = new ArrayAdapter<String>(this,R.layout.list_item, LeidirNofn);
		//        lv.setAdapter(NofnAdapter);
		//       
		//		lv.setOnItemClickListener(new OnItemClickListener(){
		//			public void onItemClick(AdapterView<?> a, View v, int position, long id) 
		//			{
		//				
		//				int RouteID = LeidirID[position];
		//				getLeidarTafla(RouteID);
		//				
		//				/* AlertDialog.Builder adb = new AlertDialog.Builder(BusMobile.this);
		//        		adb.setTitle("Leidarkerfi");
		//        		adb.setMessage("Selected Item is = "+lv.getItemAtPosition(position));
		//        		adb.setPositiveButton("Ok", null);
		//        		adb.show(); */
		//        	}
		//        });

	}
	public void getLeidarTafla(int RouteID) {
		Intent intent = new Intent (this, hopur2.BusMobile.LeidarTafla.class);
		try {this.startActivity(intent);}
		catch(ActivityNotFoundException e){e.toString();}
		hopur2.BusMobile.LeidarTafla.RouteID = RouteID;
	}
}


