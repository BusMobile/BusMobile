package hopur2.BusMobile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RouteDisplay extends Activity {

	public static int RouteID;
	private static final String TAG = "LeidarTafla";
	private Route[] routes;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.leidartaflalayout);
        
        routes = new Route[3];


        Button maps = (Button) findViewById(R.id.kort);
        maps.setText("Sjá kort");        
        OnClickListener mapsButtonListener = new OnClickListener(){
        	public void onClick(View V)
        	{
        		openMap();
        	}
        };
        maps.setOnClickListener(mapsButtonListener); 
		//bætir degi og korta takka í efstu röð töflu
        
        routes[0] = new Route(this,RouteID,1);
        TextView dagurtv = (TextView)findViewById(R.id.virkirdagar);
        dagurtv.setText(routes[0].dagur);        
        
        //bætir nafni í næst efstu röð fyrir ofan töflu
        
        TextView nafntv= (TextView)findViewById(R.id.nafnvirkir);        
        nafntv.setText(routes[0].nafn);

        routes[1] = new Route(this,RouteID,2);
        TextView laugartv = (TextView)findViewById(R.id.laugardagar);
        laugartv.setText(routes[1].dagur);        
        
        //bætir nafni í næst efstu röð fyrir ofan töflu
        
       
        routes[2] = new Route(this,RouteID,3);
        TextView helgitv = (TextView)findViewById(R.id.helgidagar);
        helgitv.setText(routes[2].dagur);        
        
        //bætir nafni í næst efstu röð fyrir ofan töflu
        
        
        int id = R.id.taflavirkir;
        makeTable(1,id);
        
        id = R.id.taflalaugar;
        makeTable(2,id);
        
        id = R.id.taflahelgi;
        makeTable(3,id);
        
        //LayoutParams P = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        
        //ScrollView sv = (ScrollView)findViewById(R.id.myScrollview);
        //HorizontalScrollView hv = (HorizontalScrollView)findViewById(R.id.myHView);
        
        
        //this.setContentView(t1);
        
        //sv.addView(hv);
        //hv.addView(t1);   
	
	}
	private void makeTable(int dayType,int id)
	{
		Route route = routes[dayType-1];
		if (route.tafla!=null){
	        TableLayout t1 = (TableLayout)findViewById(id);
	        t1.setOrientation(LinearLayout.VERTICAL);
	        t1.setBackgroundColor(Color.rgb(255, 250, 250));
	       
	        /*TableRow timabilrow = new TableRow(this);
	        timabilrow.setLayoutParams(P); 
	        
	        TextView stod = new TextView(this);
	        stod.setText("tímabil ");
	        timabilrow.addView(stod);
	
	        TextView fyrstaferd = new TextView(this);
	        stod.setText("fyrsta ferð");
	        timabilrow.addView(fyrstaferd);
	        
	        TextView sidastaferd = new TextView(this);
	        stod.setText("fyrsta ferð");
	        timabilrow.addView(sidastaferd);
	        
	        int i=0;
	        while (i!=route.timabil.length)
	        {
	        	TextView k = new TextView(this);
	        	k.setText(route.timabil[i]);  
	        	timabilrow.addView(k);
	        	i++;
	        
	        }
	        t1.addView(timabilrow); */
	        
		        
		        int j=0;
		        int k=0; 
		        int liturint = 0;
		        
		       while (route.tafla[j][k]!=null) {
		        	TableRow stoppstodrow = new TableRow(this);
		        	
		        	while (route.tafla[j][k]!=null)
		        	{
		        		
		        		TextView gildi = new TextView(this);
		        		if(k==0){gildi.setWidth(120);}
		        		else{gildi.setWidth(60);};
		        		
		        		gildi.setTextColor(Color.rgb(0,0,0));
		        		gildi.setText(route.tafla[j][k]);
		       
		        		
		        		stoppstodrow.addView(gildi);
		        		k++;
		        	}
		        	k=0; 
		        	
		        	
		        	t1.addView(stoppstodrow);
		        	
		        	if ((liturint+2)%2==0){stoppstodrow.setBackgroundColor(Color.rgb(255, 140, 0));};
		        	liturint++;
		        	j++;
		 
		       } 
		}
	}

	private  void openMap(){
		Intent map = new Intent(this,hopur2.BusMobile.MapDisplay.class);
		map.putExtra("hopur2.BusMobile.MapDisplay.ROUTE_NUMBER", RouteID/10);
		try {
			this.startActivity(map);
		}
		catch(ActivityNotFoundException e){
			e.toString();
		}
	}

}
