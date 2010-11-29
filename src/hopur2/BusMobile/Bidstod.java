package hopur2.BusMobile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Color;


public class Bidstod extends Activity {
	
	//Biðstöð er klasi sem birtir upplýsingar fyrir eina tiltekna biðstöð

	private TextView nafn;
	private static int busstopID; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bidstodlayout);

		
		BusStop thisstop = new BusStop(this, busstopID);

		nafn =(TextView)findViewById(R.id.stoppstodnafn);
		nafn.setText(thisstop.nameandloc[0]);

		String[] nextbus = thisstop.nextbus;

		int liturint = 0; 

		TableLayout t2 = (TableLayout)findViewById(R.id.naestuvagnartable);
		t2.setOrientation(TableLayout.VERTICAL);
		t2.setBackgroundColor(Color.rgb(255, 250, 250));



		TableRow titlerow = new TableRow(this);

		TextView leid = new TextView(this);
		leid.setPadding(15, 15, 15, 10);
		leid.setText("Leið");

		TextView dest= new TextView(this);
		dest.setPadding(15, 15, 15, 10);
		dest.setText("Áfangastaður");

		TextView min = new TextView(this);
		min.setPadding(15, 15, 15, 10);
		min.setText("Kemur eftir");

		leid.setTextColor(Color.rgb(0,0,0));
		titlerow.addView(leid);
		dest.setTextColor(Color.rgb(0,0,0));
		titlerow.addView(dest);
		min.setTextColor(Color.rgb(0,0,0));
		titlerow.addView(min);

		t2.addView(titlerow);

		int j=0;

		if (nextbus.length==2) //strætó ekki í umferð
		{
			TextView leidph = new TextView(this); 
			TextView gengurekki = new TextView(this);
			gengurekki.setText("Strætó gengur ekki þessa stundina");
			gengurekki.setTextColor(Color.rgb(0, 0, 0));
			TableRow naestivagnrow = new TableRow(this);
			naestivagnrow.addView(leidph);
			naestivagnrow.addView(gengurekki);
			naestivagnrow.setBackgroundColor(Color.rgb(255, 140, 0));
			t2.addView(naestivagnrow);
		}
		
		else
		{
		while (nextbus[j]!=null)
		{
			final int routeID=Integer.parseInt(nextbus[j++]);

			TableRow naestivagnrow = new TableRow(this);
			Button leidin = new Button(this);
			leidin.setPadding(10, 15, 15, 10);


			OnClickListener row = new OnClickListener(){public void onClick(View v){getLeidarTafla(routeID); }};
			leidin.setOnClickListener(row);

			leidin.setTextColor(Color.rgb(0,0,0));
			leidin.setText(nextbus[j++]);
			naestivagnrow.addView(leidin);


			TextView destin = new TextView(this);
			destin.setTextColor(Color.rgb(0,0,0));
			destin.setText(nextbus[j++]);
			destin.setPadding(10, 15, 15, 10);
			naestivagnrow.addView(destin);

			TextView minin = new TextView(this);
			minin.setTextColor(Color.rgb(0,0,0));
			minin.setPadding(10, 15, 25, 10);
			minin.setText(nextbus[j++]);

			naestivagnrow.addView(minin);
			naestivagnrow.setOnClickListener(row);

			if ((liturint+2)%2==0){naestivagnrow.setBackgroundColor(Color.rgb(255, 140, 0));};
			t2.addView(naestivagnrow);
			liturint++;
		}
		}


	}

	public void getLeidarTafla(int RouteID) 
	{
		Intent intent = new Intent (this, hopur2.BusMobile.LeidarTafla.class);
		try {this.startActivity(intent);}
		catch(ActivityNotFoundException e){e.toString();}
		hopur2.BusMobile.LeidarTafla.RouteID = RouteID;
	}

}
