package hopur2.BusMobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {
	private SQLiteDatabase db;
    private static final DataBaseManager INSTANCE = new DataBaseManager();
	private static final String DB_PATH =DataBaseCreator.DB_PATH;
	private static final String DB_NAME =DataBaseCreator.DB_NAME;
	private static final String TAG = "DataBaseManager";
	
	private DataBaseManager(){
		
	}
	public static DataBaseManager getInstance()
	{
		return INSTANCE;
	}
	public Cursor doQuery(String sql)
	{
		this.db = SQLiteDatabase.openDatabase(DB_PATH+DB_NAME, null,SQLiteDatabase.OPEN_READONLY);
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		db.close();
		return c;
	}
	

}
