package hopur2.BusMobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.text.Html;

public class WebGuideParser  {
    private String results;
    private String subheading,tblhead1,tblhead2;
    private ArrayList<String[]> table1,table2;
    
    
    public WebGuideParser(String url){
    	try {
    		URL guide = new URL(url);
    		InputStream in = guide.openStream();
    		BufferedReader breader = new BufferedReader(new InputStreamReader(in));
    		String source = "";
    		String line;
    		String endOn ="<form class=\"leit inresult\" name=\"radgjafi\" method=\"get\" action=\"/leit\">";
    		while ((line = breader.readLine()) != null&&!endOn.equals(line.trim())) {
    			source+=line;
    		}
    		if(line!=null)
    			source = source + "</body></html>";

    		in.close();
    		breader.close();
    		
    		results = Html.fromHtml(source).toString();

    	} catch (MalformedURLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	int start =results.lastIndexOf("Leitarniðurstöður");
        int end = results.indexOf("Leitin skilaði");
        subheading = results.substring(start+17,end).trim();
        
        String[] suggestions = results.split("Tillaga [0-9]: ");
        if(suggestions.length>1){
        	end = suggestions[1].indexOf("Sýna Kort");
			
			String heading = (suggestions[1].substring(0, end));
			tblhead1 = heading.replaceAll("\n", " ");
			String tblbody = suggestions[1].substring(end+9).trim();
			table1 = new ArrayList<String[]>();
			int index = 0;
			for(int i = 0; i<tblbody.length()-5;i++)
			{
				String region = tblbody.substring(i,i+5);
				if(region.matches("[0-9][0-9]:[0-9][0-9]")){
					String[] tblrow = new String[4];
					tblrow[0] = "  ";
					tblrow[1] = tblbody.substring(index, i).trim();
					tblrow[2] = region;
					String rest = tblbody.substring(i+5).trim();
					if(rest.startsWith("Gengið")){
						index= rest.indexOf("metra")+5;
						tblrow[3] = rest.substring(0,index);
						index = index+i+5;
					}
					if(rest.startsWith("Leið")){
						index = 7;
						tblrow[3] = rest.substring(0,index);
						index = index+i+5;
					}
					table1.add(tblrow);
				}
			}
			String[] row = table1.get(0);
			row[0] = "Frá:";
			row[1] = row[1].replace("Frá:", "");
			row = table1.get(table1.size()-1);
			row[0] = "Til:";
			row[1] = row[1].replace("Til:", "");
        }
        if(suggestions.length>2){
        	end = suggestions[2].indexOf("Sýna Kort");
			
			String heading = (suggestions[2].substring(0, end));
			tblhead2 = heading.replaceAll("\n", " ");
			String tblbody = suggestions[2].substring(end+9).trim();
			table2 = new ArrayList<String[]>();
			int index = 0;
			for(int i = 0; i<tblbody.length()-5;i++)
			{
				String region = tblbody.substring(i,i+5);
				if(region.matches("[0-9][0-9]:[0-9][0-9]")){
					String[] tblrow = new String[4];
					tblrow[0] = "  ";
					tblrow[1] = tblbody.substring(index, i).trim();
					tblrow[2] = region;
					String rest = tblbody.substring(i+5).trim();
					if(rest.startsWith("Gengið")){
						index= rest.indexOf("metra")+5;
						tblrow[3] = rest.substring(0,index);
						index = index+i+5;
					}
					if(rest.startsWith("Leið")){
						index = 7;
						tblrow[3] = rest.substring(0,index);
						index = index+i+5;
					}
					table2.add(tblrow);
				}
			}
			String[] row = table2.get(0);
			row[0] = "Frá:";
			row[1] = row[1].replace("Frá:", "");
			row = table2.get(table2.size()-1);
			row[0] = "Til:";
			row[1] = row[1].replace("Til:", "");
        }

    }
    
	
    public String getSubHeading(){
        return subheading;
    }
    
    public String getTblHead1(){
    	return tblhead1;
    }
    public String getTblHead2(){
    	return tblhead2;
    }
    public ArrayList<String[]> getTable1(){
    	return table1;
    }
    public ArrayList<String[]> getTable2(){
    	return table2;
    }

    

}
