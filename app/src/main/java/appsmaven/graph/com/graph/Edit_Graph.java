package appsmaven.graph.com.graph;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;
import graph.service.ServiceActivity;
import graph.sqlite.Contact;
import graph.sqlite.DatabaseHandler;


public class Edit_Graph extends AppCompatActivity implements View.OnClickListener {
    static String change = "yes";
    @InjectView(R.id.edt_vw_target_start)
    EditText edt_vw_target_start;
    @InjectView(R.id.edt_vw_target_end)
    EditText edt_vw_target_end;
    @InjectView(R.id.edt_vw_table_name)
    EditText edt_vw_table_name;
    @InjectView(R.id.edt_notify_days)
    EditText edt_notify_days;
    @InjectView(R.id.txt_vw_date_start)
    TextView txt_vw_date_start;
    @InjectView(R.id.txt_vw_date_end)
    TextView txt_vw_date_end;
    @InjectView(R.id.txt_vw_time_remindr)
    TextView txt_vw_time_remindr;
    @InjectView(R.id.txt_freq)
    TextView txt_freq;
    @InjectView(R.id.txt_vw_get_above)
    TextView txt_vw_get_above;
    @InjectView(R.id.lnr_vw_start_date)
    LinearLayout lnr_vw_start_date;
    @InjectView(R.id.lnr_vw_date_end)
    LinearLayout lnr_vw_date_end;
    @InjectView(R.id.lnr_vw_target_start)
    LinearLayout lnr_vw_target_start;
    @InjectView(R.id.lnr_vw_get_abov)
    LinearLayout lnr_vw_get_abov;
    @InjectView(R.id.lnr_vw_frequency)
    LinearLayout lnr_vw_frequency;
    @InjectView(R.id.lnr_vw_target_end)
    LinearLayout lnr_vw_target_end;
    @InjectView(R.id.btn_save)
    Button btn_save;
    @InjectView(R.id.btn_cancel)
    Button btn_cancel;
    @InjectView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.radio_grp)
    RadioGroup radioGroup;
    @InjectView(R.id.txt_measurmnt)
    EditText txt_measurmnt;
    @InjectView(R.id.chk_reminder_yes)
    CheckBox chk_reminder_yes;
    int year, month, day;
    Calendar calendar;
    String calndr = "";
    TimePicker timePicker1;
    String format = "";
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> numbers = new ArrayList<>();
    String get_intent, number_column;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    DatabaseHandler db = new DatabaseHandler(this);
    List<Contact> contacts;
    RadioButton radioButton;
    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            if (calndr.equals("start")) {
                {
                    Date start_date = null;
                    Date end_date = null;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy"); // Set your date format

                        start_date = sdf.parse(GlobalConstant.method_get_current_date());
                        end_date = sdf.parse(new StringBuilder().append(month +1)
                                .append("-").append(day).append("-").append(year)
                                .append(" ").toString());
                    } catch (Exception ex) {

                    }
                    if (end_date.before(start_date)) {
                        GlobalConstant.snackbar_method("Enter more than current date", coordinatorLayout);
                        txt_vw_date_end.setText("");
                        txt_vw_date_start.setText("");

                    } else {
                        txt_vw_date_start.setText(new StringBuilder().append(month +1)
                                .append("-").append(day).append("-").append(year)
                                .append(" "));
                    }
                }
                txt_vw_date_end.setText("");
            } else {
                Date start_date = null;
                Date end_date = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy"); // Set your date format

                    start_date = sdf.parse(txt_vw_date_start.getText().toString());
                    end_date = sdf.parse(new StringBuilder().append(month+1 )
                            .append("-").append(day).append("-").append(year)
                            .append(" ").toString());
                } catch (Exception ex) {

                }
                if (end_date.after(start_date)) {
                    txt_vw_date_end.setText(new StringBuilder().append(month+1)
                            .append("-").append(day).append("-").append(year)
                            .append(" "));
                } else {
                    GlobalConstant.snackbar_method("Enter date after start date", coordinatorLayout);
                    txt_vw_date_end.setText("");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_graph);

        ButterKnife.inject(this);
        setActionToolbar();

        OnClicklistner();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        preferences = PreferenceManager
                .getDefaultSharedPreferences(Edit_Graph.this);
        editor = preferences.edit();
        number_column = preferences.getString("number_column", null);

        method_get_intent();
        addListenerOnButton();
    }

    private void setActionToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Graph");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void method_get_intent() {
        Intent intnt_get = getIntent();
        get_intent = intnt_get.getStringExtra("edit");
        if (get_intent != null) {
            if (get_intent.equals("edit")) {
                String pref_table_name = preferences.getString("table_name", null);
                String pref_measurmnt = preferences.getString("measurmnt", "minutes");
                String end_target = preferences.getString("end_number", null);
                String goal = preferences.getString("goal", "Get at or above");
                String reminder_chk = preferences.getString("reminder_chk", "yes");
                String txt_reminder_time = preferences.getString("reminder", GlobalConstant.get_current_time());
                String start_target = preferences.getString("edt_vw_target_start.getText().toString()" + "0", null);

                contacts = db.getAllContacts();

                String start_date = contacts.get(0).getx();
                String end_date = contacts.get(1).getx();

                if (reminder_chk.equals("yes")) {
                    chk_reminder_yes.setChecked(true);
                    txt_vw_time_remindr.setText(txt_reminder_time);
                } else {
                    chk_reminder_yes.setChecked(false);

                }

                edt_vw_table_name.setText(pref_table_name);
                txt_vw_date_start.setText(start_date);
                txt_measurmnt.setText(pref_measurmnt);
                txt_vw_date_end.setText(end_date);
                edt_vw_target_end.setText(end_target);
                edt_vw_target_start.setText(start_target);
                if (goal.equals("above")) {
                    txt_vw_get_above.setText("Get at or above");
                } else {
                    txt_vw_get_above.setText("Get at or below");
                }
            }
        }
    }

    private void OnClicklistner() {
        lnr_vw_date_end.setOnClickListener(this);
        lnr_vw_start_date.setOnClickListener(this);
        txt_vw_time_remindr.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        lnr_vw_target_start.setOnClickListener(this);
        lnr_vw_target_end.setOnClickListener(this);
        lnr_vw_get_abov.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnr_vw_start_date:
                change = "no";
                txt_vw_date_start.setText("");
                calndr = "start";
                showDialog(100);
                break;

            case R.id.lnr_vw_get_abov:
                show_frequency_dialog("target");
                break;


            case R.id.lnr_vw_date_end:
                change = "no";
                if (txt_vw_date_start.getText().toString().length() > 0) {
                    calndr = "end";
                    showDialog(100);
                } else {
                    GlobalConstant.snackbar_method("Enter start date first", coordinatorLayout);
                }
                break;

            case R.id.txt_vw_time_remindr:
                show_time_dialog();
                break;

            case R.id.btn_cancel:
                Edit_Graph.this.finish();
                break;

            case R.id.lnr_vw_target_start:
                edt_vw_target_start.requestFocus();
                break;

            case R.id.lnr_vw_target_end:
                edt_vw_target_end.requestFocus();
                break;

            case R.id.btn_save:
                method_check_validations();
                break;
        }
    }

    public void setTime(View view) {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);
    }

    public void method_check_validations() {
        if (edt_vw_table_name.getText().toString().length() <= 0) {
            GlobalConstant.snackbar_method("Enter table name", coordinatorLayout);
        } else if (txt_vw_date_start.getText().toString().length() <= 0) {
            GlobalConstant.snackbar_method("Enter start date", coordinatorLayout);
        } else if (txt_vw_date_end.getText().toString().length() <= 0) {
            GlobalConstant.snackbar_method("Enter end date", coordinatorLayout);
        } else if (edt_vw_target_start.getText().toString().length() <= 0) {
            GlobalConstant.snackbar_method("Enter start target value", coordinatorLayout);
        } else if (edt_vw_target_end.getText().toString().length() <= 0) {
            GlobalConstant.snackbar_method("Enter end target value", coordinatorLayout);
        } else if (chk_reminder_yes.isChecked() == true) {
            if (txt_vw_time_remindr.getText().toString().length() >= 0) {
                editor.putString("reminder_chk", "yes");
                editor.commit();
                editor.apply();
                method_start_intent();
            } else {
                editor.putString("reminder_chk", "no");
                editor.commit();
                editor.apply();
                GlobalConstant.snackbar_method("Enter reminder time", coordinatorLayout);
            }
        } else {
            editor.putString("reminder_chk", "no");
            editor.commit();
            editor.apply();
            method_start_intent();
        }
    }

    private void method_start_intent() {
        Intent myIntent = new Intent(Edit_Graph.this, ServiceActivity.class);
        startService(myIntent);

        if (preferences.getString("number_column", null) == null) {
            editor.putString("change", "no");
            db.addContact(new Contact(txt_vw_date_start.getText().toString(), edt_vw_target_start.getText().toString(), edt_vw_target_start.getText().toString()));
            db.addContact(new Contact(txt_vw_date_end.getText().toString(), edt_vw_target_end.getText().toString(), edt_vw_target_end.getText().toString()));
        } else {
            if (change == "no") {
                db.Delete();
                editor.putString("change", "no");
                db.addContact(new Contact(txt_vw_date_start.getText().toString(), edt_vw_target_start.getText().toString(), edt_vw_target_start.getText().toString()));
                db.addContact(new Contact(txt_vw_date_end.getText().toString(), edt_vw_target_end.getText().toString(), edt_vw_target_end.getText().toString()));
            } else {
                List<Contact> contacts = db.getAllContacts();
                editor.putString("change", "yes");
                db.updateContact((new Contact(contacts.get(0).getx(), edt_vw_target_start.getText().toString(), edt_vw_target_start.getText().toString())), "1");
                db.updateContact((new Contact(contacts.get(1).getx(), edt_vw_target_end.getText().toString(), edt_vw_target_end.getText().toString())), "2");
            }
        }

        //////////////adding in shared prefrence
        editor.putString("edt_vw_target_start.getText().toString()" + "0", edt_vw_target_start.getText().toString());
        editor.putString("end_number", edt_vw_target_end.getText().toString());
        editor.putString("end_date", txt_vw_date_end.getText().toString());
        editor.putString("start_date", txt_vw_date_start.getText().toString());
        editor.putString("table_name", edt_vw_table_name.getText().toString());
        editor.putString("number_column", "2");
        if (txt_measurmnt.getText().toString().length() > 0) {
            editor.putString("measurmnt", txt_measurmnt.getText().toString());
        } else {
            editor.putString("measurmnt", "minutes");
        }

        editor.putString("time_start", txt_vw_date_start.getText().toString());
        editor.putString("time_after_four", GlobalConstant.get_next_day(1, txt_vw_date_start.getText().toString()));
        if (txt_vw_get_above.getText().toString().equals("Get at or below")) {
            editor.putString("goal", "below");
        } else {
            editor.putString("goal", "above");
        }
        editor.commit();
        editor.apply();

        ArrayList<Date> dates_ = GlobalConstant.getDates(txt_vw_date_start.getText().toString(),
                txt_vw_date_end.getText().toString());
        if (Integer.valueOf(edt_vw_target_end.getText().toString()) > Integer.valueOf(edt_vw_target_start.getText().toString())) {
            numbers = GlobalConstant.range_number(edt_vw_target_start.getText().toString(),
                    edt_vw_target_end.getText().toString());
        } else {
            numbers = GlobalConstant.range_number(edt_vw_target_end.getText().toString(),
                    edt_vw_target_start.getText().toString());
        }

        for (Date date : dates_) {
            dates.add(date.toString());
        }

        Intent i = new Intent(Edit_Graph.this, MainActivity.class);
        i.putExtra("start_number", edt_vw_target_start.getText().toString());
        i.putExtra("end_number", edt_vw_target_end.getText().toString());
        i.putExtra("start_date", txt_vw_date_start.getText().toString());
        i.putExtra("end_date", txt_vw_date_end.getText().toString());
        i.putExtra("table_name", edt_vw_table_name.getText().toString());
        i.putStringArrayListExtra("date", dates);
        i.putStringArrayListExtra("number", numbers);

        startActivity(i);
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 100:
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                SimpleDateFormat df1 = new SimpleDateFormat("MM-dd-yyyy");
                return new DatePickerDialog(this, datePickerListener,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private void show_time_dialog() {
        final Dialog dialog = new Dialog(Edit_Graph.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_time_pickr);

        timePicker1 = (TimePicker) dialog.findViewById(R.id.timePicker);
        final int hour = timePicker1.getCurrentHour();
        final int min = timePicker1.getCurrentMinute();

        Button btndialog_cancel = (Button) dialog.findViewById(R.id.btndialog_cancl);
        Button btndialog_ok = (Button) dialog.findViewById(R.id.btndialog_ok);
        btndialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_vw_time_remindr.setText("");
                dialog.dismiss();
            }
        });
        btndialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(v);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void show_frequency_dialog(final String title) {
        final Dialog dialog = new Dialog(Edit_Graph.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_freq);

        TextView txt_everyday = (TextView) dialog.findViewById(R.id.dialog_txt_everyday);
        TextView txt_week = (TextView) dialog.findViewById(R.id.txt_dialog_week);

        if (title.equals("frequency")) {
            txt_everyday.setText("Everyday");
            txt_week.setText("Week");
        } else {

            txt_everyday.setText("Get at or below");
            txt_week.setText("Get at or above");
        }
        txt_everyday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.equals("frequency")) {
                    txt_freq.setText("Everyday");
                } else {
                    txt_vw_get_above.setText("Get at or below");
                }
                dialog.dismiss();
            }
        });


        txt_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.equals("frequency")) {
                    txt_freq.setText("Week");
                } else {

                    txt_vw_get_above.setText("Get at or above");
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public String showTime(int hour, int min) {
        StringBuilder builder_time;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        int length = (int) (Math.log10(min) + 1);
        builder_time = new StringBuilder().append(hour).append(":").append("0" + min)
                .append(" ").append(format);
        if (length == 1) {
            builder_time = new StringBuilder().append(hour).append(":").append("0" + min)
                    .append(" ").append(format);
        } else {
            builder_time = new StringBuilder().append(hour).append(":").append(min)
                    .append(" ").append(format);
        }
        if (min == 0) {
            builder_time = new StringBuilder().append(hour).append(":").append("00")
                    .append(" ").append(format);
        }
        StringBuilder build_long= new StringBuilder().append(hour).append(":").append("00")
                .append(" ");
        txt_vw_time_remindr.setText(builder_time.toString());

        editor.putString("reminder", txt_vw_time_remindr.getText().toString());
        Time t = Time.valueOf("10:06:00");
        long l = t.getTime();
        editor.putString("hour", String.valueOf(hour));
        editor.putString("min", String.valueOf(min));
        editor.putString("time", String.valueOf(builder_time));
        editor.putString("format", format);
        editor.putLong("long_time", 0);
        Log.e("format in edit", format);
        editor.commit();
        editor.apply();

        return builder_time.toString();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (number_column != null) {
            Intent i = new Intent(Edit_Graph.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (number_column != null) {
                    Intent i = new Intent(Edit_Graph.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    ////radio button selector
    public void addListenerOnButton() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                radioButton = (RadioButton) findViewById(selectedId);

                if (selectedId == R.id.radio_daily) {
                    editor.putString("number_days", "1");

                } else if (selectedId == R.id.radio_daily) {
                    editor.putString("number_days", edt_notify_days.getText().toString());
                    editor.commit();
                    editor.apply();

                }
            }
        });
    }
}

