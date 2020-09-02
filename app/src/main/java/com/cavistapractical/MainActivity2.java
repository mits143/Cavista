package com.cavistapractical;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.cavistapractical.db.DBHelper;
import com.cavistapractical.db.InsertHelper;
import com.cavistapractical.model.Image;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity2 extends AppCompatActivity {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.edtComment)
    EditText edtComment;
    @BindView(R.id.txtSubmit)
    TextView txtSubmit;

    private Image data;
    SQLiteDatabase db;
    DBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }

    private void init() {
        mHelper = new DBHelper(this);
        if (getIntent().getExtras() != null) {
            data = (Image) getIntent().getSerializableExtra("data"); //Obtaining data
            assert data != null;
            toolBar.setTitle(data.getTitle());
            Glide.with(this).load(data.getLink()).into(image);

            display_data(data.getId());
        }
    }

    @OnClick({R.id.toolBar, R.id.image, R.id.edtComment, R.id.txtSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtSubmit:
                saveData(data.getId(), edtComment.getText().toString().trim());
                break;
        }
    }

    // Function for insertion
    private void saveData(String image_id, String image_cmt) {
        db = mHelper.getWritableDatabase();
        // the helper class for doing insert operation
        InsertHelper ins = new InsertHelper(this);
        ins.open();

        long ret = ins.insertComment(image_id, image_cmt);
        if (ret > 0) {
            edtComment.setText("");
            Toast.makeText(this.getBaseContext(), "Comment Saved in DB successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
        }

        ins.close();
        db.close();
    }


    public void display_data(String idd) {
        db = mHelper.getReadableDatabase();

        String select_data = "SELECT * FROM " + DBHelper.COMMENT_TABLE + " WHERE " + DBHelper.IMAGE_ID +  "= '" + idd + "'";
        Cursor sCursor  = db.rawQuery(select_data, null);
            if (sCursor.moveToFirst()) {
            do {
                edtComment.setText(sCursor.getString(sCursor.getColumnIndex(DBHelper.IMAGE_COMMENT)));
            } while (sCursor.moveToNext());
        }
        sCursor.close();
    }
}