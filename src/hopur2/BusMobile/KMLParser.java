package hopur2.BusMobile;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import org.w3c.dom.Element;

import com.google.android.maps.GeoPoint;
import android.util.Log;

//Klasi sem nær í leiðarupplýsingar úr kml-skrá til að geta teiknað leiðina á kort.

public class KMLParser {
	private Context myContext;
	private Resources res;
	private Document doc;
	
	public KMLParser(Context c, int routeNumber){
		this.myContext = c;
		this.res = myContext.getResources();
		
		
		
		R.raw r = new R.raw();
		
		
		InputStream instream;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			instream = res.openRawResource(r.getClass().getField("leid"+routeNumber).getInt(r));
			db = dbf.newDocumentBuilder();
			this.doc = db.parse(instream);
		} catch (NotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalArgumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SecurityException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (NoSuchFieldException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public GeoPoint[] parseRoute()
	{
		NodeList nodes = doc.getElementsByTagName("LineString");
        String path="";
		for(int s=0; s<nodes.getLength() ; s++){

        	Log.v("xxx",""+s);
			Node firstNode = nodes.item(s);
            if(firstNode.getNodeType() == Node.ELEMENT_NODE){
            	
            	Element firstElement = (Element)firstNode;

                //-------
                NodeList coordinatesList = firstElement.getElementsByTagName("coordinates");
                Element coordinatesElement = (Element)coordinatesList.item(0);

                NodeList coordinatesText = coordinatesElement.getChildNodes();
                path =path+coordinatesText.item(0).getNodeValue();
            }//end of if clause

        }//end of for loop with s var
        String[] coordinates = path.split(",0.000000"); //each coordinate is lon,lat,0.000000
        int numPoints = coordinates.length-1; //the last token in coordinates is empty because the last coordinate ends in 0.000000
        GeoPoint[] pts = new GeoPoint[2*(numPoints)-2];//want to draw lines from point 0 to point 1, point 1 to point 2,...
    	Log.v("xxx",""+numPoints);
        //first point once
        String[] lngLat = coordinates[0].split(",");
        // watch out! For GeoPoint, first:latitude, second:longitude
        pts[0] = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
        //middle points twice
        int i = 1;
        while(i<2*numPoints-3) // the last one would be crash
        {
        	lngLat = coordinates[i/2+1].split(",");
	        // watch out! For GeoPoint, first:latitude, second:longitude
	        pts[i++] = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
	        pts[i++] = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
        }
        //last point once
        lngLat = coordinates[numPoints-1].split(",");
        pts[2*numPoints-3] = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
        return pts;
	}
	
}
