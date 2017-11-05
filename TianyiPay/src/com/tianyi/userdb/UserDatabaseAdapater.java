package com.tianyi.userdb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDatabaseAdapater extends UserDatabaseParameter
{
	private DatabaseHelper	helper	= null;
	private SQLiteDatabase	db		= null;
	private Context			context	= null;

	public UserDatabaseAdapater(Context context)
	{
		this.context = context;
		helper = new DatabaseHelper(this.context);
	}

	/*public void deleteRowByRowid(long rowid)
	{
		String sql = "delete from " + TABLE_USER + " where "
				+ UserDatabaseParameter.COLUMN_NAME_ID + " = " + rowid;
		doSql(sql);
	}*/

	public void deleteRowByUsername(String username)
	{
		String sql = "delete from " + TABLE_USER + " where "
				+ UserDatabaseParameter.COLUMN_USER_NAME + " = "
				+ "\""+username+"\"";
		doSql(sql);
	}

	/*public void updateUserInfoByRowid(long rowid, String tablename, String colname,
			String colvalue)
	{
		String sql = "update " + tablename + " set " + colname + "=" + "\""
				+ colvalue + "\"" + " where "
				+ UserDatabaseParameter.COLUMN_NAME_ID + " = " + rowid;
		doSql(sql);
	}*/

	public void updateUserByUsername(String tablename, String colname,
			String colvalue, String username)
	{
		String sql = "update " + tablename + " set " + colname + "=" + "\""
				+ colvalue + "\"" + " where "
				+ UserDatabaseParameter.COLUMN_USER_NAME + " = " + "\""+username+"\"";
		doSql(sql);
	}
	
	private void doSql(String sql)
	{
		try
		{
			db = helper.getWritableDatabase();
			db.beginTransaction();
			db.execSQL(sql);
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
		}
		finally
		{
			db.endTransaction();
			db.close();
			helper.close();
		}
	}

	static class DatabaseHelper extends SQLiteOpenHelper
	{

		public DatabaseHelper(Context context)
		{
			super(context, DATABASE_USER, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(CREATE_TABLE_FILTER);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
		}

	}

	public void insertRow(UserInfo userInfo)
	{
		String sql = "insert into " + TABLE_USER 
				+ " values(null"
				+ ", \""+ userInfo.getUsername() + "\""
				+ ", \""+ userInfo.getPassword()  + "\""
				+ ", \""+ userInfo.getAccesstoken() + "\""
				+ ", \""+ userInfo.getBindedPhoneNumber() + "\""
				+ ", \""+ userInfo.isIsremember() + "\""
				+ ", \""+ userInfo.isIslatestlogin() + "\");";
		doSql(sql);
	}
	
	public UserInfo getUserWhichIsLastestLogin()
	{
		UserInfo userInfo = null;

		String sql = "select * from " + TABLE_USER+" where "
				+ UserDatabaseParameter.COLUMN_IS_LATEST_LOGIN_ACCOUNT + " = \"1\"";
		db = helper.getReadableDatabase();
		Cursor cursor=null;
		
		cursor = db.rawQuery(sql, null);
		if (cursor == null)
		{
			close(cursor, db, helper);
			return null;
		}
		if (cursor.getCount() == 0)
		{
			close(cursor, db, helper);
			return null;
		}

		cursor.moveToFirst();
		int index = 1;
		userInfo = new UserInfo();
		userInfo.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_USER_NAME)));
		userInfo.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_PASSWORD)));
		userInfo.setAccesstoken(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_ACCESSTOKEN)));
		userInfo.setBindedPhoneNumber(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_BINDEDNUM)));
		userInfo.setIsremember(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_REMEMBER_PASSWORD)));
		userInfo.setIslatestlogin(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_IS_LATEST_LOGIN_ACCOUNT)));

		close(cursor, db, helper);
		Log.i("user name ", userInfo.getUsername());
		return userInfo;
	}
	
	public UserInfo getUserByusername(String username)
	{
		UserInfo userInfo = null;

		String sql = "select * from " + TABLE_USER+" where "
				+ UserDatabaseParameter.COLUMN_USER_NAME + " = \""+username+"\"";
		db = helper.getReadableDatabase();
		Cursor cursor=null;
		
		cursor = db.rawQuery(sql, null);
		if (cursor == null)
		{
			close(cursor, db, helper);
			return null;
		}
		if (cursor.getCount() == 0)
		{
			close(cursor, db, helper);
			return null;
		}

		cursor.moveToFirst();
		int index = 1;
		userInfo = new UserInfo();
		userInfo.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_USER_NAME)));
		userInfo.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_PASSWORD)));
		userInfo.setAccesstoken(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_ACCESSTOKEN)));
		userInfo.setBindedPhoneNumber(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_BINDEDNUM)));
		userInfo.setIsremember(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_REMEMBER_PASSWORD)));
		userInfo.setIslatestlogin(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_IS_LATEST_LOGIN_ACCOUNT)));

		close(cursor, db, helper);
		Log.i("user name ", userInfo.getUsername());
		return userInfo;
	}
	
	public List<UserInfo> getUserList()
	{
		List<UserInfo> userlist = null;
		UserInfo userInfo = null;

		String sql = "select * from " + TABLE_USER;
		db = helper.getReadableDatabase();
		Cursor cursor=null;
		
		cursor = db.rawQuery(sql, null);
		if (cursor == null)
		{
			close(cursor, db, helper);
			return null;
		}
		if (cursor.getCount() == 0)
		{
			close(cursor, db, helper);
			return null;
		}

		cursor.moveToFirst();
		userlist = new ArrayList<UserInfo>();
		do
		{
			int index = 1;
			userInfo = new UserInfo();
			userInfo.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_USER_NAME)));
			userInfo.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_PASSWORD)));
			userInfo.setAccesstoken(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_ACCESSTOKEN)));
			userInfo.setBindedPhoneNumber(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_BINDEDNUM)));
			userInfo.setIsremember(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_REMEMBER_PASSWORD)));
			userInfo.setIslatestlogin(cursor.getString(cursor.getColumnIndex(UserDatabaseParameter.COLUMN_IS_LATEST_LOGIN_ACCOUNT)));

			userlist.add(userInfo);
		} while (cursor.moveToNext());

		close(cursor, db, helper);
		Log.i("user account ", userlist.size()+"");
		return userlist;
	}
	
	private void close(Cursor cursor, SQLiteDatabase db, DatabaseHelper helper)
	{
		if(cursor!=null)
			cursor.close();
		if(db!=null)
			db.close();
		if(helper!=null)
			helper.close();
	}
}