package com.example.testingconnectionlowerapi;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.testingconnectionlowerapi.domain.History;
import com.example.testingconnectionlowerapi.viewmodel.HistoryViewModel;
import com.example.testingconnectionlowerapi.websockets.WebSocketConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private class ChartDataAdapter extends ArrayAdapter<LineData> {

        ChartDataAdapter(Context context, List<LineData> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            LineData data = getItem(position);

            ViewHolder holder;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item_linechart, null);
                holder.chart = convertView.findViewById(R.id.chart);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // apply styling
            if (data != null) {
                data.setValueTextColor(Color.BLACK);
            }
            holder.chart.getDescription().setEnabled(false);
            holder.chart.setDrawGridBackground(false);

            XAxis xAxis = holder.chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);

            YAxis leftAxis = holder.chart.getAxisLeft();
            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(15f);

            YAxis rightAxis = holder.chart.getAxisRight();
            rightAxis.setLabelCount(5, false);
            rightAxis.setSpaceTop(15f);

            // set data
            holder.chart.setData(data);

            // do not forget to refresh the chart
//            holder.chart.invalidate();
            holder.chart.animateX(700);

            return convertView;
        }

        private class ViewHolder {

            LineChart chart;
        }
    }

    private static HistoryViewModel mHistoryViewModel = null;
    LineChart liveChart;
    Thread thread;
    private long midnightTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        midnightTimestamp = calendar.getTimeInMillis();

        mHistoryViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        liveChart = findViewById(R.id.live_chart);
        liveChart.getDescription().setEnabled(true);
        liveChart.getDescription().setText("Real-time sensor data");

        liveChart.setTouchEnabled(true);
        liveChart.setDragEnabled(true);
        liveChart.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        liveChart.setPinchZoom(true);
        // set an alternative background color
        liveChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();

        data.addDataSet(createSet(null,"Acetone", Color.BLUE));
        data.addDataSet(createSet(null,"Clean Air", Color.GRAY));
        data.addDataSet(createSet(null,"Alcohol", Color.RED));
        data.addDataSet(createSet(null,"Gas", Color.GREEN));

        // add empty data
        liveChart.setData(data);

        Legend liveChartLegend = liveChart.getLegend();

        // modify the legend ...
        liveChartLegend.setForm(Legend.LegendForm.LINE);
        liveChartLegend.setTextColor(Color.BLACK);

        XAxis xAxis = liveChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            @Override
            public String getFormattedValue(float value) {
                long mValue = (midnightTimestamp + TimeUnit.SECONDS.toMillis((long) value));
                return mFormat.format(new Date(mValue));
            }
        });


        YAxis leftAxis = liveChart.getAxisLeft();
        leftAxis.setAxisMaximum(20f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = liveChart.getAxisRight();
        rightAxis.setEnabled(false);

        liveChart.setDrawBorders(false);

        feedMultiple();

        ListView lv = findViewById(R.id.history_list);

        ArrayList<LineData> list = new ArrayList<>();

        // 20 items
        for (int i = 0; i < 20; i++) {
            list.add(generateDataLine(i + 1));
        }

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);

//        insertDummyHistories();

        WebSocketConnection webSocketConnection = new WebSocketConnection("http://172.30.114.246:3000");
        webSocketConnection.start(this);
    }

    public void updateUI(){
        System.out.println("Updated");
    }

    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> values1 = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            values1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(values1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            values2.add(new Entry(i, values1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(values2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        return new LineData(sets);
    }

    private LineDataSet createSet(List<Entry> initialValues, String label, int color) {

        LineDataSet set = new LineDataSet(initialValues, label);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(color);
        set.setHighlightEnabled(true);
        set.setDrawValues(true);
        set.setDrawCircles(true);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    private void feedMultiple() {

        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(() -> {
            while (true){
                addEntry();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void addEntry() {
        LineData data = liveChart.getData();
        if (data != null) {
            // set.addEntry(...); // can be called as well
            float now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - midnightTimestamp);
            data.addEntry(new Entry(now, (float) Math.random() * 15), 0);
            data.addEntry(new Entry(now, (float) Math.random() * 15), 1);
            data.addEntry(new Entry(now, (float) Math.random() * 15), 2);
            data.addEntry(new Entry(now, (float) Math.random() * 15), 3);

            data.notifyDataChanged();

            // let the chart know it's data has changed
            liveChart.notifyDataSetChanged();

            // limit the number of visible entries
            liveChart.setVisibleXRangeMaximum(15);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            liveChart.moveViewToX(now);
        }


    }




    private void insertDummyHistories() {

        List<History> historyList = new ArrayList<>();
        historyList.add(new History("Acetone", 222));
        historyList.add(new History("Acetone", 223));

//        mHistoryViewModel.insertHistoriesAsync(historyList);

        new GetLocalHistory().execute();

    }

    private class GetLocalHistory extends AsyncTask<Void, Void, List<History>> {

        private List<History> histories;


        @Override
        protected List<History> doInBackground(Void... voids) {
            histories = mHistoryViewModel.getLocalHistorySync();
            return histories;
        }

        @Override
        protected void onPostExecute(List<History> histories) {
            System.out.println("here");

        }
    }
}
