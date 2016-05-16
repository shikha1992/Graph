package graph.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contactsManager";
    public static final String TABLE_CONTACTS = "contacts";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "id";
    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";
    private static final String KEY_POINT = "point";
    public static String DB_PATH = "/data/data/appsmaven.graph.com.graph/databases/";
    private SQLiteDatabase myDataBase;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void openDataBase()  {
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_X + " TEXT,"+ KEY_Y + " TEXT,"
                + KEY_POINT + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_X, contact.getx());
        values.put(KEY_Y, contact.get_y());
        values.put(KEY_POINT, contact.get_point()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_X,KEY_Y, KEY_POINT }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return contact;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getWritableDatabase();
        db.isOpen();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setx(cursor.getString(1));
                contact.set_y(cursor.getString(2));
                contact.set_point(cursor.getString(3));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public int updateContact(Contact contact,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db=this.getReadableDatabase();
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_X, contact.getx());
        values.put(KEY_Y, contact.get_y());
        values.put(KEY_POINT, contact.get_point());
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { (id) });
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

public void Delete(){
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("delete from "+ TABLE_CONTACTS);
    db.close();
}
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

}
