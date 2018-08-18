package com.andrewxa.androidsensor;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = "MainActivity";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mTempr;
    private  Sensor sensors;

    private LineChart mChart;
    private LineChart mChartStep;
    private Thread thread;
    private boolean plotData = true;
    LineData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mTempr = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);

        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for(int i=0; i<sensors.size(); i++){
            Log.d(TAG, "onCreate: Sensor "+ i + ": " + sensors.get(i).toString());
        }

        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        if (mTempr != null) {
            mSensorManager.registerListener(this, mTempr, SensorManager.SENSOR_DELAY_GAME);
        }

        mChart = (LineChart) findViewById(R.id.chart1);
/*        mChartStep = (LineChart) findViewById(R.id.chart1);*/

        // enable description text
        mChart.getDescription().setEnabled(true);
/*        mChartStep.getDescription().setEnabled(true);*/

        // enable touch gestures
        mChart.setTouchEnabled(true);
/*        mChartStep.setTouchEnabled(true);*/

        // enable scaling and dragging
        mChart.setDragEnabled(true);
/*        mChartStep.setDragEnabled(true);*/
        mChart.setScaleEnabled(true);
    /*    mChartStep.setScaleEnabled(true);*/
        mChart.setDrawGridBackground(false);
        /*mChartStep.setDrawGridBackground(false);
*/
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
     /*   mChartStep.setPinchZoom(true);*/

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
       /* mChartStep.setBackgroundColor(Color.WHITE);*/

        data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);
        /*mChartStep.setData(data);*/

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
/*        Legend l2 = mChartStep.getLegend();*/

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

/*
        l2.setForm(Legend.LegendForm.LINE);
        l2.setTextColor(Color.WHITE);
*/

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);
/*

        // ----------------------

        XAxis x2 = mChartStep.getXAxis();
        x2.setTextColor(Color.WHITE);
        x2.setDrawGridLines(true);
        x2.setAvoidFirstLastClipping(true);
        x2.setEnabled(true);

        YAxis leftAxis2 = mChartStep.getAxisLeft();
        leftAxis2.setTextColor(Color.WHITE);
        leftAxis2.setDrawGridLines(false);
        leftAxis2.setAxisMaximum(10f);
        leftAxis2.setAxisMinimum(0f);
        leftAxis2.setDrawGridLines(true);


        YAxis rightAxis2 = mChartStep.getAxisRight();
        rightAxis2.setEnabled(false);

        mChartStep.getAxisLeft().setDrawGridLines(false);
        mChartStep.getXAxis().setDrawGridLines(false);
        mChartStep.setDrawBorders(false);
*/


        feedMultiple();

    }

    private void addEntry(SensorEvent event) {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

//            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
            data.addEntry(new Entry(set.getEntryCount(), event.values[0] + 5), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(150);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());
/*

            mChart.setData(data);
            mChart.invalidate();
*/

        }
    }


    private void addEntryStepDetector(SensorEvent event) {

       LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

//            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
            data.addEntry(new Entry(set.getEntryCount(), event.values[0] + 5), 0);
          /*  mChart.invalidate();*/
            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(150);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());

          /*  mChart.setData(data);
            mChart.invalidate();*/


        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.MAGENTA);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    private void feedMultiple() {

        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
        mSensorManager.unregisterListener(this);

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if(plotData){
            addEntry(event);
            addEntryStepDetector(event);


            /*mChart.setData(data);
            mChart.invalidate();*/

            plotData = false;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mTempr, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        mSensorManager.unregisterListener(MainActivity.this);
        thread.interrupt();
        super.onDestroy();
    }
}
