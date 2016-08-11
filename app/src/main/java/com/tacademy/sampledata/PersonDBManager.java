package com.tacademy.sampledata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-11.
 */
public class PersonDBManager extends SQLiteOpenHelper {

    private static PersonDBManager instance;

    public static PersonDBManager getInstance() {
        if (instance == null)
            instance = new PersonDBManager();

        return instance;
    }

    private static final String DB_NAME = "address_db";
    private static final int DB_VERSON = 1;

    private PersonDBManager() {
        super(MyApplication.getContext(), DB_NAME, null, DB_VERSON);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + AddressContract.Address.TABLE + "("
                + AddressContract.Address._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + AddressContract.Address.COLUMN_NAME + " CHAR NOT NULL,"
                + AddressContract.Address.COLUMN_AGE + " INTEGER NOT NULL,"
                + AddressContract.Address.COLUMN_PHONE + " VARCHAR(11) NOT NULL,"
                + AddressContract.Address.COLUMN_ADDRESS + " TEXT NOT NULL);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Person insert(Person p) {
        if (p.getId() == -1) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.clear();
            values.put(AddressContract.Address.COLUMN_NAME, p.getName());
            values.put(AddressContract.Address.COLUMN_AGE, p.getAge());
            values.put(AddressContract.Address.COLUMN_PHONE, p.getPhone());
            values.put(AddressContract.Address.COLUMN_ADDRESS, p.getAddress());

            long id = db.insert(AddressContract.Address.TABLE, null, values);
            p.setId(id);
        } else update(p);

        return p;
    }

    public Person update(Person p) {
        if (p.getId() != -1) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.clear();
            values.put(AddressContract.Address.COLUMN_NAME, p.getName());
            values.put(AddressContract.Address.COLUMN_AGE, p.getAge());
            values.put(AddressContract.Address.COLUMN_PHONE, p.getPhone());
            values.put(AddressContract.Address.COLUMN_ADDRESS, p.getAddress());

            String where = AddressContract.Address._ID + " = ?";
            String[] args = { "" + p.getId() };

            db.update(AddressContract.Address.TABLE, values, where, args);
            return p;
        } else return insert(p);

    }

    public void delete(Person p) {
        if (p.getId() != -1) {
            SQLiteDatabase db = getWritableDatabase();
            String where = AddressContract.Address._ID + " = ?";
            String[] args = { "" + p.getId() };

            db.delete(AddressContract.Address.TABLE, where, args);
            p.setId(-1);
        }
    }

    public List<Person> select(String keyword) {
        List<Person> personList = new ArrayList<>();
        Cursor c = getPersonCursor(keyword);

        while (c.moveToNext()) {
            Person p = new Person();
            p.setId(c.getLong(c.getColumnIndex(AddressContract.Address._ID)));
            p.setName(c.getString(c.getColumnIndex(AddressContract.Address.COLUMN_NAME)));
            p.setAge(c.getInt(c.getColumnIndex(AddressContract.Address.COLUMN_AGE)));
            p.setPhone(c.getString(c.getColumnIndex(AddressContract.Address.COLUMN_PHONE)));
            p.setAddress(c.getString(c.getColumnIndex(AddressContract.Address.COLUMN_ADDRESS)));
            personList.add(p);
        }

        c.close();
        return personList;
    }

    public Cursor getPersonCursor(String keyword) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = { AddressContract.Address._ID,
                AddressContract.Address.COLUMN_NAME,
                AddressContract.Address.COLUMN_AGE,
                AddressContract.Address.COLUMN_PHONE,
                AddressContract.Address.COLUMN_ADDRESS };
        String selection = null;
        String[] args = null;

        if (!TextUtils.isEmpty(keyword)) {
            selection = AddressContract.Address.COLUMN_NAME + " LIKE ?";
            args = new String[] { "%" + keyword + "%" };
        }

        String groupby = null;
        String having = null;
        String orderby = AddressContract.Address.COLUMN_NAME + " COLLATE LOCALIZED ASC";

        return db.query(AddressContract.Address.TABLE, columns, selection, args, groupby, having, orderby);
    }
}
