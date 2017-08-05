package com.xhh.upage;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/4/1.
 */
public class UpageUtils implements BaseColumns {
    private UpageUtils() {}

    public static final String TABLE_NAME = "upage";

    public static final String DB_NAME = "pages.db";
    public static final int DB_VERSION = 2;

    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_PAGE = "note";
    public static final String COLUMN_NAME_CREATED = "created";
    public static final String COLUMN_NAME_MODIFIED = "modified";

    public static final String DEFAULT_SORT_ORDER = "modified DESC";

    public static final String AUTHORITY = "com.xhh.provider.upage";
    public static final String PATH_PAGES = "pages";
    public static final String PATH_ID_PATTERN = "pages/#";

    public static final int PAGES = 1;
    public static final int PAGE_ID = 2;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/pages");
    public static final Uri ID_URI_PATTERN = Uri.parse("content://" + AUTHORITY + "/pages/#");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.xhh.page";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.xhh.page";

    public static final String TAG_PAGES = "pages";
    public static final String TAG_BOOKS = "books";
}
