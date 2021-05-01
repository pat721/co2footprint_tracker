package de.htwg.co2footprint_tracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import de.htwg.co2footprint_tracker.model.DatabaseInterval;
import de.htwg.co2footprint_tracker.model.Package;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_FILE_NAME = "footprint_tracker_db";
    private static final String TABLE_NAME_DATA_PER_MINUTE_TABLE = "data_per_minute";
    private static final String TABLE_NAME_DATA_PER_HOUR_TABLE = "data_per_hour";
    private static final String TABLE_NAME_DATA_PER_DAY_TABLE = "data_per_day";
    private static final String TABLE_NAME_DATA_PER_WEEK_TABLE = "data_per_week";
    private static final String TABLE_NAME_DATA_PER_MONTH_TABLE = "data_per_month";
    private static final String NAME = "app_name";
    private static final String TIMESTAMP = "starting_time";
    private static final String VERSION = "version";
    private static final String PACKAGE_NAME = "package_name";
    private static final String PACKAGE_UID = "package_uid";
    private static final String DUPLICATE_UIDS = "duplicate_uids"; //  Whether multiple packages share this uid
    private static final String RECEIVED_BYTES_WIFI = "received_bytes_wifi";
    private static final String RECEIVED_BYTES_MOBILE = "received_bytes_mobile";
    private static final String RECEIVED_BYTES_TOTAL = "received_bytes_total";
    private static final String TRANSMITTED_BYTES_WIFI = "transmitted_bytes_wifi";
    private static final String TRANSMITTED_BYTES_MOBILE = "transmitted_bytes_mobile";
    private static final String TRANSMITTED_BYTES_TOTAL = "transmitted_bytes_total";
    private static final String RECEIVED_PACKETS_WIFI = "received_packets_wifi";
    private static final String RECEIVED_PACKETS_MOBILE = "received_packets_mobile";
    private static final String RECEIVED_PACKETS_TOTAL = "received_packets_total";
    private static final String TRANSMITTED_PACKETS_WIFI = "transmitted_packets_wifi";
    private static final String TRANSMITTED_PACKETS_MOBILE = "transmitted_packets_mobile";
    private static final String TRANSMITTED_PACKETS_TOTAL = "transmitted_packets_total";
    private static final String ENERGY_CONSUMPTION = "energy_consumption";
    private static DatabaseHelper databaseHelperInstance = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, 1);
    }

    public static DatabaseHelper getInstance(Context ctx) {
        if (databaseHelperInstance == null) {
            databaseHelperInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return databaseHelperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableFor(TABLE_NAME_DATA_PER_MINUTE_TABLE, db);
        createTableFor(TABLE_NAME_DATA_PER_HOUR_TABLE, db);
        createTableFor(TABLE_NAME_DATA_PER_DAY_TABLE, db);
        createTableFor(TABLE_NAME_DATA_PER_WEEK_TABLE, db);
        createTableFor(TABLE_NAME_DATA_PER_MONTH_TABLE, db);
    }

    private void createTableFor(String tableName, SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + tableName +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT" + "," +
                TIMESTAMP + " INTEGER" + "," +
                VERSION + " TEXT" + "," +
                PACKAGE_NAME + " TEXT" + "," +
                PACKAGE_UID + " TEXT" + "," +
                DUPLICATE_UIDS + " TEXT" + "," +
                RECEIVED_BYTES_WIFI + " TEXT" + "," +
                RECEIVED_BYTES_MOBILE + " TEXT" + "," +
                RECEIVED_BYTES_TOTAL + " TEXT" + "," +
                TRANSMITTED_BYTES_WIFI + " TEXT" + "," +
                TRANSMITTED_BYTES_MOBILE + " TEXT" + "," +
                TRANSMITTED_BYTES_TOTAL + " TEXT" + "," +
                RECEIVED_PACKETS_WIFI + " TEXT" + "," +
                RECEIVED_PACKETS_MOBILE + " TEXT" + "," +
                RECEIVED_PACKETS_TOTAL + " TEXT" + "," +
                TRANSMITTED_PACKETS_WIFI + " TEXT" + "," +
                TRANSMITTED_PACKETS_MOBILE + " TEXT" + "," +
                TRANSMITTED_PACKETS_TOTAL + " TEXT" + "," +
                ENERGY_CONSUMPTION + " REAL" +
                ")";
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        dropTable(db, TABLE_NAME_DATA_PER_MINUTE_TABLE);
        dropTable(db, TABLE_NAME_DATA_PER_HOUR_TABLE);
        dropTable(db, TABLE_NAME_DATA_PER_DAY_TABLE);
        dropTable(db, TABLE_NAME_DATA_PER_WEEK_TABLE);
        dropTable(db, TABLE_NAME_DATA_PER_MONTH_TABLE);
        onCreate(db);
    }

    private void dropTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP IF TABLE EXISTS " + tableName);
        Log.d(TAG, "dropTable: Dropping " + tableName);
    }


    public boolean addData(DatabaseInterval databaseInterval, Package packageModel) {

        Log.d(TAG, "attemting to addData");

        String affectedTable = "";
        if (databaseInterval == DatabaseInterval.MINUTE) {
            affectedTable = TABLE_NAME_DATA_PER_MINUTE_TABLE;
        } else if (databaseInterval == DatabaseInterval.HOUR) {
            affectedTable = TABLE_NAME_DATA_PER_HOUR_TABLE;
        } else if (databaseInterval == DatabaseInterval.DAY) {
            affectedTable = TABLE_NAME_DATA_PER_DAY_TABLE;
        } else if (databaseInterval == DatabaseInterval.WEEK) {
            affectedTable = TABLE_NAME_DATA_PER_WEEK_TABLE;
        } else {
            affectedTable = TABLE_NAME_DATA_PER_MONTH_TABLE; //Default
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, packageModel.getName());
        contentValues.put(TIMESTAMP, packageModel.getTimestamp());
        contentValues.put(VERSION, packageModel.getVersion());
        contentValues.put(PACKAGE_NAME, packageModel.getPackageName());
        contentValues.put(PACKAGE_UID, packageModel.getPackageUid());
        contentValues.put(DUPLICATE_UIDS, packageModel.getDuplicateUids());
        contentValues.put(RECEIVED_BYTES_WIFI, packageModel.getReceivedBytesWifi());
        contentValues.put(RECEIVED_BYTES_MOBILE, packageModel.getReceivedBytesMobile());
        contentValues.put(RECEIVED_BYTES_TOTAL, packageModel.getReceivedBytesTotal());
        contentValues.put(TRANSMITTED_BYTES_WIFI, packageModel.getTransmittedBytesWifi());
        contentValues.put(TRANSMITTED_BYTES_MOBILE, packageModel.getTransmittedBytesMobile());
        contentValues.put(TRANSMITTED_BYTES_TOTAL, packageModel.getTransmittedBytesTotal());
        contentValues.put(RECEIVED_PACKETS_WIFI, packageModel.getReceivedPacketsWifi());
        contentValues.put(RECEIVED_PACKETS_MOBILE, packageModel.getReceivedPacketsMobile());
        contentValues.put(RECEIVED_PACKETS_TOTAL, packageModel.getReceivedPacketsTotal());
        contentValues.put(TRANSMITTED_PACKETS_WIFI, packageModel.getTransmittedPacketsWifi());
        contentValues.put(TRANSMITTED_PACKETS_MOBILE, packageModel.getTransmittedPacketsMobile());
        contentValues.put(TRANSMITTED_PACKETS_TOTAL, packageModel.getTransmittedPacketsTotal());
        contentValues.put(ENERGY_CONSUMPTION, packageModel.getEnergyConsumption());

        Log.d(TAG, "addData: Adding " + packageModel.getPackageName() + " to " + affectedTable);

        long result = db.insert(affectedTable, null, contentValues);

        //if date as inserted incorrectly it will return -1
        return result != -1;
    }

    public void clearDb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_DATA_PER_MINUTE_TABLE);
        db.execSQL("DELETE FROM " + TABLE_NAME_DATA_PER_HOUR_TABLE);
        db.execSQL("DELETE FROM " + TABLE_NAME_DATA_PER_DAY_TABLE);
        db.execSQL("DELETE FROM " + TABLE_NAME_DATA_PER_WEEK_TABLE);
        db.execSQL("DELETE FROM " + TABLE_NAME_DATA_PER_MONTH_TABLE);
        db.execSQL("VACUUM");
        Log.d(TAG, "db " + DATABASE_FILE_NAME + " cleared");
    }


    public Cursor getData(DatabaseInterval databaseInterval) {

        String requestedTable;
        if (databaseInterval == DatabaseInterval.MINUTE) {
            requestedTable = TABLE_NAME_DATA_PER_MINUTE_TABLE;
        } else if (databaseInterval == DatabaseInterval.HOUR) {
            requestedTable = TABLE_NAME_DATA_PER_HOUR_TABLE;
        } else if (databaseInterval == DatabaseInterval.DAY) {
            requestedTable = TABLE_NAME_DATA_PER_DAY_TABLE;
        } else if (databaseInterval == DatabaseInterval.WEEK) {
            requestedTable = TABLE_NAME_DATA_PER_WEEK_TABLE;
        } else {
            requestedTable = TABLE_NAME_DATA_PER_MONTH_TABLE; //Default
        }

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + requestedTable;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getTotalsForPackage(int packageUid) {
        String query = "SELECT" +
                " SUM(" + RECEIVED_BYTES_WIFI + ") as " + RECEIVED_BYTES_WIFI +
                " , SUM(" + RECEIVED_BYTES_MOBILE + ") as " + RECEIVED_BYTES_MOBILE +
                " , SUM(" + RECEIVED_BYTES_TOTAL + ") as " + RECEIVED_BYTES_TOTAL +
                " , SUM(" + RECEIVED_PACKETS_WIFI + ") as " + RECEIVED_PACKETS_WIFI +
                " , SUM(" + RECEIVED_PACKETS_MOBILE + ") as " + RECEIVED_PACKETS_MOBILE +
                " , SUM(" + RECEIVED_PACKETS_TOTAL + ") as " + RECEIVED_PACKETS_TOTAL +
                " , SUM(" + ENERGY_CONSUMPTION + ") as " + ENERGY_CONSUMPTION +

                " FROM " + TABLE_NAME_DATA_PER_MINUTE_TABLE + " WHERE " + PACKAGE_UID + " = " + packageUid + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public double getTotalEnergyConsumptionFor(int packageUid) {
        String query = "SELECT SUM(" + ENERGY_CONSUMPTION + ") as " + ENERGY_CONSUMPTION +
                " FROM " + TABLE_NAME_DATA_PER_MINUTE_TABLE + " WHERE " + PACKAGE_UID + " = " + packageUid + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        return cursor.getDouble(0);
    }


    public long getTotalReceivedBytesFor(int packageUid) {
        String query = "SELECT SUM(" + RECEIVED_BYTES_TOTAL + ") as total_bytes FROM " + TABLE_NAME_DATA_PER_MINUTE_TABLE + " WHERE " + PACKAGE_UID + " = " + packageUid + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        long bytes = 0;

        if (data.moveToFirst()) {
            bytes = data.getLong(0);
        }
        return bytes;
    }


}
