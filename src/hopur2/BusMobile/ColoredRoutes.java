package hopur2.BusMobile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ColoredRoutes extends Activity {
	private ListView lv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routesmenu);

		int color = this.getIntent().getIntExtra("hopur2.BusMobile.ColoredRoutes.COLOR", 1);
		DataBaseManager myDataBase = DataBaseManager.getInstance();
		Cursor c = myDataBase.doQuery("select number, name, _id from Route WHERE color="+color+";");
		c.moveToFirst();

		String[] LeidirNofn = new String[c.getCount()];
		final int[] LeidirID = new int[c.getCount()];
		int n=0;

		while (!c.isAfterLast())
		{
			String q = "Leið "+c.getString(0)+"\n"+" "+c.getString(1);
			LeidirNofn[n]= q;
			LeidirID[n]=c.getInt(2);
			c.moveToNext();
			n++;
		};

		c.close();
		lv =(ListView)findViewById(R.id.ListView01);

		ArrayAdapter<String> NofnAdapter = new ArrayAdapter<String>(this,R.layout.list_item, LeidirNofn);
		lv.setAdapter(NofnAdapter);

		lv.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> a, View v, int position, long id) 
			{

				int RouteID = LeidirID[position];
				getLeidarTafla(RouteID);

			}
		});
	}
	
	public void getLeidarTafla(int RouteID) {
		Intent intent = new Intent (this, hopur2.BusMobile.RouteDisplay.class);
		try {this.startActivity(intent);}
		catch(ActivityNotFoundException e){e.toString();}
		hopur2.BusMobile.RouteDisplay.RouteID = RouteID;
	}

}
