package hopur2.BusMobile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;



public class DataBaseCreator extends SQLiteOpenHelper{
	private final Context myContext;
	public static final String DB_PATH ="data/data/hopur2.BusMobile/databases/";
	public static final String DB_NAME ="BusMobile";
	private static String TAG = "DataBaseCreator";
	
	//databasehelper constructor
	public DataBaseCreator(Context context)
	{
		super(context, DB_NAME, null, 1);
		this.myContext = context; 
	}
	
	
	public void createDataBase(){
		boolean dbExist = checkDatabase();
		if (!dbExist){
			this.getReadableDatabase();
			try{
				copyDatabase();
			} catch(IOException e){
				Log.v(TAG,"Error copying database to device.");
			}
		}
		
	}
	
	private void copyDatabase() throws IOException{
		InputStream  myInput = myContext.getAssets().open(DB_NAME);
		String outFile = DB_PATH+DB_NAME;
		OutputStream myOutput= new FileOutputStream(outFile);
		byte[] buffer = new byte[1024];
		int length;
		while((length=myInput.read(buffer))>0)
		{
			myOutput.write(buffer, 0,length);
		}
		myOutput.flush();
		myInput.close();
		myOutput.close();
	}
	
	private boolean checkDatabase() {
		SQLiteDatabase checkDb=null;
		try{
			String myPath=DB_PATH+DB_NAME;
			checkDb = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
		}catch(SQLiteException e){
		
		}
		
		if(checkDb !=null)
		{
			checkDb.close();
		}
		return checkDb!=null ? true:false;
		}
	
	@Override
	public void onCreate(SQLiteDatabase DB)
	{
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	//todo method
	}

}



