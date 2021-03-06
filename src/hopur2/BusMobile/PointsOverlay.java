package hopur2.BusMobile;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class PointsOverlay extends ItemizedOverlay {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public PointsOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	public PointsOverlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	public void addOverlays(OverlayItem[] overlays) {
		for(int i = 0; i!=overlays.length;i++)
		{
			this.addOverlay(overlays[i]);
		}
	}
	
	public void setContext(Context c)
	{
		mContext = c;
	}
	
	public void addRoute(int routeNumber){
		String sql = "SELECT DISTINCT b.shortname, b.lat, b.long,b._id "
					+" FROM busstop b, stopsat s, route r "
					+" WHERE b._id = s.busstop_id AND r._id = s.route_id "
					+" AND r.number ="+routeNumber+";";
		DataBaseManager dbm = DataBaseManager.getInstance();
		Cursor c = dbm.doQuery(sql);
		c.moveToFirst();
		while(!c.isAfterLast()){
			GeoPoint gp = new GeoPoint((int) (Double.parseDouble(c.getString(1))*1e6),(int) (Double.parseDouble(c.getString(2))*1e6));
			this.addOverlay(new OverlayItem(gp,c.getString(0),c.getString(3)));
			c.moveToNext();
		}
		c.close();
	}
	
	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  String name = item.getTitle();
	  String id = item.getSnippet();
	  String sql = "SELECT DISTINCT r.number "+
	  			   " FROM busstop b, stopsat s, route r "+
	  			   " WHERE b._id = s.busstop_id AND r._id = s.route_id "+
	  			   " AND b._id="+id+";";
	  DataBaseManager dbm = DataBaseManager.getInstance();
	  Cursor c = dbm.doQuery(sql);
	  c.moveToFirst();
	  String snippet = c.getString(0);
	  c.moveToNext();
	  while(!c.isAfterLast()){
		  snippet = snippet +", "+ c.getString(0);
		  c.moveToNext();
	  }
	  c.close();
	  dialog.setTitle(name);
	  dialog.setMessage(snippet);
	  dialog.show();
	  return true;
	}

}
