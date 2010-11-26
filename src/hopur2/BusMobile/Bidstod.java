package hopur2.BusMobile;

import android.app.Activity;
import android.os.Bundle;


import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Color;


public class Bidstod extends Activity {

	private TextView nafn, herstoppa;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bidstodlayout);
		
		BusStop thisstop = new BusStop(this, 10000802);
		
		
		nafn =(TextView)findViewById(R.id.stoppstodnafn);
		nafn.setText(thisstop.nameandloc[0]);
		
//		herstoppa =(TextView)findViewById(R.id.leidirtextview);
//		herstoppa.setText("Eftirfarandi leiðir stöðva hér:");
		
		
		String[] nextbus = thisstop.nextbus;
		String[] routes = thisstop.routes;
		
        
        int liturint = 0; 
        int j=0; 
//        
//        TableLayout t1 = (TableLayout)findViewById(R.id.leidirtable);
//		t1.setOrientation(TableLayout.VERTICAL);
//        t1.setBackgroundColor(Color.rgb(255, 250, 250));
//        
        //bætir leiðum á töflu fyrir stoppstöð
//        while (routes[j]!=null)
//        {
//        	TableRow stoppstodrow = new TableRow(this);
//        	
//        	
//        	TextView leid = new TextView(this);
//        	leid.setTextColor(Color.rgb(0,0,0));
//        	leid.setText(routes[j]);
//        	stoppstodrow.addView(leid);
//        	
//        	if ((liturint+2)%2==0){stoppstodrow.setBackgroundColor(Color.rgb(255, 140, 0));};
//        	t1.addView(stoppstodrow);
//        	
//        	
//        	liturint++;
//        	j++;
//        }
		
        TextView naestuvagnar = (TextView)findViewById(R.id.naestuvagnartextview);
		naestuvagnar.setText("Næstu vagnar á þessari stöð");
		
		TableLayout t2 = (TableLayout)findViewById(R.id.naestuvagnartable);
		t2.setOrientation(TableLayout.VERTICAL);
        t2.setBackgroundColor(Color.rgb(255, 250, 250));
		
        TableRow titlerow = new TableRow(this);
        TextView leid = new TextView(this);
    	leid.setText("Leið");
        TextView dest= new TextView(this);
    	dest.setText("Áfangastaður");
        TextView min = new TextView(this);
    	min.setText("Kemur eftir");
    	
    	titlerow.addView(leid);
    	titlerow.addView(dest);
    	titlerow.addView(min);
    	
    	t2.addView(titlerow);

		j=0;
		while (nextbus[j]!=null)
        {
        	TableRow naestivagnrow = new TableRow(this);

        	
        	TextView leidin = new TextView(this);
        	leidin.setTextColor(Color.rgb(0,0,0));
        	leidin.setText(nextbus[j++]);
        	naestivagnrow.addView(leidin);

        	TextView destin = new TextView(this);
        	destin.setTextColor(Color.rgb(0,0,0));
        	destin.setText(nextbus[j++]);
        	naestivagnrow.addView(destin);
        	
        	TextView minin = new TextView(this);
        	minin.setTextColor(Color.rgb(0,0,0));
        	minin.setText(nextbus[j++]);
        	naestivagnrow.addView(minin);
        	
        	
        	if ((liturint+2)%2==0){naestivagnrow.setBackgroundColor(Color.rgb(255, 140, 0));};
        	t2.addView(naestivagnrow);
        	
        	liturint++;
        }
		

	}

}
