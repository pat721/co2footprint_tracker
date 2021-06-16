package de.htwg.co2footprint_tracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import de.htwg.co2footprint_tracker.enums.DatabaseInterval;
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
    private static final String CONNECTION_TYPE = "connection_type";
    private static final String MERGE_KEY = "merge_key";

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


    /**
     * Adds the data on a per app base to the database.
     * @param packageModel
     */
    public void addData(Package packageModel) {

        Log.d(TAG, "attemting to addData");

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
        contentValues.put(CONNECTION_TYPE, packageModel.getConnectionType().toString());
        contentValues.put(MERGE_KEY, generateMergeKey(packageModel));

        cumulateDailyData(contentValues, packageModel);

        Log.d(TAG, "addData: Adding " + packageModel.getPackageName() + " to " + TABLE_NAME_DATA_PER_MINUTE_TABLE);

        db.insert(TABLE_NAME_DATA_PER_MINUTE_TABLE, null, contentValues);
    }

    /**
     * Method adds and cumulates the data on a app/connection-type/date base.
     * @param packageModel
     */
    public void cumulateDailyData(ContentValues contentValues, Package packageModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_DATA_PER_DAY_TABLE + " WHERE " + MERGE_KEY + " = '" + generateMergeKey(packageModel) + "'";
        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor cursor = db1.rawQuery(query, null);
        cursor.moveToFirst();

        if (cursor.getCount() <= 0) {
            db.insert(TABLE_NAME_DATA_PER_DAY_TABLE, null, contentValues);
        } else {
            long rbw = cursor.getLong(cursor.getColumnIndex(RECEIVED_BYTES_WIFI));
            long rbm = cursor.getLong(cursor.getColumnIndex(RECEIVED_BYTES_MOBILE));
            long rbt = cursor.getLong(cursor.getColumnIndex(RECEIVED_BYTES_TOTAL));
            long tbw = cursor.getLong(cursor.getColumnIndex(TRANSMITTED_BYTES_WIFI));
            long tbm = cursor.getLong(cursor.getColumnIndex(TRANSMITTED_BYTES_MOBILE));
            long tmt = cursor.getLong(cursor.getColumnIndex(TRANSMITTED_BYTES_TOTAL));
            long rpw = cursor.getLong(cursor.getColumnIndex(RECEIVED_PACKETS_WIFI));
            long rpm = cursor.getLong(cursor.getColumnIndex(RECEIVED_PACKETS_MOBILE));
            long rpt = cursor.getLong(cursor.getColumnIndex(RECEIVED_PACKETS_TOTAL));
            long tpw = cursor.getLong(cursor.getColumnIndex(TRANSMITTED_PACKETS_WIFI));
            long tpm = cursor.getLong(cursor.getColumnIndex(TRANSMITTED_PACKETS_MOBILE));
            long tpt = cursor.getLong(cursor.getColumnIndex(TRANSMITTED_PACKETS_TOTAL));
            double energyConsumption = cursor.getDouble(cursor.getColumnIndex(ENERGY_CONSUMPTION));

            String cumulateDailyData = "UPDATE " + TABLE_NAME_DATA_PER_DAY_TABLE + " SET " +
                    RECEIVED_BYTES_WIFI + " = " + (rbw + packageModel.getReceivedBytesWifi()) + " ," +
                    RECEIVED_BYTES_MOBILE + " = " + (rbm + packageModel.getReceivedBytesMobile()) + " ," +
                    RECEIVED_BYTES_TOTAL + " = " + (rbt + packageModel.getReceivedBytesTotal()) + " ," +
                    TRANSMITTED_BYTES_WIFI + " = " + (tbw + packageModel.getTransmittedBytesWifi()) + " ," +
                    TRANSMITTED_BYTES_MOBILE + " = " + (tbm + packageModel.getTransmittedBytesMobile()) + " ," +
                    TRANSMITTED_BYTES_TOTAL + " = " + (tmt + packageModel.getTransmittedBytesTotal()) + " ," +
                    RECEIVED_PACKETS_WIFI + " = " + (rpw + packageModel.getReceivedPacketsWifi()) + " ," +
                    RECEIVED_PACKETS_MOBILE + " = " + (rpm + packageModel.getReceivedPacketsMobile()) + " ," +
                    RECEIVED_PACKETS_TOTAL + " = " + (rpt + packageModel.getReceivedPacketsTotal()) + " ," +
                    TRANSMITTED_PACKETS_WIFI + " = " + (tpw + packageModel.getTransmittedPacketsWifi()) + " ," +
                    TRANSMITTED_PACKETS_MOBILE + " = " + (tpm + packageModel.getTransmittedPacketsMobile()) + " ," +
                    TRANSMITTED_PACKETS_TOTAL + " = " + (tpt + packageModel.getTransmittedPacketsTotal()) + " ," +
                    ENERGY_CONSUMPTION + " = " + (energyConsumption + packageModel.getEnergyConsumption()) +
                    " WHERE " + MERGE_KEY + " = '" + generateMergeKey(packageModel) + "'";
            db.execSQL(cumulateDailyData);
        }
    }

    private String generateMergeKey(Package packageModel) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY);
        Date packageTimestampDate = new Date(packageModel.getTimestamp() * 1000);
        String packageTimestamp = sdf.format(packageTimestampDate);

        return packageModel.getPackageUid() + "" + packageModel.getConnectionType() + "" + packageTimestamp + "";
    }


    private void createTables(SQLiteDatabase db) {
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
                ENERGY_CONSUMPTION + " REAL" + "," +
                CONNECTION_TYPE + " TEXT" + "," +
                MERGE_KEY + " TEXT" +
                ")";
        db.execSQL(createTable);
    }


    private void dropTables(SQLiteDatabase db) {
        dropTable(db, TABLE_NAME_DATA_PER_MINUTE_TABLE);
        dropTable(db, TABLE_NAME_DATA_PER_HOUR_TABLE);
        dropTable(db, TABLE_NAME_DATA_PER_DAY_TABLE);
        dropTable(db, TABLE_NAME_DATA_PER_WEEK_TABLE);
        dropTable(db, TABLE_NAME_DATA_PER_MONTH_TABLE);
    }

    private void dropTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        Log.d(TAG, "dropTable: Dropping " + tableName);
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

    public void purgeDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        clearDb();
        onUpgrade(db, db.getVersion(), db.getVersion() + 1);
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


    public double getTotalEnergyConsumptionForPackage(int packageUid) {
        String query = "SELECT SUM(" + ENERGY_CONSUMPTION + ") as " + ENERGY_CONSUMPTION +
                " FROM " + TABLE_NAME_DATA_PER_MINUTE_TABLE + " WHERE " + PACKAGE_UID + " = " + packageUid + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        return cursor.getDouble(0);
    }


    public long getTotalReceivedBytesForPackage(int packageUid) {
        String query = "SELECT SUM(" + RECEIVED_BYTES_TOTAL + ") as total_bytes FROM " + TABLE_NAME_DATA_PER_MINUTE_TABLE + " WHERE " + PACKAGE_UID + " = " + packageUid + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        long bytes = 0;

        if (data.moveToFirst()) {
            bytes = data.getLong(0);
        }
        return bytes;
    }

    public double getTotalEnergyConsumption(HashSet<Integer> applicationUIDs) {
        double energyConsumption = 0.0;
        for (Integer uid : applicationUIDs) {
            energyConsumption += getTotalEnergyConsumptionForPackage(uid);
        }
        return energyConsumption;
    }

    public long getTotalReceivedBytes(HashSet<Integer> applicationUIDs) {
        long reveivedBytes = 0L;
        for (Integer uid : applicationUIDs) {
            reveivedBytes += getTotalReceivedBytesForPackage(uid);
        }
        return reveivedBytes;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        dropTables(db);
        onCreate(db);
    }


}
