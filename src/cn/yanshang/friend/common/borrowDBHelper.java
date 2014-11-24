package cn.yanshang.friend.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class borrowDBHelper extends SQLiteOpenHelper {

	 private static final int VERSION = 1;
	 private final static String DATABASE_NAME = "borrowFriend.db"; 
	 
	 private final static String TABLE_NAME = "reason_table"; 
	 public final static String _ID = "_id"; 
	 public final static String IS_CURRENT = "is_current"; 
	 public final static String R_MONEY = "r_money"; 
	 public final static String R_DATE = "r_date"; 
	 public final static String R_INTEREST = "r_interest";
	 public final static String R_DEADLINE = "r_deadline";
	 
	public borrowDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + _ID 
				+ " INTEGER primary key autoincrement, " + IS_CURRENT + " text, "+R_MONEY + " text, "
				+R_DATE + " text, "+R_INTEREST + " text, "+ R_DEADLINE +" text);"; 
				db.execSQL(sql); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME; 
		db.execSQL(sql); 
		onCreate(db); 
	}

	public Cursor select() { 
		SQLiteDatabase db = this.getReadableDatabase(); 
		Cursor cursor = db 
		.query(TABLE_NAME, null, null, null, null, null, null); 
		return cursor; 
		}
	
	public long insert(String bookname,String author) 
	{ 
	SQLiteDatabase db = this.getWritableDatabase(); 
	/* ContentValues */
	ContentValues cv = new ContentValues(); 
	//cv.put(BOOK_NAME, bookname); 
	//cv.put(BOOK_AUTHOR, author); 
	long row = db.insert(TABLE_NAME, null, cv); 
	return row; 
	} 
	
	public void delete(int id) 
	{ 
//	SQLiteDatabase db = this.getWritableDatabase(); 
//	String where = BOOK_ID + " = ?"; 
//	String[] whereValue ={ Integer.toString(id) }; 
//	db.delete(TABLE_NAME, where, whereValue); 
	}
	
	public void update(int id, String bookname,String author) 
	{ 
	SQLiteDatabase db = this.getWritableDatabase(); 
//	String where = BOOK_ID + " = ?"; 
//	String[] whereValue = { Integer.toString(id) }; 
//	 
//	ContentValues cv = new ContentValues(); 
//	cv.put(BOOK_NAME, bookname); 
//	cv.put(BOOK_AUTHOR, author); 
//	db.update(TABLE_NAME, cv, where, whereValue); 
	} 
}



















