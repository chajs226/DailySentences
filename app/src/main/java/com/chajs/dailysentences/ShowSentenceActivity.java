package com.chajs.dailysentences;

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
    TextView txtKorSentence;
    TextView txtEngSentence;
    String id = null;
    private static final String TAG = "ShowSentenceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sentence);

        myDb = new DatabaseHelper(this);
        btnViewAll = (Button)findViewById(R.id.buttonViewAll);
        btnClose = (Button)findViewById(R.id.buttonClose);
        txtKorSentence = (TextView)findViewById(R.id.textViewKorSentence);
        txtEngSentence = (TextView)findViewById(R.id.textViewEngSentence);
        viewAll();
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
        Sentence sentence = (Sentence)intent.getSerializableExtra("sentence");

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
                        /*
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            showMessage("Error", "Nothing Found");//show message
                            return ;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Kor: " + res.getString(1) + "\n");
                            buffer.append("Eng: " + res.getString(2) + "\n");
                        }

                        showMessage("Data",buffer.toString());
                        */
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
