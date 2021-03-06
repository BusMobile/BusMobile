package hopur2.BusMobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.ExpandableListView.OnChildClickListener;

public class LandmarkMenu extends Activity implements OnChildClickListener{
	private ExpandableListView lv;
	private String[] landmarksHeiti;
	private  List<List<Map>> childList;
	private boolean isFrom;
	private String fromString,toString;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.landmarkmenu);
		
		isFrom = getIntent().getBooleanExtra("hopur2.BusMobile.LandmarkMenu.IS_FROM", true);
		 fromString = getIntent().getStringExtra("hopur2.BusMobile.GuideUI.FROM");
		 toString = getIntent().getStringExtra("hopur2.BusMobile.GuideUI.TO");

		lv =(ExpandableListView)findViewById(R.id.ListView01);
		HashMap<String,String> landmarkMap= new HashMap<String,String>();
		landmarksHeiti = this.getResources().getStringArray(R.array.landmark_array);
		childList = createChildList();
		SimpleExpandableListAdapter expListAdapter =
			new SimpleExpandableListAdapter(
				this,
				createGroupList(),	// groupData describes the first-level entries
				R.layout.group_item,	// Layout for the first-level entries
				new String[] { "landmarkGroup" },	// Key in the groupData maps to display
				new int[] { R.id.landmark },		// Data under "colorName" key goes into this TextView
				(List<? extends List<? extends Map<String, ?>>>) childList,	// childData describes second-level entries
				R.layout.list_item,	// Layout for second-level entries
				new String[] { "landmark" },	// Keys in childData maps to display
				new int[] { R.id.landmark }	// Data under the keys above go into these TextViews
			);
		lv.setAdapter( expListAdapter );
		lv.setOnChildClickListener(this);
	}
	

	@SuppressWarnings( "unchecked" )
	private List createGroupList() {
		ArrayList result = new ArrayList();
		for( int i = 0 ; i < landmarksHeiti.length ; ++i ) {
			HashMap m = new HashMap();
			m.put( "landmarkGroup",landmarksHeiti[i] );
			result.add( m );
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private List createChildList() {
		ArrayList<List<Map>> result = new ArrayList<List<Map>>();
			// Second-level lists
		
			ArrayList<Map> secList = new ArrayList<Map>();
			String[] landmarks = this.getResources().getStringArray(R.array.bokasofn);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap child = new HashMap();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
			secList = new ArrayList();
			landmarks= this.getResources().getStringArray(R.array.felagsmidstodvar);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap child = new HashMap();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
			secList = new ArrayList();
			landmarks = this.getResources().getStringArray(R.array.ithrottamannvirki);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap child = new HashMap();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
			secList = new ArrayList();
			landmarks= this.getResources().getStringArray(R.array.kirkjur);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap child = new HashMap();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
			secList = new ArrayList();
			landmarks= this.getResources().getStringArray(R.array.menning_og_sofn);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap child = new HashMap();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
			secList = new ArrayList();
			landmarks= this.getResources().getStringArray(R.array.skolar);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap child = new HashMap();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
			secList = new ArrayList();
			landmarks= this.getResources().getStringArray(R.array.spitalar_og_hjukrunarheimili);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap child = new HashMap();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
			secList = new ArrayList();
			landmarks= this.getResources().getStringArray(R.array.straeto);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap child = new HashMap();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
			secList = new ArrayList();
			landmarks= this.getResources().getStringArray(R.array.sundlaugar);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap child = new HashMap();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
			secList = new ArrayList();
			landmarks = this.getResources().getStringArray(R.array.verslunarkjarnar);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap<String, String> child = new HashMap<String, String>();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
			secList = new ArrayList();
			landmarks = this.getResources().getStringArray(R.array.ymislegt);
			for(int j = 0; j!=landmarks.length;j++){
				HashMap child = new HashMap();
				child.put( "landmark", landmarks[j] );
				secList.add( child );
			}
			result.add( secList );
		return result;
	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		String value = childList.get(groupPosition).get(childPosition).get("landmark").toString();

		Intent guide = new Intent (this, hopur2.BusMobile.GuideUI.class);
		if(isFrom){
			guide.putExtra("hopur2.BusMobile.GuideUI.FROM",value);
			guide.putExtra("hopur2.BusMobile.GuideUI.TO",toString);
		}
		else
		{
			guide.putExtra("hopur2.BusMobile.GuideUI.TO",value);
			guide.putExtra("hopur2.BusMobile.GuideUI.FROM",fromString);
		}
		try {
			this.startActivity(guide);
		}
		catch(ActivityNotFoundException e){
			e.toString();
		}
		return false;
	}

}
