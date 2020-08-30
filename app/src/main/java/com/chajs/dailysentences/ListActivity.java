package com.chajs.dailysentences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private ArrayList<Sentence> sentenceList;
    DatabaseHelper myDb = null;

    TextView txtViewStaticsSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("등록 리스트");

        txtViewStaticsSum = (TextView)findViewById(R.id.textViewStatics);
        myDb = new DatabaseHelper(this);
        this.InitializeData();

        OpenStatsGraphActivity();
    }

    @Override
    protected void onResume() {

        super.onResume();
        InitializeData();
        // The activity has become visible (it is now "resumed").
    }


    protected void onNewIntent(Intent intent) {
        Log.d(TAG,"onNewIntent: ");
        if (intent != null )
        {
            InitializeData();
        }
        super.onNewIntent(intent);
    }

    public void InitializeData()
    {
        LoadStaticsSum();
        LoadList();
    }

    public void LoadList()
    {
        sentenceList = new ArrayList<>();

        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            Toast.makeText(ListActivity.this, "Nothing Found", Toast.LENGTH_LONG);//show message
        }
        else {

            while (res.moveToNext()) {
                sentenceList.add(new Sentence(res.getString(0),
                        res.getString(1), res.getString(2),
                        res.getString(3), res.getString(4),
                        res.getString(5), res.getString(6),
                        res.getString(7), res.getString(8),
                        res.getString(9), res.getString(10)));
            }
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RecyclerViewList);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new SentenceAdapter(sentenceList));  // Adapter 등록
    }

    public void LoadStaticsSum()
    {
        Cursor res = myDb.getStaticSum();
        if(res.getCount() == 0) {
            ;
        }
        else {
            while (res.moveToNext()) {
                txtViewStaticsSum.setText("총점: " + res.getString(3) + " | 평균: " + res.getString(4)  +" | 성공: " + res.getString(0) + " 실패: " + res.getString(1) + " 통과: " + res.getString(2));
            }
        }
    }

    public void OpenStatsGraphActivity() {
        txtViewStaticsSum.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), StatsGraphActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

}
