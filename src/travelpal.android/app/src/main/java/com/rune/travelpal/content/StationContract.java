package com.rune.travelpal.content;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by RuneJensen on 1/20/16.
 */
public class StationContract {

    private static final String AUTHORITY = "rune.travelpal.content"; 

    public static final String BASE_PATH = "Station";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/todos";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/todo";

}
