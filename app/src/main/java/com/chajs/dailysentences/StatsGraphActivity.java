package com.chajs.dailysentences;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class StatsGraphActivity extends AppCompatActivity {

    private enum BEFORE {
        ONE_WEEK,
        ONE_MONTH,
        THREE_MONTH
    }
    private LineChart chart;

    DatabaseHelper myDb = null;
    static String[] fromDate = null;

    Button btnOneWeekBefore;
    Button btnOneMonthBefore;
    Button btnThreeMonthBefore;

    static ArrayList<String> xAxisLable  = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_graph);

        myDb = new DatabaseHelper(this);
        xAxisLable = new ArrayList<String>();

        chart = findViewById(R.id.LineChart);
        btnOneWeekBefore = (Button)findViewById(R.id.buttonOneWeekBefore);
        btnOneMonthBefore = (Button)findViewById(R.id.buttonOneMonthBefore);
        btnThreeMonthBefore = (Button)findViewById(R.id.buttonThreeMonthBefore);

        fromDate = new String[] { getDate(BEFORE.ONE_WEEK) };
        DrawChart(fromDate);

        OneWeekBefore();
        OneMonthBefore();
        ThreeMonthBefore();

    }

    public void OneWeekBefore() {
        btnOneWeekBefore.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fromDate = new String[] {getDate(BEFORE.ONE_WEEK)};
                        DrawChart(fromDate);
                    }
                }
        );
    }

    public void OneMonthBefore() {
        btnOneMonthBefore.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fromDate = new String[] {getDate(BEFORE.ONE_MONTH)};
                        DrawChart(fromDate);
                    }
                }
        );
    }

    public void ThreeMonthBefore() {
        btnThreeMonthBefore.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fromDate = new String[] {getDate(BEFORE.THREE_MONTH)};
                        DrawChart(fromDate);
                    }
                }
        );
    }


    private String getDate(BEFORE before) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        xAxisLable.clear();
        //오늘날짜 가져오기
        Calendar cal = Calendar.getInstance();

        if (before == BEFORE.ONE_WEEK) {
            cal.add(Calendar.DAY_OF_MONTH, -7);
            /*
            for (int i=0; i>-7; i--) {
                cal.add(Calendar.DAY_OF_MONTH, i);
            }
            xAxisLable.add(res.getString(0).substring(4,6) + "/" + res.getString(0).toString().substring(6,8));
            */

        }
        else if (before == BEFORE.ONE_MONTH) {
            cal.add(Calendar.MONTH, -1);
        }
        else if (before == BEFORE.THREE_MONTH) {
            cal.add(Calendar.MONTH, -3);
        }
        else {
            ;
        }
        Log.d("StatsGraphActivity",sdf.format(cal.getTime()).toString());
        return sdf.format(cal.getTime()).toString();
    }

    private void DrawChart(String[] fromDate) {
        ArrayList<Entry> values = new ArrayList<>();
        int i=0;
        Cursor res = myDb.getStaticsforGraph(fromDate);
        if(res.getCount() == 0) {
            Toast.makeText(StatsGraphActivity.this, "통계정보가 없습니다.", Toast.LENGTH_LONG);//show message
        }
        else {

            xAxisLable.clear();

            values.clear();
            while (res.moveToNext()) {
                float val = Float.parseFloat(res.getString(1).toString());
                //values.add(new Entry(Integer.parseInt(res.getString(0).toString()), val));
                values.add(new Entry(i, val));
                xAxisLable.add(res.getString(0).substring(4,6) + "/" + res.getString(0).toString().substring(6,8));
                i++;
                }

            //for(int i)

            //
        }
        /*
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));
        }
         */

        LineDataSet set1;
        set1 = new LineDataSet(values, "평균점수");
        set1.setLineWidth(2); //선굵기
        set1.setCircleRadius(3); //곡률
        //set1.setCircleColor(ContextCompat.getColor(mContext, R.color.graphColor)); // LineChart에서 Line Circle Color 설정
        //set1.setCircleHoleColor(ContextCompat.getColor(mContext, R.color.graphColor)); // LineChart에서 Line Hole Circle Color 설정
        //set1.setColor(ContextCompat.getColor(mContext, R.color.graphColor)); // LineChart에서 Line Color 설정

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.DKGRAY);
        set1.setCircleColor(Color.GRAY);

        // set data
        chart.setData(data);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        //xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLable));
        xAxis.setLabelCount(i);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLable));


        YAxis yLAxis = chart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        chart.setDoubleTapToZoomEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDescription(description);
        //chart.animateY(2000, Easing.EasingOption.EaseInCubic);
        chart.invalidate();

    }

    /*
    public ArrayList<String> getAreaCount() {
        ArrayList<String> label = new ArrayList<>();
        for (int i=0; i<xAxisLable.size(); i++)
        {
            label.add(xAxisLable.get(i));
        }
        return label;
    }

     */

}
