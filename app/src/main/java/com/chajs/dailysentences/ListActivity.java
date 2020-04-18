package com.chajs.dailysentences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ArrayList<Sentence> sentenceList;
    DatabaseHelper myDb = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        myDb = new DatabaseHelper(this);
        this.InitializeData();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RecyclerViewList);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new SentenceAdapter(sentenceList));  // Adapter 등록

        SentenceAdapter adpater = new SentenceAdapter(sentenceList);
        //adpater.setOnItemClickListener(new SentenceAdapter.OnItemClickListener() {
        /*
        @Override
            public void onItemClick(View v, int position) {
            }
        });
         */
    }

    public void InitializeData()
    {
        sentenceList = new ArrayList<>();

        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            Toast.makeText(ListActivity.this, "Nothing Found", Toast.LENGTH_LONG);//show message
        }
        else {
            //StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                sentenceList.add(new Sentence(res.getString(0),
                        res.getString(1), res.getString(2),
                        res.getString(3), res.getString(4),
                        res.getString(5), res.getString(6),
                        res.getString(7), res.getString(8)));
                }
        }

    }
}
