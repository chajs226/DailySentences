package com.chajs.dailysentences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowSentenceActivity extends AppCompatActivity {

    DatabaseHelper myDb = null;
    Button btnViewAll;
    Button btnClose;
    Button btnSuccess;
    Button btnFail;
    Button btnSkip;

    TextView txtKorSentence;
    TextView txtEngSentence;
    String id = null;
    private static final String TAG = "ShowSentenceActivity";
    Sentence sentence;

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
        txtKorSentence = (TextView)findViewById(R.id.textViewKorSentence);
        txtEngSentence = (TextView)findViewById(R.id.textViewEngSentence);
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
                        if(txtEngSentence.getVisibility() == View.INVISIBLE)
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
                            finish();
                            Intent intent = new Intent(v.getContext(), ListActivity.class);
                            v.getContext().startActivity(intent);
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
                            finish();
                            Intent intent = new Intent(v.getContext(), ListActivity.class);
                            v.getContext().startActivity(intent);
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
                            finish();
                            Intent intent = new Intent(v.getContext(), ListActivity.class);
                            v.getContext().startActivity(intent);
                        }
                    }
                }
        );
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
