package telegramplus.Theming.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

import telegramplus.Theming.theme;


public class ThemeDBAdapter {
    private static final String CREATE_MAINTABLE_THEMES = "CREATE TABLE \"changes\" (\"id\" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , \"uid\" INTEGER NOT NULL , \"type\" INTEGER NOT NULL  DEFAULT 0 , \"time\" DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP)";

    private static final String DATABASE_MAINTABLE = "themes";
    private static final String DATABASE_NAME = "HastiGram5";
    private static final int DATABASE_VERSION = 8;


    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_THUMB1 = "thumb1";
    private static final String KEY_THUMB2 = "thumb2";
    private static final String KEY_THUMB3 = "thumb3";
    private static final String KEY_XMLLINK = "xmllink";
    private static final String KEY_IMGELINK = "imagelink";
    private static final String KEY_XMLDATA = "xmldata";




    private static final String TAG = "IGRAM_USER_TABLE_THEMES";
    private final ThemeDBAdapter.DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private final String[] yek_SH_flashkart;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, ThemeDBAdapter.DATABASE_NAME, null, ThemeDBAdapter.DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(ThemeDBAdapter.CREATE_MAINTABLE_THEMES);
            } catch (SQLException e) {
                FirebaseCrash.report(e);
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(ThemeDBAdapter.TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS main");
            onCreate(db);
        }
    }

    public ThemeDBAdapter(Context ctx) {
        this.yek_SH_flashkart = new String[]{KEY_ID,KEY_NAME , KEY_DESCRIPTION , KEY_THUMB1,KEY_THUMB2 , KEY_THUMB3 , KEY_XMLLINK ,KEY_IMGELINK, KEY_XMLDATA};
        this.DBHelper = new ThemeDBAdapter.DatabaseHelper(ctx);
    }

    public void open() throws SQLException {
        this.db = this.DBHelper.getWritableDatabase();
    }

    public void close() {
        this.DBHelper.close();
    }

    public List<theme> getAllItms(int type) {
        return cursorToList(this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "type == '" + type + "'", null, null, null, " id desc", null));
    }

    public List<theme> getAllItms() {
        return cursorToList(this.db.query(DATABASE_MAINTABLE, this.yek_SH_flashkart, null, null, null, null, " id desc"));
    }


    public int getsize() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM themes;", null);
        boolean isOk = cursor.moveToFirst();
        return cursor.getInt(0);
    }


    private List<theme> cursorToList(Cursor cursor) {
        List<theme> items = new ArrayList();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                theme nam = new theme();
                nam.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                nam.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                nam.setThumb1(cursor.getString(cursor.getColumnIndex(KEY_THUMB1)));
                nam.setThumb2(cursor.getString(cursor.getColumnIndex(KEY_THUMB2)));
                nam.setThumb3(cursor.getString(cursor.getColumnIndex(KEY_THUMB3)));
                nam.setXmllink(cursor.getString(cursor.getColumnIndex(KEY_XMLLINK)));
                nam.setImagelink(cursor.getString(cursor.getColumnIndex(KEY_IMGELINK)));
                nam.setXmldata(cursor.getString(cursor.getColumnIndex(KEY_XMLDATA)));



                items.add(nam);
            }
        }
        return items;
    }

    public theme getItm(int ID) throws SQLException {
        Cursor cursor = this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "id == '" + ID + "' ", null, null, null, null, null);
        theme nam = new theme();
        if (cursor != null) {
            cursor.moveToFirst();
            nam.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            nam.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
            nam.setThumb1(cursor.getString(cursor.getColumnIndex(KEY_THUMB1)));
            nam.setThumb2(cursor.getString(cursor.getColumnIndex(KEY_THUMB2)));
            nam.setThumb3(cursor.getString(cursor.getColumnIndex(KEY_THUMB3)));
            nam.setXmllink(cursor.getString(cursor.getColumnIndex(KEY_XMLLINK)));
            nam.setImagelink(cursor.getString(cursor.getColumnIndex(KEY_IMGELINK)));
            nam.setXmldata(cursor.getString(cursor.getColumnIndex(KEY_XMLDATA)));

        }
        assert cursor != null;
        cursor.close();
        return nam;
    }





    public void insert(theme theme) {
        //Open connection to write data
        ContentValues values = new ContentValues();
        values.put(KEY_NAME , theme.getName());
        values.put(KEY_DESCRIPTION, theme.getDescription());
        values.put(KEY_THUMB1, theme.getThumb1());
        values.put(KEY_THUMB2, theme.getThumb2());
        values.put(KEY_THUMB3, theme.getThumb3());
        values.put(KEY_XMLLINK, theme.getXmllink());
        values.put(KEY_IMGELINK, theme.getImagelink());
        values.put(KEY_XMLDATA, theme.getXmldata());
//        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//        values.put(KEY_TIME, currentDateTimeString);

        // Inserting Row
        db.insert(DATABASE_MAINTABLE, null, values);

    }

    public void delete(String Name) {

        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, KEY_NAME + "= ?", new String[]{String.valueOf(Name)});
        db.close(); // Closing database connection
    }

    public void deleteAll() {

        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, null, null);
        db.close(); // Closing database connection
    }

}