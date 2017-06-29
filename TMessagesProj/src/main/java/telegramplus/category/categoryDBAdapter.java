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



public class categoryDBAdapter  {
    private static final String CREATE_MAINTABLE_CATS = "CREATE TABLE \"categories\"" +
            " (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
            " \"username\" TEXT )";

    private static final String DATABASE_MAINTABLE = "categories";
    private static final String DATABASE_NAME = "HastiGram4";
    private static final int DATABASE_VERSION = 8;


    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "username";


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
                db.execSQL(CREATE_MAINTABLE_CATS);
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


    public categoryDBAdapter(Context ctx) {
        this.yek_SH_flashkart = new String[]{KEY_ID,KEY_NAME};
        this.DBHelper = new DatabaseHelper(ctx);
    }

    public void open() throws SQLException {
        this.db = this.DBHelper.getWritableDatabase();
    }

    public void close() {
        this.DBHelper.close();
    }

    public List<category> getAllItms(int id) {
        return cursorToList(this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "id == '" + id + "'", null, null, null, " id desc", null));
    }

    public List<category> getAllItms() {
        return cursorToList(this.db.query(DATABASE_MAINTABLE, this.yek_SH_flashkart, null, null, null, null, " id desc"));
    }


    public int getsize() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM categories;", null);
        boolean isOk = cursor.moveToFirst();
        return cursor.getInt(0);
    }



    public int getHidedSize() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM categories ;", null);
        boolean isOk = cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int getCatSize(int id ) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM categories Where id == '" + id + "';", null);
        boolean isOk = cursor.moveToFirst();
        return cursor.getInt(0);
    }

    private List<category> cursorToList(Cursor cursor) {
        List<category> items = new ArrayList();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                category nam = new category();
                nam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                nam.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));


                items.add(nam);
            }
        }
        return items;
    }


    public category getItm(int ID) throws SQLException {
        Cursor cursor = this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "dialog_id == '" + ID + "' AND isHidden = 1", null, null, null, null, null);
        category nam = new category();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            nam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            nam.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));

        }
        assert cursor != null;
        cursor.close();
        return nam;
    }


    public void insert(category category) {
        //Open connection to write data
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());

        db.insert(DATABASE_MAINTABLE, null, values);

    }

    public void delete(int UID) {

        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, KEY_ID + "= ?", new String[]{String.valueOf(UID)});
        db.close(); // Closing database connection
    }

    public void deleteAll() {

        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, null, null);
        db.close(); // Closing database connection
    }

    public void update(int code, String value) {

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, value);
        db.update(DATABASE_MAINTABLE, values, KEY_ID + "= ?", new String[]{String.valueOf(code)});

    }

}

