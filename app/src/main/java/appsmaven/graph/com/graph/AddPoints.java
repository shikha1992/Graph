package appsmaven.graph.com.graph;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import graph.sqlite.Contact;
import graph.sqlite.DatabaseHandler;


public class AddPoints extends AppCompatActivity implements View.OnClickListener {
    @InjectView(R.id.edt_point)
    EditText edt_point;
    @InjectView(R.id.btn_save)
    Button btn_save;
    @InjectView(R.id.txt_vw_time)
    TextView txt_vw_time;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String pref_number, pref_index, pref_time, pref_time_after_four_hr,
            current_time, strng_edt, table_name, pref_chang;
    DatabaseHandler db = new DatabaseHandler(this);
    Integer start_number, end_number;
    List<Contact> contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_daily_points);

        ButterKnife.inject(this);
        method_settoolbar();
        preferences = PreferenceManager
                .getDefaultSharedPreferences(AddPoints.this);
        editor = preferences.edit();
        OnClicklistner();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        if (checkDataBase()) {
            contacts = db.getAllContacts();
        }

    }

    private void method_settoolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Points");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void method_set_prefrenceData() {
        pref_number = preferences.getString("number_column", null);
        table_name = preferences.getString("number_column", null);
        pref_index = preferences.getString("index", null);
        pref_time = preferences.getString("time_start", null);
        pref_chang = preferences.getString("change", null);
        pref_time_after_four_hr = preferences.getString("time_after_four", null);
        String end_number_ = preferences.getString("end_number", null);
        String start_number_ = preferences.getString("edt_vw_target_start.getText().toString()" + "0", null);
        end_number = Integer.valueOf(end_number_);
        start_number = Integer.valueOf(start_number_);
        current_time = GlobalConstant.method_get_current_date();
    }

    private void OnClicklistner() {
        edt_point.requestFocus();
        method_set_prefrenceData();

        if (GlobalConstant.compare_dates(current_time, pref_time_after_four_hr)) {
            txt_vw_time.setText("Enter point for " + current_time);
        } else {
            txt_vw_time.setText("Enter point for " + pref_time);
        }

        btn_save.setOnClickListener(this);
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(db.DB_PATH + db.DATABASE_NAME, null,
                    SQLiteDatabase.OPEN_READONLY);
            DatabaseHandler dbHelper = new DatabaseHandler(this);
            dbHelper.openDataBase();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }


    @Override
    public void onClick(View view) {
        if (view == btn_save) {
            strng_edt = edt_point.getText().toString();

            if (strng_edt.length() > 0) {
                method_add_point(view);
            } else {
                InputMethodManager imm = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                GlobalConstant.snackbar_method("Enter point to add first", view);
            }
        }
    }

    private void method_add_point(View view) {
        method_add_point_inrange();
    }


    private void method_add_point_inrange() {
        if (GlobalConstant.compare_dates(current_time, pref_time_after_four_hr)) {
            if (pref_chang.equals("no")) {
                editor.putString("change", "yes");
            }
            editor.putString("time_start", current_time);
            editor.putString("time_after_four", GlobalConstant.get_next_day(1, current_time));
            editor.commit();
            editor.apply();

            db.addContact((new Contact(current_time, strng_edt, String.valueOf(Integer.valueOf(strng_edt)))));
            editor.putString("number_column", String.valueOf(Integer.valueOf(pref_number) + 1));
            editor.putString(String.valueOf(Integer.valueOf(pref_number) + 1), strng_edt);
            editor.commit();
            editor.apply();
        } else {
            if (pref_chang.equals("no")) {
                editor.putString("change", "yes");
            }
            if (pref_number.equals("2")) {
                Integer int_contc = Integer.valueOf(contacts.get(0).get_point());
                Integer int_edit = Integer.valueOf(edt_point.getText().toString());
                String value = null;

                if (pref_chang.equals("no")) {
                    value = String.valueOf(
                            int_edit);
                    editor.putString("change", "yes");
                } else {
                    value = String.valueOf(int_contc +
                            int_edit);
                }
                db.updateContact((new Contact(contacts.get(0).getx(), value, value)), "1");
            } else {
                Integer int_contc = Integer.valueOf(contacts.get(Integer.valueOf(pref_number) - 1).get_point());
                Integer int_edit = Integer.valueOf(edt_point.getText().toString());
                String value = String.valueOf(int_contc +
                        int_edit);
                db.updateContact((new Contact(current_time, value, value)), pref_number);
            }

            editor.putString("number_column", String.valueOf(Integer.valueOf(pref_number)));
            editor.putString(String.valueOf(Integer.valueOf(pref_number)), strng_edt);
            editor.commit();
            editor.apply();
        }
        Intent i = new Intent(AddPoints.this, MainActivity.class);
        startActivity(i);
        AddPoints.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddPoints.this, MainActivity.class);
        startActivity(i);
        AddPoints.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(AddPoints.this, MainActivity.class);
                startActivity(i);
                AddPoints.this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

