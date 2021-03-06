package hopur2.BusMobile;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RouteOverlay extends Overlay {
	private GeoPoint[] gps;
	private int defaultColor;

	public RouteOverlay(GeoPoint[] gps) // GeoPoint is a int. (6E)
	{
		this.gps = gps;


	}



	public RouteOverlay(GeoPoint[] gps,int defaultColor) // GeoPoint is a int. (6E)
	{
		this.gps = gps;
		this.defaultColor = defaultColor;

	}
	

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
	{
		Projection projection = mapView.getProjection();
		if (shadow == false && gps!=null)
		{
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(defaultColor);
			Point[] pts = new Point[gps.length];
			for(int i = 0; i!=gps.length;i++){
				pts[i]=projection.toPixels(gps[i], null);
			}
			float[] ptsd = new float[2*pts.length];
			for(int i = 0;i<ptsd.length-1;i=i+2){
				ptsd[i] = pts[i/2].x;
				ptsd[i+1]=pts[i/2].y;
			}
			paint.setStrokeWidth(5);
			paint.setAlpha(120);
			canvas.drawLines(ptsd, paint);
		}
		return super.draw(canvas, mapView, shadow, when);
	}

}
