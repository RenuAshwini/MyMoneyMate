/* *************************************************************************************
Copyright � 2013 Deepika Punyamurtula

This program is free software: you can redistribute it and/or modify it under 
the terms of the GNU General Public License as published by the Free Software Foundation, 
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. 
If not, see http://www.gnu.org/licenses/.

Author - Deepika Punyamurtula
email: udeepika@pdx.edu

MyMoneyMate - An android application to keep a record of your expenses.
***************************************************************************************** */
package com.example.moneymeterexample;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Expenses.db";
	private static final String TABLE_EXPENSES = "Expenses";
	
	private static final String KEY_ID = "_id";
	private static final String KEY_CATEGORY = "category";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_DATE = "date";
	Context c;
    private ArrayList<ExpenseEntry> exList = new ArrayList<ExpenseEntry>();
    
    public DataBaseHelper(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		c = context;
		// TODO Auto-generated constructor stub
	}
    public String getTableName(){
    	return DataBaseHelper.TABLE_EXPENSES;
    }
    
    public String getDBName(){
    	return DataBaseHelper.DATABASE_NAME;
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String query = "CREATE TABLE " + TABLE_EXPENSES + "(" + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_CATEGORY + " TEXT, " +
				KEY_AMOUNT + " INTEGER NOT NULL, " + KEY_DATE + " TEXT);" ;
				db.execSQL(query);
		
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_EXPENSES);
		onCreate(db);
		
	}
	
	public void addExpenseEntry(ExpenseEntry e){
		 SQLiteDatabase db = this.getWritableDatabase();
		 ContentValues contentValues = new ContentValues();
		 contentValues.put("amount", e.amount);
		 contentValues.put("date", e.date);
		 contentValues.put("category", e.category);
		 db.insert(TABLE_EXPENSES,null,contentValues);
		 db.close();
	}
	
	public ArrayList<ExpenseEntry> getExpenses(){
		exList.clear();
		SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " ORDER BY date ", null);
       
        if(cursor.getCount()!=0){
        	if (cursor.moveToFirst()) {
                do {
                	ExpenseEntry ee = new ExpenseEntry();
                	ee._id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                	ee.amount = cursor.getInt(cursor.getColumnIndex(KEY_AMOUNT));
                	ee.category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                	ee.date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
                	exList.add(ee);
                }while(cursor.moveToNext());
        }
	}
        cursor.close();
        db.close();
        return exList;
}

}
