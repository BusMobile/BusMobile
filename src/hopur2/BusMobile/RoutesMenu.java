package hopur2.BusMobile;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class RoutesMenu extends TabActivity {
	private TabHost tabHost ;  // The activity TabHost
	private TabHost.TabSpec spec;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablayout);
		tabHost = getTabHost();
		
		
		setupTab("Rauðar",1);
		setupTab("Grænar",2);
		setupTab("Bláar",3);
				

	}
	private void setupTab(String tag,int color){
		View tabView = LayoutInflater.from(this).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) tabView.findViewById(R.id.tabsText);
		tv.setText(tag);

		Intent tabIntent = new Intent().setClass(this, ColoredRoutes.class);
		tabIntent.putExtra("hopur2.BusMobile.ColoredRoutes.COLOR",color);
		spec = tabHost.newTabSpec(tag).setIndicator(tabView).setContent(tabIntent);
		tabHost.addTab(spec);
	}

}


