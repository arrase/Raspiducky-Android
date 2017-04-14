package io.github.arrase.raspiducky.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.github.arrase.raspiducky.database.PayloadsDB;


public class PayloadsProvider extends ContentProvider {

    private static final String AUTH = "io.github.arrase.raspiducky.PAYLOADS_PROVIDER";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTH + "/payloads");

    //UriMatcher
    private static final int PAYLOADS = 1;
    private static final int PAYLOAD_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTH, "payloads", PAYLOADS);
        uriMatcher.addURI(AUTH, "payload/#", PAYLOAD_ID);
    }

    private PayloadsDB payloadsDB;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        payloadsDB = new PayloadsDB(mContext);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String where = selection;
        if (uriMatcher.match(uri) == PAYLOAD_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = payloadsDB.getReadableDatabase();

        return db.query(PayloadsDB.SELECTED_TABLE_NAME, projection, where,
                selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case PAYLOADS:
                return "vnd.android.cursor.dir/vnd.payloads.paths";
            case PAYLOAD_ID:
                return "vnd.android.cursor.item/vnd.payload.path";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long regId;

        SQLiteDatabase db = payloadsDB.getWritableDatabase();

        regId = db.insert(PayloadsDB.SELECTED_TABLE_NAME, null, values);

        mContext.getContentResolver().notifyChange(CONTENT_URI, null);

        return ContentUris.withAppendedId(CONTENT_URI, regId);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        String where = selection;
        if (uriMatcher.match(uri) == PAYLOAD_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = payloadsDB.getWritableDatabase();

        Integer rows = db.delete(PayloadsDB.SELECTED_TABLE_NAME, where, selectionArgs);

        mContext.getContentResolver().notifyChange(CONTENT_URI, null);

        return rows;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = payloadsDB.getWritableDatabase();
        Integer rows = db.update(PayloadsDB.SELECTED_TABLE_NAME, values, selection, selectionArgs);
        mContext.getContentResolver().notifyChange(CONTENT_URI, null);
        return rows;
    }

    public static final class Payload implements BaseColumns {

        public static final String PATH = "path";

        private Payload() {
        }
    }
}
