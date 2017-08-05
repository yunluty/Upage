package com.xhh.upage;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016/5/18.
 */
public abstract class BaseRecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static String TAG = "BaseRecyclerViewCursorAdapter";

    public static int FLAG_REGISTER_CONTENT_OBSERVER = 0x02;


    public BaseRecyclerViewCursorAdapter(Context context, Cursor c) {
        this(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
    }

    public BaseRecyclerViewCursorAdapter(Context context, Cursor c, int flag) {
        init(context, c, flag);
    }

    void init(Context context, Cursor c, int flag) {

    }


}
