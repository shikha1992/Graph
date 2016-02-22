package appsmaven.graph.com.graph;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import graph.sqlite.Contact;
import graph.sqlite.DatabaseHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

   /* @InjectView(R.id.xyplot)
    com.github.mikephil.charting.charts.LineChart xyPlot;*/
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.btn_edt_metric)
    FloatingActionButton fab_btn_edt_metric;
    @InjectView(R.id.btn_add_point)
    FloatingActionButton btn_add_point;
    @InjectView(R.id.btn_create)
    Button btn_create;
    @InjectView(R.id.btn_save)
    Button btn_save;
    @InjectView(R.id.card_create)
    CardView card_create;
    @InjectView(R.id.lnr_add_pnt)
    LinearLayout lnr_add_pnt;
    @InjectView(R.id.edt_point)
    EditText edt_point;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    SharedPreferences        preferences;
    SharedPreferences.Editor editor;
    String                   pref_start_number, pref_end_number, pref_start_date, pref_end_date,
                             pref_no_columns, pref_table_name, pref_goal, pref_number, pref_time, pref_time_after_four_hr,
                             current_time, strng_edt,end_number_,start_number_;
    DatabaseHandler          db = new DatabaseHandler(this);
    List<Contact>            contacts;
    Integer                  start_number, end_number;
    String x_mont[]=new String[]{};
     GraphicalView chartView;
    @InjectView(R.id.chart)
     LinearLayout chart;
     ArrayList<String> labels = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        preferences = PreferenceManager
                      .getDefaultSharedPreferences(MainActivity.this);
        editor = preferences.edit();
        pref_table_name = preferences.getString("table_name", null);
        if (pref_table_name==null)
        {
            Intent i = new Intent(MainActivity.this, Edit_Graph.class);
            startActivity(i);
            finish();
        }

        Onclicklistners();

        method_set_sharedpref_values();
        current_time = GlobalConstant.get_current_time();

        if (checkDataBase())
        {
            contacts = db.getAllContacts();
        }

        method_getintent();

    }


    //////////////////////////shared_prefrence_method//////////////
    private void method_set_sharedpref_values()
    {
        pref_no_columns = preferences.getString("number_column", null);
        pref_table_name = preferences.getString("table_name", null);
        if(pref_table_name!=null)
        {
            toolbar.setTitle(pref_table_name);
        }
        else
        {
            toolbar.setTitle("Create graph");
        }
        pref_start_number = preferences.getString("edt_vw_target_start.getText().toString()" + "0", null);
        pref_end_number = preferences.getString("end_number", null);
        pref_start_date = preferences.getString("start_date", null);
        pref_end_date = preferences.getString("end_date", null);
        pref_goal = preferences.getString("goal", null);
        pref_number = preferences.getString("number_column", null);
        pref_time = preferences.getString("time_start", null);
        pref_time_after_four_hr = preferences.getString("time_after_four", null);
        end_number_ = preferences.getString("end_number", null);
        start_number_ = preferences.getString("edt_vw_target_start.getText().toString()" + "0", null);
        if (end_number_ != null)
        {
            end_number = Integer.valueOf(end_number_);
        }
        if (start_number_ != null)
        {
            start_number = Integer.valueOf(start_number_);
        }
    }


    ////////////////Database exist or not//////////
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(db.DB_PATH + db.DATABASE_NAME, null,
                    SQLiteDatabase.OPEN_READONLY);
            DatabaseHandler dbHelper = new DatabaseHandler(this);
            dbHelper.openDataBase();
        }
        catch (SQLiteException e)
        {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }


    private void Onclicklistners()
    {
        fab.setOnClickListener(this);
        btn_add_point.setOnClickListener(this);
        btn_create.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        fab_btn_edt_metric.setOnClickListener(this);
        chart.setOnClickListener(this);
//        chartView.setOnClickListener(this);
    }


    //////////////////////////set data
    private void method_getintent()
    {
        Intent i = getIntent();
        if (i.getStringExtra("start_number") != null)
        {
            if(pref_goal.equals("below")){
                method_value_add_to_graph_below();
            }
            else{
                method_add_values_toGraph();
            }
        }
        else if (pref_no_columns != null)
        {
            if(pref_goal.equals("below")){
                method_value_add_to_graph_below();
            }
            else{
            method_add_values_toGraph();
        }

        }
        else
        {
            btn_add_point.setVisibility(View.GONE);
            lnr_add_pnt.setVisibility(View.GONE);
            fab_btn_edt_metric.setVisibility(View.GONE);
//            xyPlot.setNoDataText("Currently no chart available," + "\n" + "Click above to add chart");
        }
    }


    /////////////////////add to graph
    private void method_add_values_toGraph() {
        {
            btn_create.setVisibility(View.GONE);
            card_create.setVisibility(View.GONE);
            lnr_add_pnt.setVisibility(View.GONE);
            fab_btn_edt_metric.setVisibility(View.VISIBLE);
            btn_add_point.setVisibility(View.VISIBLE);
//            xyPlot.setContentDescription(pref_table_name);


            ArrayList<String> array_x = new ArrayList<>();
            ArrayList<Integer> array_y = new ArrayList<>();
            ArrayList<String> array_point_strng = new ArrayList<>();
            ArrayList<String> labels_uper = new ArrayList<String>();
            String secnd_x = null;
            String secnd_y = null;
            String secnd_pnt = null;


            ArrayList<String> array_threshold_line_y=new ArrayList<>();
            String pref_chang = preferences.getString("change", null);

                for (int i = 0; i <= contacts.size()-1; i++){
                    if(i==0){
                            if(contacts.get(0).get_y().equals(pref_start_number)){
                                if(pref_chang.equals("no")){
                                    array_threshold_line_y.add("0");
                                }
                                else{
                                    array_threshold_line_y.add("0");
                                }

                        }
                        else{
                                array_threshold_line_y.add(contacts.get(0).get_y());
                        }
                    }
                   else if(i!=1){
                        array_threshold_line_y.add(contacts.get(i).get_y());
                    }
                }


//            array_threshold_line_y.add(contacts.get(1).get_y());
            for (int i = 0; i <= contacts.size() - 1; i++)
            {
                if (i == 1)
                {
                    secnd_x = contacts.get(1).getx();
                    secnd_y = contacts.get(1).get_y();
                    secnd_pnt = contacts.get(1).get_point();
                }
                else
                {
                    array_x.add(contacts.get(i).getx());
                    labels.add(contacts.get(i).getx());
                    labels_uper.add("");
                    array_y.add(Integer.valueOf(contacts.get(i).get_y()));
                    array_point_strng.add((contacts.get(i).get_point()));
                    Log.e("value", String.valueOf(contacts.get(i).getID()));
                }
            }
            array_x.add(secnd_x);
            labels.add(secnd_x);
            array_y.add(Integer.valueOf(secnd_y));
            array_point_strng.add(secnd_pnt);

//            ArrayList<Entry> entries = new ArrayList<>();

            for (int i = 0; i < array_point_strng.size(); i++)
            {
                Integer int_value = Integer.valueOf(array_y.get(i));
                Log.e("y", String.valueOf(array_y.get(i)));
//                Entry entry = new Entry(int_value, i);
//                entries.add(entry);
            }

            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();


            String[] mMonth = new String[] {
                    "Jan", "Feb" , "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
            };

            for(int i=0;i<labels.size();i++){
//                multiRenderer.addXTextLabel(i+1, labels.get(i));
            }
            for (int i = 0; i < labels.size(); i++)
            {

                if(i==labels.size()){
                    multiRenderer.addTextLabel(labels.size()+1, labels.get(i));
                }else{
                    multiRenderer.addTextLabel(i, labels.get(i));
                }

            }

            multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
            multiRenderer.setXLabels(0);
            // Adding incomeRenderer and expenseRenderer to multipleRenderer
            // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
            // should be same
            XYSeries incomeSeries = new XYSeries("Threshold line");
            // Creating an  XYSeries for Income
            XYSeries expenseSeries = new XYSeries("Points added");
            XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
            incomeRenderer.setColor(Color.BLUE);

            incomeRenderer.setPointStyle(PointStyle.CIRCLE);
            incomeRenderer.setPointStyle(PointStyle.CIRCLE);
//            incomeRenderer.setFillBelowLine(true);
            incomeRenderer.setLineWidth(8f);
            incomeRenderer.setFillPoints(true);
            incomeRenderer.setLineWidth(2);
            incomeRenderer.setDisplayChartValues(true);
            multiRenderer.setLabelsColor(Color.BLACK);



            XYMultipleSeriesDataset datasetf = new XYMultipleSeriesDataset();
            // Adding Income Series to the dataset
            datasetf.addSeries(incomeSeries);

            incomeSeries.add(0,Integer.valueOf(pref_start_number));
            incomeSeries.add(contacts.size()-1, Integer.valueOf(pref_end_number));
            for (int i=0;i<array_threshold_line_y.size();i++){
                if(i!=array_threshold_line_y.size()){
                    expenseSeries.add(i,Integer.valueOf(array_threshold_line_y.get(i)));
                }

            }
            // Adding Expense Series to dataset
            datasetf.addSeries(expenseSeries);
            // Creating XYSeriesRenderer to customize expenseSeries
            XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
            expenseRenderer.setColor(Color.RED);
            expenseRenderer.setFillPoints(false);
            expenseRenderer.setLineWidth(10);
            incomeRenderer.setColor(Color.BLACK);
            incomeRenderer.getChartValuesTextSize();
            incomeRenderer.setChartValuesTextSize(20);
            incomeRenderer.setChartValuesTextAlign(Paint.Align.CENTER);
            incomeRenderer.setDisplayChartValues(true);


            expenseRenderer.setPointStyle(PointStyle.CIRCLE);
//            expenseRenderer.setFillPoints(true);
            expenseRenderer.setLineWidth(10);
            incomeRenderer.setDisplayChartValues(true);
            expenseRenderer.setDisplayChartValues(true);

            expenseRenderer.setFillBelowLineColor(Color.YELLOW);
//            incomeRenderer.setFillBelowLineColor(R.color.colorAccent);

            /*expenseRenderer.setFillBelowLine(true);
            incomeRenderer.setFillBelowLine(true);*/
            incomeRenderer.setGradientStart(0, Color.rgb(192, 192, 192));
            expenseRenderer.setGradientStart(0, Color.rgb(192, 192, 192));
            expenseRenderer.setDisplayChartValues(true);
            incomeRenderer.setDisplayChartValues(true);
            incomeRenderer.isDisplayChartValues();
            expenseRenderer.isDisplayChartValues();
            expenseRenderer.setGradientEnabled(true);
            incomeRenderer.setGradientEnabled(true);



//            multiRenderer.setDisplayChartValues(true);
            expenseRenderer.setDisplayChartValues(true);
            multiRenderer.setMargins(new int[]{10, 30, 15, 20});
            multiRenderer.addSeriesRenderer(expenseRenderer);
            multiRenderer.addSeriesRenderer(incomeRenderer);

           /* XYSeriesRenderer xyRenderer = (XYSeriesRenderer) multiRenderer.getSeriesRendererAt(0);

            XYSeriesRenderer.FillOutsideLine  fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.
                    Type.ABOVE);
            fill.setColor(Color.YELLOW);
            xyRenderer.addFillOutsideLine(fill);

            fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BELOW);
            fill.setColor(Color.WHITE);
            xyRenderer.addFillOutsideLine(fill);

            XYSeriesRenderer xyRenderer2 = (XYSeriesRenderer) multiRenderer.getSeriesRendererAt(1);

            XYSeriesRenderer.FillOutsideLine  fill2 = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.
                    Type.ABOVE);
//            fill2.setColor(Color.CYAN);
            xyRenderer2.addFillOutsideLine(fill2);

            fill2 = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.ABOVE);
//            fill2.setColor(Color.CYAN);
            xyRenderer2.addFillOutsideLine(fill2);*/

            String measurmnt=preferences.getString("measurmnt", "minutes");
            multiRenderer.setYTitle(measurmnt, 0);

            multiRenderer.setMarginsColor(Color.WHITE);
            multiRenderer.setBackgroundColor(Color.BLACK);
            multiRenderer
                    .setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
            multiRenderer.setChartTitleTextSize(18);
            multiRenderer.setAxisTitleTextSize(14);
            multiRenderer.setLabelsTextSize(24);


            multiRenderer.setLabelsColor(Color.BLACK);
            multiRenderer.setLabelsTextSize(20);
            multiRenderer.setXLabelsColor(Color.BLACK);
//            multiRenderer.setChartValuesTextSize(20f);
            multiRenderer.setAntialiasing(true);
            multiRenderer.setZoomEnabled(true);
            multiRenderer.setPanEnabled(true, false);
            multiRenderer.setFitLegend(false);
            multiRenderer.setLegendTextSize(20);
            multiRenderer.setShowLegend(true);
            multiRenderer.setPointSize(10);
            multiRenderer.setShowGrid(true);
            multiRenderer.setZoomButtonsVisible(true);
            multiRenderer.setTextTypeface("sans_serif", Typeface.BOLD);
            multiRenderer.setAxesColor(Color.BLACK);



            multiRenderer.setChartTitleTextSize(20f);
            multiRenderer.setChartTitle(pref_table_name);

String hh[]=new String[]{"ff","fs"};
//            xyPlot.setData(data);
            String[] types = new String[] { LineChart.TYPE, LineChart.TYPE };
            chartView = ChartFactory.getLineChartView(getBaseContext(), datasetf, multiRenderer);
//            chart.setOnClickListener(this);
            chartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection seriesSelection = chartView.getCurrentSeriesAndPoint();

                    if (seriesSelection != null) {
                        int seriesIndex = seriesSelection.getSeriesIndex();
                        String selectedSeries="Income";
                        if(seriesIndex==0)
                            selectedSeries = "Income";
                        else
                            selectedSeries = "Expense";
                        // Getting the clicked Month
                        String month = labels.get((int)seriesSelection.getXValue());
                        // Getting the y value
                        int amount = (int) seriesSelection.getValue();
                        Toast.makeText(
                                getBaseContext(),
                                selectedSeries + " in "  + month + " : " + amount ,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            chart.addView(chartView);


        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create:
                Intent i = new Intent(MainActivity.this, Edit_Graph.class);
                startActivity(i);
                finish();
                break;

            case R.id.btn_edt_metric:
                Intent i_edt = new Intent(MainActivity.this, Edit_Graph.class);
                i_edt.putExtra("edit", "edit");
                startActivity(i_edt);
                finish();
                break;

            case R.id.btn_add_point:
                if (GlobalConstant.compare_dates_main_clas(GlobalConstant.method_get_current_date(), pref_end_date) == false) {
                    GlobalConstant.snackbar_method("You cannot exceed your end target value", view);
                }
                else
                {
                    Intent ir = new Intent(MainActivity.this, AddPoints.class);
                    startActivity(ir);
                    finish();
                }
               /* Intent ir = new Intent(MainActivity.this, AddPoints.class);
                startActivity(ir);
                finish();*/
                break;

            case R.id.btn_save:
                method_save_point(view);
                break;

            case R.id.chart:
            {
                SeriesSelection seriesSelection = chartView.getCurrentSeriesAndPoint();

                if (seriesSelection != null) {
                    int seriesIndex = seriesSelection.getSeriesIndex();
                    String selectedSeries="Income";
                    if(seriesIndex==0)
                        selectedSeries = "Income";
                    else
                        selectedSeries = "Expense";
                    // Getting the clicked Month
                    String month = labels.get((int)seriesSelection.getXValue());
                    // Getting the y value
                    int amount = (int) seriesSelection.getValue();
                    Toast.makeText(
                            getBaseContext(),
                            selectedSeries + " in "  + month + " : " + amount ,
                            Toast.LENGTH_SHORT).show();
                }
            }
                break;
        }
    }


    private void method_save_point(View view) {
        strng_edt = edt_point.getText().toString();

        if (strng_edt.length() > 0)
        {
            method_add_point(view);
        }
        else
        {
            InputMethodManager imm = (InputMethodManager) getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText())
            {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            GlobalConstant.snackbar_method("Enter point to add first", view);
        }
    }



    private void method_add_point(View view)
    {
        Log.e("start", String.valueOf(start_number));
        Log.e("end", String.valueOf(end_number));
        method_add_point_inrange();
    }



    private void method_add_point_inrange() {
        if (GlobalConstant.compare_time(current_time, pref_time_after_four_hr) == false) {
            editor.putString("time_start", GlobalConstant.get_current_time());
            editor.putString("time_after_four", GlobalConstant.get_next_four_hr_time());
            editor.commit();
            editor.apply();

            db.addContact((new Contact(GlobalConstant.get_current_time(), strng_edt, String.valueOf(Integer.valueOf(strng_edt)))));
            editor.putString("number_column", String.valueOf(Integer.valueOf(pref_number) + 1));
            editor.putString(String.valueOf(Integer.valueOf(pref_number) + 1), strng_edt);
            editor.commit();
            editor.apply();
        } else {
            if (pref_number.equals("2")) {
                db.updateContact((new Contact(contacts.get(0).getx(), contacts.get(0).get_y(),
                        String.valueOf(Integer.valueOf(Integer.valueOf(contacts.get(0).get_point()) + Integer.valueOf(strng_edt))))), "1");
            } else {
                Integer int_contc = Integer.valueOf(contacts.get(Integer.valueOf(pref_number) - 1).get_point());
                Integer int_edit = Integer.valueOf(edt_point.getText().toString());
                String value = String.valueOf(int_contc +
                        int_edit);
                db.updateContact((new Contact(GlobalConstant.get_current_time(), value, value)), pref_number);

                for (Contact cnt : contacts) {
                    Log.e("value", String.valueOf(cnt.getID()));
                }
            }
            editor.putString("number_column", String.valueOf(Integer.valueOf(pref_number)));
            editor.putString(String.valueOf(Integer.valueOf(pref_number)), strng_edt);
            editor.commit();
            editor.apply();
        }

    }

    private void method_value_add_to_graph_below(){
        {
            btn_create.setVisibility(View.GONE);
            card_create.setVisibility(View.GONE);
            lnr_add_pnt.setVisibility(View.GONE);
            fab_btn_edt_metric.setVisibility(View.VISIBLE);
            btn_add_point.setVisibility(View.VISIBLE);
//            xyPlot.setContentDescription(pref_table_name);


            ArrayList<String> array_x = new ArrayList<>();
            ArrayList<Integer> array_y = new ArrayList<>();
            ArrayList<String> array_point_strng = new ArrayList<>();
            ArrayList<String> labels_uper = new ArrayList<String>();
            String secnd_x = null;
            String secnd_y = null;
            String secnd_pnt = null;


            ArrayList<String> array_threshold_line_y=new ArrayList<>();
            String pref_chang = preferences.getString("change", null);
            for (int i = 0; i <= contacts.size()-1; i++){
                if(i==0){
                    if(contacts.get(0).get_y().equals(pref_start_number)){
                        if(pref_chang.equals("no")){
                            array_threshold_line_y.add("0");
                        }
                        else{
                            array_threshold_line_y.add("0");
                        }

                    }
                    else{
                        array_threshold_line_y.add(contacts.get(0).get_y());
                    }
                }
                else if(i!=1){
                    array_threshold_line_y.add(contacts.get(i).get_y());
                }
            }
//            array_threshold_line_y.add(contacts.get(1).get_y());
            for (int i = 0; i <= contacts.size() - 1; i++)
            {
                if (i == 1)
                {
                    secnd_x = contacts.get(1).getx();
                    secnd_y = contacts.get(1).get_y();
                    secnd_pnt = contacts.get(1).get_point();
                }
                else
                {
                    array_x.add(contacts.get(i).getx());
                    labels.add(contacts.get(i).getx());
                    labels_uper.add("");
                    array_y.add(Integer.valueOf(contacts.get(i).get_y()));
                    array_point_strng.add((contacts.get(i).get_point()));
                    Log.e("value", String.valueOf(contacts.get(i).getID()));
                }
            }
            array_x.add(secnd_x);
            labels.add(secnd_x);
            array_y.add(Integer.valueOf(secnd_y));
            array_point_strng.add(secnd_pnt);

//            ArrayList<Entry> entries = new ArrayList<>();

            for (int i = 0; i < array_point_strng.size(); i++)
            {
                Integer int_value = Integer.valueOf(array_y.get(i));
                Log.e("y", String.valueOf(array_y.get(i)));
//                Entry entry = new Entry(int_value, i);
//                entries.add(entry);
            }

            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();


            String[] mMonth = new String[] {
                    "Jan", "Feb" , "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
            };

            for(int i=0;i<labels.size();i++){
//                multiRenderer.addXTextLabel(i+1, labels.get(i));
            }
            for (int i = 0; i < labels.size(); i++)
            {

                if(i==labels.size()){
                    multiRenderer.addTextLabel(labels.size()+1, labels.get(i));
                }else{
                    multiRenderer.addTextLabel(i, labels.get(i));
                }

            }

            multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
            multiRenderer.setXLabels(0);
            // Adding incomeRenderer and expenseRenderer to multipleRenderer
            // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
            // should be same
            XYSeries incomeSeries = new XYSeries("Threshold line");
            // Creating an  XYSeries for Income
            XYSeries expenseSeries = new XYSeries("Points added");
            XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
            incomeRenderer.setColor(Color.BLUE);

            incomeRenderer.setPointStyle(PointStyle.CIRCLE);
            incomeRenderer.setPointStyle(PointStyle.CIRCLE);
//            incomeRenderer.setFillBelowLine(true);
            incomeRenderer.setLineWidth(8f);
            incomeRenderer.setFillPoints(true);
            incomeRenderer.setLineWidth(2);
            incomeRenderer.setDisplayChartValues(true);
            multiRenderer.setLabelsColor(Color.BLACK);



            XYMultipleSeriesDataset datasetf = new XYMultipleSeriesDataset();
            // Adding Income Series to the dataset
            datasetf.addSeries(incomeSeries);

            incomeSeries.add(0,Integer.valueOf(pref_start_number));
            incomeSeries.add(contacts.size()-1, Integer.valueOf(pref_end_number));
            for (int i=0;i<array_threshold_line_y.size();i++){
                if(i!=array_threshold_line_y.size()){
                    expenseSeries.add(i,Integer.valueOf(array_threshold_line_y.get(i)));
                }

            }
            // Adding Expense Series to dataset
            datasetf.addSeries(expenseSeries);
            // Creating XYSeriesRenderer to customize expenseSeries
            XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
            expenseRenderer.setColor(Color.RED);
            expenseRenderer.setFillPoints(false);
            expenseRenderer.setLineWidth(10);
            incomeRenderer.setColor(Color.BLACK);
            incomeRenderer.getChartValuesTextSize();
            incomeRenderer.setChartValuesTextSize(20);
            incomeRenderer.setChartValuesTextAlign(Paint.Align.CENTER);
            incomeRenderer.setDisplayChartValues(true);


            expenseRenderer.setPointStyle(PointStyle.CIRCLE);
//            expenseRenderer.setFillPoints(true);
            expenseRenderer.setLineWidth(10);
            incomeRenderer.setDisplayChartValues(true);
            expenseRenderer.setDisplayChartValues(true);

            expenseRenderer.setFillBelowLineColor(Color.YELLOW);
//            incomeRenderer.setFillBelowLineColor(R.color.colorAccent);

            /*expenseRenderer.setFillBelowLine(true);
            incomeRenderer.setFillBelowLine(true);*/
            incomeRenderer.setGradientStart(0, Color.rgb(192, 192, 192));
            expenseRenderer.setGradientStart(0, Color.rgb(192, 192, 192));
            expenseRenderer.setDisplayChartValues(true);
            incomeRenderer.setDisplayChartValues(true);
            incomeRenderer.isDisplayChartValues();
            expenseRenderer.isDisplayChartValues();
            expenseRenderer.setGradientEnabled(true);
            incomeRenderer.setGradientEnabled(true);



//            multiRenderer.setDisplayChartValues(true);
            expenseRenderer.setDisplayChartValues(true);
            multiRenderer.setMargins(new int[]{10, 30, 15, 20});
            multiRenderer.addSeriesRenderer(expenseRenderer);
            multiRenderer.addSeriesRenderer(incomeRenderer);

         /*   XYSeriesRenderer xyRenderer = (XYSeriesRenderer) multiRenderer.getSeriesRendererAt(0);

            XYSeriesRenderer.FillOutsideLine  fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.
                    Type.BELOW);
            fill.setColor(Color.MAGENTA);
            xyRenderer.addFillOutsideLine(fill);

            fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BELOW);
            fill.setColor(Color.YELLOW);
            xyRenderer.addFillOutsideLine(fill);

            XYSeriesRenderer xyRenderer2 = (XYSeriesRenderer) multiRenderer.getSeriesRendererAt(1);

            XYSeriesRenderer.FillOutsideLine  fill2 = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.
                    Type.BELOW);
//            fill2.setColor(Color.CYAN);
            xyRenderer2.addFillOutsideLine(fill2);

            fill2 = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BELOW);
//            fill2.setColor(Color.CYAN);
            xyRenderer2.addFillOutsideLine(fill2);*/

            String measurmnt=preferences.getString("measurmnt", "minutes");
            multiRenderer.setYTitle(measurmnt, 0);

            multiRenderer.setMarginsColor(Color.WHITE);
            multiRenderer.setBackgroundColor(Color.BLACK);
            multiRenderer
                    .setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
            multiRenderer.setChartTitleTextSize(18);
            multiRenderer.setAxisTitleTextSize(14);
            multiRenderer.setLabelsTextSize(24);
            multiRenderer.setLabelsColor(Color.BLACK);
            multiRenderer.setLabelsTextSize(20);
            multiRenderer.setXLabelsColor(Color.BLACK);
//            multiRenderer.setChartValuesTextSize(20f);
            multiRenderer.setAntialiasing(true);
            multiRenderer.setZoomEnabled(true);
            multiRenderer.setPanEnabled(true, false);
            multiRenderer.setFitLegend(false);
            multiRenderer.setLegendTextSize(20);
            multiRenderer.setShowLegend(true);
            multiRenderer.setPointSize(10);
            multiRenderer.setShowGrid(true);
            multiRenderer.setZoomButtonsVisible(true);
            multiRenderer.setTextTypeface("sans_serif", Typeface.BOLD);
            multiRenderer.setAxesColor(Color.BLACK);



            multiRenderer.setChartTitleTextSize(20f);
            multiRenderer.setChartTitle(pref_table_name);

            String hh[]=new String[]{"ff","fs"};
//            xyPlot.setData(data);
            String[] types = new String[] { LineChart.TYPE, LineChart.TYPE };
            chartView = ChartFactory.getLineChartView(getBaseContext(), datasetf, multiRenderer);
//            chart.setOnClickListener(this);
            chartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection seriesSelection = chartView.getCurrentSeriesAndPoint();

                    if (seriesSelection != null) {
                        int seriesIndex = seriesSelection.getSeriesIndex();
                        String selectedSeries="Income";
                        if(seriesIndex==0)
                            selectedSeries = "Income";
                        else
                            selectedSeries = "Expense";
                        // Getting the clicked Month
                        String month = labels.get((int)seriesSelection.getXValue());
                        // Getting the y value
                        int amount = (int) seriesSelection.getValue();
                        Toast.makeText(
                                getBaseContext(),
                                selectedSeries + " in "  + month + " : " + amount ,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            chart.addView(chartView);


        }
    }


}
