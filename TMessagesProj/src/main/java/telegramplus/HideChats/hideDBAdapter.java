package telegramplus.HideChats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;



public class hideDBAdapter {
    private static final String CREATE_MAINTABLE_HIDES = "CREATE TABLE \"hides\"" +
            " (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
            " \"dialog_id\" INTEGER NOT NULL , " +
            "\"hideCode\" INTEGER  NOT NULL ," +
            " \"isChannel\" INTEGER NOT NULL  DEFAULT 0," +
            " \"isGroup\" INTEGER NOT NULL  DEFAULT 0," +
            " \"isHidden\" INTEGER NOT NULL  DEFAULT 0)";

    private static final String DATABASE_MAINTABLE = "hides";
    private static final String DATABASE_NAME = "HastiGram2";
    private static final int DATABASE_VERSION = 8;


    private static final String KEY_ID = "id";
    private static final String KEY_DIALOG_ID = "dialog_id";
    private static final String KEY_HIDE_CODE = "hideCode";
    private static final String KEY_IS_CHANNEL = "isChannel";
    private static final String KEY_IS_GROUP = "isGroup";
    private static final String KEY_IS_HIDDEN = "isHidden";


    private static final String TAG = "IGRAM_USER_TABLE_HIDES";
    private final DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private final String[] yek_SH_flashkart;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_MAINTABLE_HIDES);
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


    public hideDBAdapter(Context ctx) {
        this.yek_SH_flashkart = new String[]{KEY_ID, KEY_DIALOG_ID, KEY_HIDE_CODE, KEY_IS_CHANNEL, KEY_IS_GROUP, KEY_IS_HIDDEN};
        this.DBHelper = new DatabaseHelper(ctx);
    }

    public void open() throws SQLException {
        this.db = this.DBHelper.getWritableDatabase();
    }

    public void close() {
        this.DBHelper.close();
    }

    public int getsize() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM hides;", null);
        boolean isOk = cursor.moveToFirst();
        return cursor.getInt(0);
    }


    public int getHidedSize() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM hides Where isHidden = 1;", null);
        boolean isOk = cursor.moveToFirst();
        int hidden_size = cursor.getInt(0);
        return hidden_size;
    }

    public hideObjc getItm(int ID) throws SQLException {
        Cursor cursor = this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "dialog_id == '" + ID + "' AND isHidden = 1", null, null, null, null, null);
        hideObjc nam = new hideObjc();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            nam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            nam.setDialog_id(cursor.getInt(cursor.getColumnIndex(KEY_DIALOG_ID)));
            nam.setHideCode(cursor.getInt(cursor.getColumnIndex(KEY_HIDE_CODE)));
            nam.setIsChannel(cursor.getInt(cursor.getColumnIndex(KEY_IS_CHANNEL)));
            nam.setIsGroup(cursor.getInt(cursor.getColumnIndex(KEY_IS_GROUP)));
            nam.setIsHidden(cursor.getInt(cursor.getColumnIndex(KEY_IS_HIDDEN)));

        }
        assert cursor != null;
        cursor.close();
        return nam;
    }

    public void insert(hideObjc hideObjc) {
        //Open connection to write data
        ContentValues values = new ContentValues();
        values.put(KEY_DIALOG_ID, hideObjc.getDialog_id());
        values.put(KEY_HIDE_CODE, hideObjc.getHideCode());
        values.put(KEY_IS_CHANNEL, hideObjc.isChannel);
        values.put(KEY_IS_GROUP, hideObjc.isGroup);
        values.put(KEY_IS_HIDDEN, hideObjc.isHidden);
        // Inserting Row
        db.insert(DATABASE_MAINTABLE, null, values);

    }

    public void delete(int UID) {
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, KEY_DIALOG_ID + "= ?", new String[]{String.valueOf(UID)});
        db.close(); // Closing database connection
    }

    public void update(int code, int value) {
        ContentValues values = new ContentValues();
        values.put(KEY_IS_HIDDEN, value);
        db.update(DATABASE_MAINTABLE, values, KEY_HIDE_CODE + "= ?", new String[]{String.valueOf(code)});

    }

}
