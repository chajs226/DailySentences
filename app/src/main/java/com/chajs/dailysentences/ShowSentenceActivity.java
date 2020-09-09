package com.chajs.dailysentences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowSentenceActivity extends AppCompatActivity {

    DatabaseHelper myDb = null;
    Button btnViewAll;
    Button btnClose;
    Button btnSuccess;
    Button btnFail;
    Button btnSkip;

    TextView txtKeyword;
    TextView txtKorSentence;
    TextView txtEngSentence;
    String id = null;
    private static final String TAG = "ShowSentenceActivity";
    Sentence sentence;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sentence);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("내용");

        myDb = new DatabaseHelper(this);
        btnViewAll = (Button)findViewById(R.id.buttonViewAll);
        btnSuccess = (Button)findViewById(R.id.buttonSucess);
        btnFail = (Button)findViewById(R.id.buttonFail);
        btnSkip = (Button)findViewById(R.id.buttonSkip);
        btnClose = (Button)findViewById(R.id.buttonClose);
        txtKeyword = (TextView)findViewById(R.id.textViewKeyword);
        txtKeyword.setMovementMethod((new ScrollingMovementMethod()));
        txtKorSentence = (TextView)findViewById(R.id.textViewKorSentence);
        txtKorSentence.setMovementMethod(new ScrollingMovementMethod());
        txtEngSentence = (TextView)findViewById(R.id.textViewEngSentence);
        txtEngSentence.setMovementMethod(new ScrollingMovementMethod());

        viewAll();
        UpdateSuccess();
        UpdateFail();
        UpdateSkip();
        close();

        Intent intent = getIntent();

        onNewIntent(getIntent());
    }

    protected void onNewIntent(Intent intent) {
        Log.d(TAG,"onNewIntent: ");
        if (intent != null )
        {
            processIntent(intent);
        }
        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent) {
        Log.d(TAG,"processIntent: ");
        sentence = (Sentence)intent.getSerializableExtra("sentence");

        if(sentence.getId() != null) {
            id = sentence.getId();
        }
        if(sentence.getKeyword() != null) {
            txtKeyword.setText((sentence.getKeyword()));
        }

        if(sentence.getKorSentence() != null) {
            txtKorSentence.setText(sentence.getKorSentence());
        }
        if(sentence.getEngSentence() != null) {
            txtEngSentence.setText(sentence.getEngSentence());
        }
    }

    public void close() {
        btnClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    public void viewAll() {
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(txtEngSentence .getVisibility() == View.INVISIBLE)
                            txtEngSentence.setVisibility(View.VISIBLE);
                        else
                            txtEngSentence.setVisibility(View.INVISIBLE);

                    }
                }
        );
    }

    public void UpdateSuccess() {
        btnSuccess.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer recordUpdate = Integer.valueOf(sentence.getSucessCount()) + 1;
                        boolean result = myDb.updateRecord(sentence.getId(), "S", recordUpdate);
                        if(result) {
                            UpSertStatHistory();
                            finish();
                            //Intent intent = new Intent(v.getContext(), ListActivity.class);
                            //v.getContext().startActivity(intent);
                        }
                    }
                }
        );
    }

    public void UpdateFail() {
        btnFail.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer recordUpdate = Integer.valueOf(sentence.getFailCount()) + 1;
                        boolean result = myDb.updateRecord(sentence.getId(), "F", recordUpdate);
                        if(result) {
                            UpSertStatHistory();
                            finish();
                            //Intent intent = new Intent(v.getContext(), ListActivity.class);
                            //v.getContext().startActivity(intent);
                        }
                    }
                }
        );
    }

    public void UpdateSkip() {
        btnSkip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer recordUpdate = Integer.valueOf(sentence.getSkipCount()) + 1;
                        boolean result = myDb.updateRecord(sentence.getId(), "K", recordUpdate);
                        if(result) {
                            UpSertStatHistory();
                            finish();
                            //Intent intent = new Intent(v.getContext(), ListActivity.class);
                            //v.getContext().startActivity(intent);
                        }
                    }
                }
        );
    }

    public void UpSertStatHistory() {

        Integer sum_success=0;
        Integer sum_fail=0;
        Integer sum_skip=0;
        Integer sum_point=0;
        Integer avg_point=0;

        String [] today = new String[] {getToday()};

        Cursor res1 = myDb.getStaticSum();
        while (res1.moveToNext()) {
            if (res1.getCount() != 0) {
                sum_success = res1.getInt(0);
                sum_fail = res1.getInt(1);
                sum_skip = res1.getInt(2);
                sum_point = res1.getInt(3);
                avg_point = res1.getInt(4);
            }
        }

        Cursor res2 = myDb.getTodayStatics(today);
        if(res2.getCount() == 0) {
            boolean result = myDb.saveStatsHisotry(today[0],sum_success, sum_fail, sum_skip, sum_point, avg_point, "");
            if(!result) {
                Toast.makeText(ShowSentenceActivity.this, "Insert Stat Error", Toast.LENGTH_LONG).show();
            }
        }
        else {
            while (res2.moveToNext()) {

                String id = res2.getString(0);

                boolean result = myDb.updateStatsHisotry(id, today[0], sum_success, sum_fail, sum_skip, sum_point, avg_point, "");
                if(!result) {
                    Toast.makeText(ShowSentenceActivity.this, "Update Stat Error", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private String getToday(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
