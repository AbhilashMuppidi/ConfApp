package bah.conference.appliation.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import bah.conference.appliation.dataStructures.BioItem;
import bah.conference.appliation.dataStructures.BoothLocation;
import bah.conference.appliation.dataStructures.Corner;
import bah.conference.appliation.dataStructures.Exhibitor;
import bah.conference.appliation.dataStructures.NewsItem;
import bah.conference.appliation.dataStructures.ScheduleItem;
import bah.conference.appliation.dataStructures.ScheduleItem2;

public class Local {
	private static final int DATABASE_VERSION = 5;
	private static final String DATABASE_NAME = "confDatabase";
	private static final String DATABASE_META_DATA_TABLE_NAME = "metaData";
	private static final String DATABASE_META_DATA_TABLE_COLUMNNAME_VERSION = "version";
	private static final String DATABASE_META_DATA_TABLE_COLUMNTYPE_VERSION = "INTEGER NOT NULL";
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper{
		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		public void create(SQLiteDatabase db) {
			if(table_Exists(db,DATABASE_META_DATA_TABLE_NAME)){
				int version = get_Meta_Data_Table_Version(db);
				onUpgrade(db,version,DATABASE_VERSION);
			}
			else
				onUpgrade(db,0,DATABASE_VERSION);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion < newVersion){
				drop_Tables(db,this);
				create_Tables(db,this);
				insert_Meta_Data_Table(newVersion,db);
			}
		}
		private void excStatement(SQLiteDatabase db,String statement){
			db.execSQL(statement);
		}
		private void excStatements(SQLiteDatabase db,String[] statements){
			for(String statement : statements){
				db.execSQL(statement);
			}
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			create(db);
		}
	}
	public Local(Context c){
		this.ourContext = c;
		open();
	}
	public boolean isOpen(){
		return ourDatabase.isOpen();
	}
	public Local open(){
		this.ourHelper = new DbHelper(this.ourContext);
		this.ourDatabase = ourHelper.getWritableDatabase();
		ourHelper.create(ourDatabase);
		return this;
	}
	public void close() {
	    if (ourHelper != null) {
	        ourHelper.close();
	    }
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public  static boolean table_Exists(SQLiteDatabase ourDatabase,String tableName){
	    Cursor cursor = ourDatabase.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
	    if (!cursor.moveToFirst()){
	    	cursor.close();
	    	return false;
	    }else{
	    	int count = cursor.getInt(0);
	    	cursor.close();
	    	return count > 0;
	    }
	}
	public  static int get_Meta_Data_Table_Version(SQLiteDatabase ourDatabase){
	    Cursor cursor = ourDatabase.rawQuery("SELECT "+DATABASE_META_DATA_TABLE_COLUMNNAME_VERSION+" FROM "+DATABASE_META_DATA_TABLE_NAME, null);
	    if (!cursor.moveToFirst()){
	    	cursor.close();
	    	return -1;
	    }else{
	    	int version = cursor.getInt(0);
	    	cursor.close();
	    	return version;
	    }
	}
	public  static long update_Meta_Data_Table(double version,SQLiteDatabase ourDatabase){
		ContentValues contentValues = new ContentValues();
		contentValues.put(DATABASE_META_DATA_TABLE_COLUMNNAME_VERSION, version );
		String[] whereArgs = new String[]{""+version};
		String whereClause = DATABASE_META_DATA_TABLE_COLUMNNAME_VERSION + "=?";
		return ourDatabase.update(DATABASE_META_DATA_TABLE_NAME, contentValues, whereClause, whereArgs);
	}
	public  static int insert_Meta_Data_Table(int version,SQLiteDatabase ourDatabase){
		try{
			ContentValues contentValues = new ContentValues();
			contentValues.put(DATABASE_META_DATA_TABLE_COLUMNNAME_VERSION, version );
			return (int) ourDatabase.insert(DATABASE_META_DATA_TABLE_NAME,null,contentValues);
		}catch(SQLiteConstraintException e ){
			return -1;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	static HashMap<String,HashMap<String,String>> tables = new HashMap<String,HashMap<String,String>>();
	static HashMap<String,String> metaData = new HashMap<String,String>();
	static{
		metaData.put(DATABASE_META_DATA_TABLE_COLUMNNAME_VERSION, DATABASE_META_DATA_TABLE_COLUMNTYPE_VERSION);
		tables.put(DATABASE_META_DATA_TABLE_NAME, metaData);
	}
	/*static HashMap<String,String> scheduleItem = new HashMap<String,String>();
	static{
		scheduleItem.put("pk_id", "INTEGER NOT NULL PRIMARY KEY");
		scheduleItem.put("Day","INTEGER NOT NULL");
		scheduleItem.put("Date", "VARCHAR(20) NOT NULL");
		scheduleItem.put("StartTime","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem.put("EndTime","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem.put("Title","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem.put("Description","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem.put("Speaker","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem.put("AvatarURL","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem.put("Event_Type","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem.put("Location","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem.put("Favorited", "TINYINT(1) DEFAULT 0");
		scheduleItem.put("Notes","VARCHAR(100) NULL DEFAULT NULL");
		tables.put("ScheduleItem", scheduleItem);
	}*/
	static HashMap<String,String> news = new HashMap<String,String>();
	static{
		news.put("pk_id", "INTEGER NOT NULL PRIMARY KEY");
		news.put("Title", "VARCHAR(100) NOT NULL");
		news.put("Link","VARCHAR(100) NOT NULL");
		news.put("Description","VARCHAR(100) NOT NULL");
		news.put("Guid","VARCHAR(100) NOT NULL");
		news.put("Author","VARCHAR(100) NOT NULL");
		news.put("PubDate","TIMESTAMP NOT NULL");
		tables.put("News", news);
	}
	static HashMap<String,String> bio = new HashMap<String,String>();
	static{
		bio.put("pk_id", "INTEGER NOT NULL PRIMARY KEY");
		bio.put("Name", "VARCHAR(100) NOT NULL");
		bio.put("Title","VARCHAR(100) NOT NULL");
		bio.put("Bio","VARCHAR(10000) NOT NULL");
		bio.put("Image","VARCHAR(100) NOT NULL");
		tables.put("Bio", bio);
	}
	static HashMap<String,String> scheduleItem2 = new HashMap<String,String>();
	static{
		scheduleItem2.put("pk_id","INTEGER NOT NULL PRIMARY KEY");
		scheduleItem2.put("Begin","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem2.put("Sequence","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem2.put("DTEnd","DATETIME NOT NULL");
		scheduleItem2.put("UID","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem2.put("Description","VARCHAR(1000) NULL DEFAULT NULL");
		scheduleItem2.put("Summary","VARCHAR(1000) NULL DEFAULT NULL");
		scheduleItem2.put("DTStart","DATETIME NOT NULL");
		scheduleItem2.put("DTStamp","DATETIME NOT NULL");
		scheduleItem2.put("Location","VARCHAR(100) NULL DEFAULT NULL");
		scheduleItem2.put("End","VARCHAR(100) NULL DEFAULT null");
		scheduleItem2.put("Favorited", "TINYINT(1) DEFAULT 0");
		scheduleItem2.put("Active", "TINYINT(1) DEFAULT 1");
		scheduleItem2.put("Notes", "VARCHAR(1000) NULL DEFAULT NULL");
		scheduleItem2.put("Speaker", "VARCHAR(100) NULL DEFAULT NULL");
		tables.put("ScheduleItem2", scheduleItem2);
	}
	static HashMap<String,String> location = new HashMap<String,String>();
	static{
		location.put("pk_id", "INTEGER NOT NULL PRIMARY KEY");
		location.put("fk_boothid", "INTEGER NOT NULL");
		location.put("sequence", "INTEGER NOT NULL");
		location.put("x", "INTEGER NOT NULL");
		location.put("y", "INTEGER NOT NULL");
		tables.put("BoothLocation", location);
	}
	static HashMap<String,String> booth = new HashMap<String,String>();
	static{
		booth.put("pk_id", "INTEGER NOT NULL PRIMARY KEY");
		booth.put("boothname", "VARCHAR(100) NOT NULL");
		booth.put("mapname", "VARCHAR(100) NOT NULL");
		tables.put("Booth", booth);
	}
	static HashMap<String,String> exhibitor = new HashMap<String,String>();
	static{
		exhibitor.put("pk_id", "INTEGER NOT NULL PRIMARY KEY");
		exhibitor.put("boothname", "VARCHAR(100) NOT NULL");
		exhibitor.put("company", "VARCHAR(100) NOT NULL");
		tables.put("Exhibitor", exhibitor);
	}
	
	
	public  static String[] drop_Tables(SQLiteDatabase ourDatabase,DbHelper ourHelper){
		Iterator<String> tablesNames = tables.keySet().iterator();
		String tableName;
		String statement;
		while(tablesNames.hasNext()){
			tableName = tablesNames.next();
			statement = "DROP TABLE IF EXISTS `"+tableName+"`";
			ourHelper.excStatement(ourDatabase,statement);
		}
		return null;
	}
	public  static String[] create_Tables(SQLiteDatabase ourDatabase,DbHelper ourHelper){
		Iterator<String> tablesNames = tables.keySet().iterator();
		String tableName;
		String statement;
		while(tablesNames.hasNext()){
			tableName = tablesNames.next();
			HashMap<String,String> tableColumns = tables.get(tableName);
			Iterator<String> tableColumnNames = tableColumns.keySet().iterator();
			statement = "CREATE TABLE `"+tableName+"` (";
			boolean first = true;
			while(tableColumnNames.hasNext()){
				if(first)
					first = false;
				else
					statement += ",";
				String tableColumnName = tableColumnNames.next();
				String tableColumnType = tableColumns.get(tableColumnName);
				statement += "`"+tableColumnName+"` "+tableColumnType;
			}
			statement += ")";
			ourHelper.excStatement(ourDatabase,statement);
		}
		return null;
	}
	public ContentValues getContentValues(HashMap<String,Object> values){
		ContentValues contentValues = new ContentValues();
		Iterator<String> iterator = values.keySet().iterator();
		while(iterator.hasNext()){
			String name = iterator.next();
			Object value = values.get(name);
			if(value instanceof Boolean)
				contentValues.put(name, (Boolean)value);
			else if(value instanceof Integer)
				contentValues.put(name, (Integer)value);
			else if(value instanceof Long)
				contentValues.put(name, (Long)value);
			else if(value instanceof Double)
				contentValues.put(name, (Double)value);
			else if(value instanceof Float)
				contentValues.put(name, (Float)value);
			else if(value instanceof String){
				if(((String)value).equals("")){
					contentValues.putNull(name);
				}else
					contentValues.put(name, (String)value);
			}else{
				//?
			}
		}
		return contentValues;
	}
	/*public Cursor getAll(String tableName,long id){
		Cursor cursor;
		String statement = "SELECT * FROM "+tableName+" WHERE Day=?";
		cursor = ourDatabase.rawQuery(statement, new String[]{""+id});
		return cursor;
	}*/
	/*public Cursor getAll(String tableName){
		Cursor cursor;
		String statement = "SELECT * FROM "+tableName;
		cursor = ourDatabase.rawQuery(statement, null);
		return cursor;
	}*/
	public  long insertRow(String tableName, HashMap<String,Object> values){
		ContentValues contentValues = getContentValues(values);
		if(contentValues!=null)
			return ourDatabase.insert(tableName,null,contentValues);
		else
			return -1;
	}
	public  void wipeTable(String tableName){
		ourDatabase.delete(tableName, null, null);
	}
	public  boolean adjustFavorites(ScheduleItem2 scheduleItem, boolean value){
		ContentValues contentValues = new ContentValues();
		contentValues.put("Favorited", value ? 1:0 );
		String[] whereArgs = new String[]{""+scheduleItem.pk_id};
		String whereClause = "pk_id=?";
		return ourDatabase.update("ScheduleItem2", contentValues, whereClause, whereArgs)>0;
	}

	public  boolean adjustNotes(ScheduleItem2 scheduleItem, String content){
		ContentValues contentValues = new ContentValues();
		contentValues.put("Notes", content);
		String[] whereArgs = new String[]{""+scheduleItem.pk_id};
		String whereClause = "pk_id=?";
		return ourDatabase.update("ScheduleItem2", contentValues, whereClause, whereArgs)>0;
	}

	public  boolean setNotes(ScheduleItem2 scheduleItem){
		String statement = "SELECT * FROM Notes WHERE fk_id=?";
		Cursor cursor = ourDatabase.rawQuery(statement, new String[]{""+scheduleItem.pk_id});
		boolean is = false;
		for (boolean hasItem = cursor.moveToFirst(); hasItem;) {
			is = true;
		}
		cursor.close();
		return is;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  ScheduleItem
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public  ArrayList<ScheduleItem2> getFavoriteTimeSlots(int day){
		HashMap<Integer,String> dayQ = new HashMap<Integer,String>();
		dayQ.put(1,"SELECT * FROM ScheduleItem2 WHERE DTStart > '2012-10-08 T 00:00:00Z' AND DTEnd < '2012-10-09 T 00:00:00Z'"+" AND Favorited=1 ORDER BY DTStart");
		dayQ.put(2,"SELECT * FROM ScheduleItem2 WHERE DTStart > '2012-10-09 T 00:00:00Z' AND DTEnd < '2012-10-10 T 00:00:00Z'"+" AND Favorited=1 ORDER BY DTStart");
		dayQ.put(3,"SELECT * FROM ScheduleItem2 WHERE DTStart > '2012-10-10 T 00:00:00Z' AND DTEnd < '2012-10-11 T 00:00:00Z'"+" AND Favorited=1 ORDER BY DTStart");
		dayQ.put(4,"SELECT * FROM ScheduleItem2 WHERE DTStart > '2012-10-11 T 00:00:00Z' AND DTEnd < '2012-10-12 T 00:00:00Z'"+" AND Favorited=1 ORDER BY DTStart");
		ArrayList<ScheduleItem2> scheduleItems = new ArrayList<ScheduleItem2>();
		if(dayQ.containsKey(day)){
			Cursor cursor = ourDatabase.rawQuery(dayQ.get(day), null);
			for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
				ScheduleItem2 aAct = new ScheduleItem2(cursor);
				scheduleItems.add(aAct);
			}
			cursor.close();
		}
		return scheduleItems;
	}

	public  ScheduleItem2 getSchedule2Item(ScheduleItem2 scheduleItem){
		String statement = "SELECT * FROM ScheduleItem2 WHERE pk_id="+scheduleItem.pk_id + " ORDER BY DTStart";
		Cursor cursor = ourDatabase.rawQuery(statement, null);
		ScheduleItem2 result = null;
		for (boolean hasItem = cursor.moveToFirst(); hasItem;) {
			result= new ScheduleItem2(cursor);
			break;
		}
		cursor.close();
		return result;
	}

	public  ArrayList<ScheduleItem2> getSchedule2Now(String currentDateandTime) {
		
		String statement = "SELECT * FROM ScheduleItem2 WHERE DTEnd > '"+currentDateandTime+"' AND DTStart < '"+currentDateandTime+"' ORDER BY DTStart";
		Cursor cursor = ourDatabase.rawQuery(statement, null);
		ArrayList<ScheduleItem2> scheduleItems = new ArrayList<ScheduleItem2>();
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
			ScheduleItem2 aAct = new ScheduleItem2(cursor);
			scheduleItems.add(aAct);
		}
		cursor.close();
		return scheduleItems;
	}
	public  ArrayList<ScheduleItem2> getSchedule2Previous(String currentDateandTime){
		String statement = "SELECT * FROM ScheduleItem2 WHERE DTEnd < '"+currentDateandTime+"' ORDER BY DTStart";
		Cursor cursor = ourDatabase.rawQuery(statement, null);
		ArrayList<ScheduleItem2> scheduleItems = new ArrayList<ScheduleItem2>();
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
			ScheduleItem2 aAct = new ScheduleItem2(cursor);
			scheduleItems.add(aAct);
		}
		cursor.close();
		return scheduleItems;
	}
	public  ArrayList<ScheduleItem2> getSchedule2Next(String currentDateandTime){
		String statement = "SELECT * FROM ScheduleItem2 WHERE DTStart > '"+currentDateandTime+"' ORDER BY DTStart";
		Cursor cursor = ourDatabase.rawQuery(statement, null);
		ArrayList<ScheduleItem2> scheduleItems = new ArrayList<ScheduleItem2>();
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
			ScheduleItem2 aAct = new ScheduleItem2(cursor);
			scheduleItems.add(aAct);
		}
		cursor.close();
		return scheduleItems;
	}
	
	
	public  ArrayList<ScheduleItem2> getSchedule2TimeSlots(int day){
		HashMap<Integer,String> dayQ = new HashMap<Integer,String>();
		dayQ.put(1,"SELECT * FROM ScheduleItem2 WHERE DTStart > '2012-10-08 T 00:00:00Z' AND DTEnd < '2012-10-09 T 00:00:00Z' ORDER BY DTStart");
		dayQ.put(2,"SELECT * FROM ScheduleItem2 WHERE DTStart > '2012-10-09 T 00:00:00Z' AND DTEnd < '2012-10-10 T 00:00:00Z' ORDER BY DTStart");
		dayQ.put(3,"SELECT * FROM ScheduleItem2 WHERE DTStart > '2012-10-10 T 00:00:00Z' AND DTEnd < '2012-10-11 T 00:00:00Z' ORDER BY DTStart");
		dayQ.put(4,"SELECT * FROM ScheduleItem2 WHERE DTStart > '2012-10-11 T 00:00:00Z' AND DTEnd < '2012-10-12 T 00:00:00Z' ORDER BY DTStart");
		ArrayList<ScheduleItem2> scheduleItems = new ArrayList<ScheduleItem2>();
		if(dayQ.containsKey(day)){
			Cursor cursor = ourDatabase.rawQuery(dayQ.get(day), null);
			for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
				ScheduleItem2 aAct = new ScheduleItem2(cursor);
				scheduleItems.add(aAct);
			}
			cursor.close();
		}
		return scheduleItems;
	}
	public  ArrayList<ScheduleItem2> getSchedule2TimeSlots(){
		ArrayList<ScheduleItem2> scheduleItems = new ArrayList<ScheduleItem2>();
		Cursor cursor = ourDatabase.rawQuery("SELECT * FROM ScheduleItem2", null);
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
			ScheduleItem2 aAct = new ScheduleItem2(cursor);
			scheduleItems.add(aAct);
		}
		cursor.close();
		return scheduleItems;
	}	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  News
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public  ArrayList<NewsItem> getNews(){
		ArrayList<NewsItem> news = new ArrayList<NewsItem>();
		String statement = "SELECT * FROM News ORDER BY PubDate DESC";
		Cursor cursor = ourDatabase.rawQuery(statement, null);
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
			NewsItem newsItem = new NewsItem(cursor);
			news.add(newsItem);
		}
		cursor.close();
		return news;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	BioItem
////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public  BioItem getBioItem(String name){
		BioItem bioItem = null;
		if(name!=null){
			String statement = "SELECT * FROM Bio WHERE Name=?";
			Cursor cursor = ourDatabase.rawQuery(statement, new String[]{name});
			for (boolean hasItem = cursor.moveToFirst(); hasItem;) {
				bioItem = new BioItem(cursor);
				break;
			}
			cursor.close();
		}
		return bioItem;
	}
	
	public  ArrayList<BioItem> getBioItems(){
		ArrayList<BioItem> bioItems = new ArrayList<BioItem>();
		String statement = "SELECT * FROM Bio";
		Cursor cursor = ourDatabase.rawQuery(statement, null);
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
			bioItems.add(new BioItem(cursor));
		}
		cursor.close();
		return bioItems;
	}
	
	/*
	 * 		location.put("pk_id", "INTEGER NOT NULL PRIMARY KEY");
		location.put("fk_boothid", "INTEGER NOT NULL");
		location.put("sequence", "INTEGER NOT NULL");
		location.put("x", "INTEGER NOT NULL");
		location.put("y", "INTEGER NOT NULL");
	 * 
	 */
	public  void insertBooth(BoothLocation booth){
		long a = ourDatabase.insert("Booth",null,booth.toContentValues());
		if(a > 0){
			ArrayList<Corner> corners = booth.getCorners();
			for(int i = 0; i < corners.size();i++){
				Corner corner = corners.get(i);
				ContentValues c = new ContentValues();
				c.put("x", corner.x);
				c.put("y", corner.y);
				c.put("sequence",i);
				c.put("fk_boothid", a);      
				long b = ourDatabase.insert("BoothLocation",null,c);
				if(b < 0)
					Log.wtf("ZOMG", "BLAAAAAAAARRRRG");
			}
		}
		else
			Log.wtf("ZOMG ZOMG", "BLARRRG BLARRRG");
		
	}
	public  ArrayList<BoothLocation> getBooths(){
		ArrayList<BoothLocation> booths = new ArrayList<BoothLocation>();
		String statement = "SELECT * FROM BoothLocation";
		Cursor cursor = ourDatabase.rawQuery(statement, null);
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
			BoothLocation boothLoc = new BoothLocation(cursor);
			ArrayList<Corner> corners = getABoothsCorners(boothLoc);
			for(Corner corner : corners){
				boothLoc.addPoint(corner.x, corner.y);
			}
			booths.add(boothLoc);
		}
		cursor.close();
		return booths;
	}
	public  ArrayList<Corner> getABoothsCorners(BoothLocation booth){
		ArrayList<Corner> corners = new ArrayList<Corner>();
		String statement = "SELECT * FROM BoothLocation WHERE fk_boothid=? ORDER BY sequence ASC";
		Cursor cursor = ourDatabase.rawQuery(statement, new String[]{""+booth.pk_id});
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
			corners.add(new Corner(cursor));
		}
		cursor.close();
		return corners;
	}
	public  ArrayList<Corner> getAallBoothsCorners(){
		ArrayList<Corner> corners = new ArrayList<Corner>();
		String statement = "SELECT * FROM BoothLocation";
		Cursor cursor = ourDatabase.rawQuery(statement, null);
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
			corners.add(new Corner(cursor));
		}
		cursor.close();
		return corners;
	}
	public  ArrayList<BoothLocation> getBoothClicked(int x, int y){
		String statement = "SELECT fk_boothid FROM BoothLocation where x>? AND x<? AND y>? AND y <? ORDER BY fk_boothid";//  
		
		String statement2 = "SELECT * FROM (Booth)a INNER JOIN ("+statement+")b on b.fk_boothid = a.pk_id";
		int xa = x+150;
		int xb = x-150;
		int ya = y+150;
		int yb = y-150;
		Cursor cursor = ourDatabase.rawQuery(statement2, new String[]{""+xb,""+xa,""+yb,""+ya});

		ArrayList<BoothLocation> booths = new ArrayList<BoothLocation>();
		for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
			BoothLocation boothLoc = new BoothLocation(cursor);
			ArrayList<Corner> corners = getABoothsCorners(boothLoc);
			for(Corner corner : corners){
				boothLoc.addPoint(corner.x, corner.y);
			}
			booths.add(boothLoc);
		}
		cursor.close();
		return booths;
	}
	public  Exhibitor getExhibitor(BoothLocation boothLocation){
		String statement = "SELECT * FROM Exhibitor WHERE boothname=?";
		Cursor cursor = ourDatabase.rawQuery(statement, new String[]{""+boothLocation.boothName});
		Exhibitor exhibitor = null;
		for (boolean hasItem = cursor.moveToFirst(); hasItem;) {
			exhibitor = new Exhibitor(cursor);
			break;
		}
		cursor.close();
		return exhibitor;
	}
	public  boolean isPopulated(String tableName){
		String statement = "SELECT * FROM "+tableName + " LIMIT 1";
		Cursor cursor = ourDatabase.rawQuery(statement,null);
		boolean populated = false;
		for (boolean hasItem = cursor.moveToFirst(); hasItem;) {
			populated = true;
			break;
		}
		cursor.close();
		return populated;
	}
	public void startTransaction(){
		ourDatabase.beginTransaction();
	}
	public void endTransaction(){
		ourDatabase.setTransactionSuccessful();
		ourDatabase.endTransaction();
	}
	public  ArrayList<Exhibitor> getExhibitors(){
		String statement = "SELECT * FROM Exhibitor";
		Cursor cursor = ourDatabase.rawQuery(statement, null);
		ArrayList<Exhibitor> exhibitors = new ArrayList<Exhibitor>();
		for (boolean hasItem = cursor.moveToFirst(); hasItem;hasItem = cursor.moveToNext()) {
			exhibitors.add(new Exhibitor(cursor));
		}
		cursor.close();
		return exhibitors;
	}
	
}
