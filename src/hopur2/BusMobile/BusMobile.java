package hopur2.BusMobile;

import android.app.Activity;
import android.view.View;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BusMobile extends Activity implements OnItemClickListener {
	private static final String TAG = "BusMobile";
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Búum til tilvik af gagnagrunninum á símanum.
        DataBaseCreator dbCreator = new DataBaseCreator(this);
        dbCreator.createDataBase();
        dbCreator.close();
        
        //Opnum aðalvalmyndina.
        setContentView(R.layout.main);
        ListView lv = (ListView)findViewById(R.id.mainMenu);
        lv.setOnItemClickListener(this);
    }

	public void onItemClick(AdapterView<?> a, View v, int selection, long id) {
		if(selection == 0){
			Intent routesmenu = new Intent (this, hopur2.BusMobile.RoutesMenu.class);
		try {
			this.startActivity(routesmenu);
		}
		catch(ActivityNotFoundException e){
			e.toString();
		}
	}
		
		if(selection == 1){
	        Intent map = new Intent (this, hopur2.BusMobile.MapMenu.class);
			try {
				this.startActivity(map);
			}
			catch(ActivityNotFoundException e){
				e.toString();
			}
		}
		if(selection == 3){
	        Intent findBusStops = new Intent (this, hopur2.BusMobile.NearbyStopMenu.class);
			try {
				this.startActivity(findBusStops);
			}
			catch(ActivityNotFoundException e){
				e.toString();
			}
		}
		if(selection == 2){
	        Intent guide = new Intent (this, hopur2.BusMobile.GuideUI.class);
			try {
				this.startActivity(guide);
			}
			catch(ActivityNotFoundException e){
				e.toString();
			}
		}
			
		
	}

}
