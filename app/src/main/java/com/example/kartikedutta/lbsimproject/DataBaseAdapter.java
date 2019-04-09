
package com.example.kartikedutta.lbsimproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseAdapter {

    private DbHelper helper;

    public DataBaseAdapter(Context context) {
        helper = new DbHelper(context);
    }

    /*public long insertNgoData(String name,String address)
    {
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbHelper.NGO_NAME,name);
        contentValues.put(DbHelper.NGO_ADDRESS,address);
        long id=db.insert(DbHelper.NGO_TABLE_NAME,null,contentValues);
        return id;
    }*/
    public long insertCompanyData(String name, String address) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COMPANY_NAME, name);
        contentValues.put(DbHelper.COMPANY_ADDRESS, address);
        long id = db.insert(DbHelper.COMPANY_TABLE_NAME, null, contentValues);
        return id;
    }

    public long insertJobData(String title, String description, String salary) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.JOB_TITLE, title);
        contentValues.put(DbHelper.JOB_DESCRIPTION, description);
        long id = db.insert(DbHelper.STARRED_TABLE_NAME, null, contentValues);
        return id;
    }
    //Get Data methods

    /*public String getNgoData()
    {
        //select ngo_id,name,address;
        SQLiteDatabase db=helper.getWritableDatabase();
        String[] columns={DbHelper.NGO_UID,DbHelper.NGO_NAME,DbHelper.NGO_ADDRESS};
        Cursor cursor=db.query(DbHelper.NGO_TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer=new StringBuffer();
        while (cursor.moveToNext())
        {
            int index1=cursor.getColumnIndex(helper.NGO_UID);
            int index2=cursor.getColumnIndex(helper.NGO_NAME);
            int index3=cursor.getColumnIndex(helper.NGO_ADDRESS);
            String ngo_id=cursor.getString(index1);
            String ngo_name=cursor.getString(index2);
            String ngo_add=cursor.getString(index3);
            buffer.append(ngo_id+" "+ngo_name+" "+ngo_add+"\n");
        }
        return buffer.toString();

    }*/
    public String getStarredJobsData() {
        //select ngo_id,name,address;
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DbHelper.JOB_UID, DbHelper.JOB_TITLE, DbHelper.JOB_DESCRIPTION};
        Cursor cursor = db.query(DbHelper.STARRED_TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(helper.JOB_UID);
            int index2 = cursor.getColumnIndex(helper.JOB_TITLE);
            int index3 = cursor.getColumnIndex(helper.JOB_DESCRIPTION);
            String job_id = cursor.getString(index1);
            String job_title = cursor.getString(index2);
            String job_description = cursor.getString(index3);
        }
        cursor.close();
        return "";

    }

    public String getCompanyData() {
        //select ngo_id,name,address;
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DbHelper.COMPANY_MAIL, DbHelper.COMPANY_NAME, DbHelper.COMPANY_ADDRESS};
        Cursor cursor = db.query(DbHelper.COMPANY_TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(helper.COMPANY_MAIL);
            int index2 = cursor.getColumnIndex(helper.COMPANY_NAME);
            int index3 = cursor.getColumnIndex(helper.COMPANY_ADDRESS);
            String com_id = cursor.getString(index1);
            String com_name = cursor.getString(index2);
            String com_add = cursor.getString(index3);
        }
        cursor.close();
        return "";

    }

    static class DbHelper extends SQLiteOpenHelper {
        //Table Names
        private static final String DATABASE_NAME = "AndroidDatabase";

        //private static final String NGO_TABLE_NAME="NGO";
        private static final String COMPANY_TABLE_NAME = "COMPANY";
        private static final String STARRED_TABLE_NAME = "STARRED";

        //USER Table column names
        private static final int DATABASE_VERSION = 1;

        //COMPANY Table column names
        private static final String COMPANY_MAIL = "MAIL";
        private static final String COMPANY_NAME = "Name";
        private static final String COMPANY_ADDRESS = "Address";

        //NGO Table column names
        /*private static final String NGO_UID="_id";
        private static final String NGO_NAME="Name";
        private static final String NGO_ADDRESS="Address";
*/

        //Starred Table column names
        private static final String JOB_UID = "jid";
        private static final String JOB_TITLE = "Title";
        private static final String JOB_DESCRIPTION = "Description";
        //private static final String JOB_SALARY="Salary";
        private Context context;

        //Table create statements
        private static final String CREATE_COMPANY_TABLE = "CREATE TABLE " + COMPANY_TABLE_NAME + " (" + COMPANY_MAIL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COMPANY_NAME + " VARCHAR(200), " + COMPANY_ADDRESS + " VARCHAR(300));";
        //private static final String CREATE_NGO_TABLE="CREATE TABLE "+NGO_TABLE_NAME+" ("+NGO_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NGO_NAME+" VARCHAR(200) , "+NGO_ADDRESS+" VARCHAR(300));";
        private static final String CREATE_JOB_TABLE = "CREATE TABLE " + STARRED_TABLE_NAME + " (" + JOB_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + JOB_TITLE + " VARCHAR(200) , " + JOB_DESCRIPTION + " VARCHAR(500));";
        //Table drop statements
        private static final String DROP_COMPANY_TABLE = "DROP TABLE IF EXISTS " + COMPANY_TABLE_NAME;
        //private static final String DROP_NGO_TABLE="DROP TABLE IF EXISTS "+NGO_TABLE_NAME;
        private static final String DROP_JOB_TABLE = "DROP TABLE IF EXISTS " + STARRED_TABLE_NAME;


        DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                //db.execSQL(CREATE_NGO_TABLE);
                db.execSQL(CREATE_COMPANY_TABLE);
                db.execSQL(CREATE_JOB_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {

                db.execSQL(DROP_COMPANY_TABLE);
                //db.execSQL(DROP_NGO_TABLE);
                db.execSQL(DROP_JOB_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
