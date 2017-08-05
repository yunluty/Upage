package com.xhh.upage;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity {
    private static final String TAG = "EditorActivity";
    private static final int STATE_INSERT = 0;
    private static final int STATE_EDIT = 0;
    private static final String[] PROJECTION = {
            UpageUtils._ID,
            UpageUtils.COLUMN_NAME_TITLE,
            UpageUtils.COLUMN_NAME_PAGE
    };

    private int mState;
    private EditText mEditor;
    private Uri mUri;
    private Cursor mCursor;
    private String mOldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mEditor = (EditText) findViewById(R.id.editor);

        Intent intent = getIntent();
        if (Intent.ACTION_INSERT.equals(intent.getAction())) {
            mState = STATE_INSERT;

            mUri = getContentResolver().insert(UpageUtils.CONTENT_URI, null);
            if (mUri == null) {
                Log.e(TAG, "Failed to insert new page");
                finish();
                return;
            }
        } else if (Intent.ACTION_EDIT.equals(intent.getAction())) {
            mState = STATE_EDIT;

            if (intent.getData() == null) {
                Log.e(TAG, "Intent data is null");
                finish();
                return;
            } else {
                mUri = intent.getData();
            }
        }

        mCursor = getContentResolver().query(mUri, PROJECTION, null, null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCursor != null) {
            mCursor.moveToFirst();
            mOldText = mCursor.getString(mCursor.getColumnIndex(UpageUtils.COLUMN_NAME_PAGE));
            mEditor.setTextKeepState(mOldText);
            mEditor.setSelection(mOldText.length());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mEditor.getText() != null) {
            String text = mEditor.getText().toString();
            int len = text.length();

            if (isFinishing() && len == 0) {
                deletePage();
            } else {
                updatePage(text, null);
            }

        }
    }

    private void updatePage(String text, String title) {
        if (mOldText.compareTo(text) == 0) {
            Log.i(TAG, "The text is not changed");
            return;
        }

        ContentValues values = new ContentValues();

        if (title == null) {
            title = text.substring(0, Math.min(30, text.length()));
        }

        values.put(UpageUtils.COLUMN_NAME_TITLE, title);
        values.put(UpageUtils.COLUMN_NAME_PAGE, text);
        values.put(UpageUtils.COLUMN_NAME_MODIFIED, System.currentTimeMillis());

        getContentResolver().update(mUri, values, null, null);
    }

    private void deletePage() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

        if (mUri != null) {
            getContentResolver().delete(mUri, null, null);
        }
    }
}
