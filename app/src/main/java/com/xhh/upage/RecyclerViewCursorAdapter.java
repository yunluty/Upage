package com.xhh.upage;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhh.upage.PagesListFragment.OnListFragmentInteractionListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by xuhuihui on 2017/11/30.
 */

public class RecyclerViewCursorAdapter extends BaseRecyclerViewCursorAdapter<RecyclerViewCursorAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final OnListFragmentInteractionListener mListener;

    public RecyclerViewCursorAdapter(Context context, OnListFragmentInteractionListener listener) {
        super(context, null);
        mLayoutInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    public RecyclerViewCursorAdapter(Context context, Cursor cursor, OnListFragmentInteractionListener listener) {
        super(context, cursor);
        mLayoutInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.pageslist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(UpageUtils.COLUMN_NAME_TITLE));
        long time = cursor.getLong(cursor.getColumnIndex(UpageUtils.COLUMN_NAME_MODIFIED));
        holder.mTitleView.setText(title);
        holder.mTimeView.setText(getTimeString(time));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onListFragmentInteraction(holder.getItemId());
                }
            }
        });
    }

    private String getTimeString(long time) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date.setTime(time);
        return formatter.format(date);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mTimeView;
        
        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mTimeView = (TextView) view.findViewById(R.id.time);
        }
    }
}
