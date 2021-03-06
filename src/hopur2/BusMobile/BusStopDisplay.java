package hopur2.BusMobile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Color;


public class BusStopDisplay extends Activity {

	//Biðstöð er klasi sem birtir upplýsingar fyrir eina tiltekna biðstöð

	private TextView nafn;
	static int busstopID; 

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
		t2.setOrientation(LinearLayout.VERTICAL);
		//t2.setBackgroundColor(Color.rgb(255, 250, 250));

		TableRow titlerow = (TableRow) findViewById(R.id.titlerow);
		int j=0;

		if (nextbus.length==2) //strætó ekki í umferð
		{


			//			TextView leidph = 
			TextView gengurekki = (TextView) findViewById(R.id.leid);
			gengurekki.setText("Strætó gengur ekki þessa stundina");
			//t2.addView(titlerow);
			//			gengurekki.setBackgroundColor(getResources().getColor(R.color.red));
			//			gengurekki.setTextColor(Color.rgb(0, 0, 0));
			//			TableRow naestivagnrow = new TableRow(this);
			//			naestivagnrow.addView(leidph);
			//			naestivagnrow.addView(gengurekki);
			//			naestivagnrow.setBackgroundColor(Color.rgb(255, 140, 0));
			//			t2.addView(naestivagnrow);
		}

		else
		{

			while (nextbus[j]!=null)
			{
				TextView leid = (TextView) findViewById(R.id.leid);
				leid.setPadding(15, 15, 15, 10);

				TextView dest= (TextView) findViewById(R.id.dest);
				dest.setPadding(15, 15, 15, 10);
				dest.setText("Áfangastaður");

				TextView min = (TextView) findViewById(R.id.min);
				min.setPadding(15, 15, 15, 10);
				min.setText("Kemur eftir");

				//			leid.setTextColor(Color.rgb(0,0,0));
				//			titlerow.addView(leid);
				//			dest.setTextColor(Color.rgb(0,0,0));
				//			titlerow.addView(dest);
				//			min.setTextColor(Color.rgb(0,0,0));
				//			titlerow.addView(min);

				//t2.addView(titlerow);
				final int routeID=Integer.parseInt(nextbus[j++]);

				TableRow naestivagnrow = new TableRow(this);
				Button leidin = new Button(this);
				leidin.setPadding(10, 15, 15, 10);	
				try {
					R.drawable d = new R.drawable();
					int id = d.getClass().getField("button"+nextbus[j++]).getInt(d);
					leidin.setBackgroundDrawable(getResources().getDrawable(id));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				OnClickListener row = new OnClickListener(){
					public void onClick(View v){
						getLeidarTafla(routeID); 
					}
				};
				leidin.setOnClickListener(row);

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

				//if ((liturint+2)%2==0){naestivagnrow.setBackgroundColor(Color.rgb(255, 140, 0));};
				t2.addView(naestivagnrow);
				liturint++;
			}
		}


	}

	public void getLeidarTafla(int RouteID) 
	{
		Intent intent = new Intent (this, hopur2.BusMobile.RouteDisplay.class);
		try {this.startActivity(intent);}
		catch(ActivityNotFoundException e){e.toString();}
		hopur2.BusMobile.RouteDisplay.RouteID = RouteID;
	}

}
