package telegramplus.category;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;



public class catDBAdapter {
    private static final String CREATE_MAINTABLE_CHATS = "CREATE TABLE \"catchats\"" +
            " (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
            " \"dialog_id\" INTEGER NOT NULL , " +
            "\"catCode\" INTEGER  NOT NULL ," +
            " \"isChannel\" INTEGER NOT NULL  DEFAULT 0," +
            " \"isGroup\" INTEGER NOT NULL  DEFAULT 0," +
            " \"isHidden\" INTEGER NOT NULL  DEFAULT 0)";

    private static final String DATABASE_MAINTABLE = "catchats";
    private static final String DATABASE_NAME = "HastiGram";
    private static final int DATABASE_VERSION = 8;


    private static final String KEY_ID = "id";
    private static final String KEY_DIALOG_ID = "dialog_id";
    private static final String KEY_HIDE_CODE = "catCode";
    private static final String KEY_IS_CHANNEL = "isChannel";
    private static final String KEY_IS_GROUP = "isGroup";
    private static final String KEY_IS_HIDDEN = "isHidden";


    private static final String TAG = "HastiGram_USER_TABLE_CATS";
    private final DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private final String[] yek_SH_flashkart;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_MAINTABLE_CHATS);
            } catch (SQLException e) {
                FirebaseCrash.report(e);
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS main");
            onCreate(db);
        }
    }


    public catDBAdapter(Context ctx) {
        this.yek_SH_flashkart = new String[]{KEY_ID, KEY_DIALOG_ID, KEY_HIDE_CODE, KEY_IS_CHANNEL, KEY_IS_GROUP, KEY_IS_HIDDEN};
        this.DBHelper = new DatabaseHelper(ctx);
    }

    public void open() throws SQLException {
        this.db = this.DBHelper.getWritableDatabase();
    }

    public void close() {
        this.DBHelper.close();
    }

    public List<chatobject> getAllItms(int HIDE_CODE) {
        return cursorToList(this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "catCode == '" + HIDE_CODE + "'", null, null, null, " id desc", null));
    }

    public List<chatobject> getAllItms() {
        return cursorToList(this.db.query(DATABASE_MAINTABLE, this.yek_SH_flashkart, null, null, null, null, " id desc"));
    }


    public int getsize() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM catchats;", null);
        boolean isOk = cursor.moveToFirst();
        return cursor.getInt(0);
    }


    public int getCatSize(int id ) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM catchats Where catCode == '" + id + "';", null);
        boolean isOk = cursor.moveToFirst();
        return cursor.getInt(0);
    }

    private List<chatobject> cursorToList(Cursor cursor) {
        List<chatobject> items = new ArrayList();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                chatobject nam = new chatobject();
                nam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                nam.setDialog_id(cursor.getInt(cursor.getColumnIndex(KEY_DIALOG_ID)));
                nam.setCatCode(cursor.getInt(cursor.getColumnIndex(KEY_HIDE_CODE)));
                nam.setIsChannel(cursor.getInt(cursor.getColumnIndex(KEY_IS_CHANNEL)));
                nam.setIsGroup(cursor.getInt(cursor.getColumnIndex(KEY_IS_GROUP)));
                nam.setIsHidden(cursor.getInt(cursor.getColumnIndex(KEY_IS_HIDDEN)));

                items.add(nam);
            }
        }
        return items;
    }


    public chatobject getItm(int ID) throws SQLException {
        Cursor cursor = this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "dialog_id == '" + ID + "' AND isHidden = 1", null, null, null, null, null);
        chatobject nam = new chatobject();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            nam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            nam.setDialog_id(cursor.getInt(cursor.getColumnIndex(KEY_DIALOG_ID)));
            nam.setCatCode(cursor.getInt(cursor.getColumnIndex(KEY_HIDE_CODE)));
            nam.setIsChannel(cursor.getInt(cursor.getColumnIndex(KEY_IS_CHANNEL)));
            nam.setIsGroup(cursor.getInt(cursor.getColumnIndex(KEY_IS_GROUP)));
            nam.setIsHidden(cursor.getInt(cursor.getColumnIndex(KEY_IS_HIDDEN)));

        }
        assert cursor != null;
        cursor.close();
        return nam;
    }


    public boolean isExist (chatobject chatobject){
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM catchats Where dialog_id == '" + chatobject.getDialog_id() + " '  AND catCode == '" + chatobject.getCatCode() + " ';", null);
        boolean isOk = cursor.moveToFirst();
        return cursor.getInt(0) > 0;

    }

    public boolean isExist (int did , int catID){
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM catchats Where dialog_id == '" + did + " '  AND catCode == '" +catID + " ';", null);
      cursor.moveToFirst();
        boolean isOk = cursor.getInt(0) > 0 ;
        cursor.close();
        return isOk;

    }

    public void insert(chatobject chatobject) {
        //Open connection to write data
        if (!isExist(chatobject)) {
            ContentValues values = new ContentValues();
            values.put(KEY_DIALOG_ID, chatobject.getDialog_id());
            values.put(KEY_HIDE_CODE, chatobject.getCatCode());
            values.put(KEY_IS_CHANNEL, chatobject.isChannel);
            values.put(KEY_IS_GROUP, chatobject.isGroup);
            values.put(KEY_IS_HIDDEN, chatobject.isHidden);
            // Inserting Row
            db.insert(DATABASE_MAINTABLE, null, values);
        }

    }

    public void delete(int UID) {

        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, KEY_DIALOG_ID + "= ?", new String[]{String.valueOf(UID)});
        db.close(); // Closing database connection
    }

    public void deleteAll(int catID) {

        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, KEY_HIDE_CODE+ "= ?", new String[]{String.valueOf(catID)});
        db.close(); // Closing database connection
    }

    public void deleteAll() {

        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, null, null);
        db.close(); // Closing database connection
    }

    public void update(int code, int value) {

        ContentValues values = new ContentValues();

        values.put(KEY_IS_HIDDEN, value);
        db.update(DATABASE_MAINTABLE, values, KEY_HIDE_CODE + "= ?", new String[]{String.valueOf(code)});

    }

}
