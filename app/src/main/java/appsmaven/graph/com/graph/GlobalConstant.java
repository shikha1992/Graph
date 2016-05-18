package appsmaven.graph.com.graph;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class GlobalConstant {
    public static Snackbar snackbar;

    public static ArrayList<Date> getDates(String dateString1, String dateString2) {
        ArrayList<Date> dates = new ArrayList<Date>();
//        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DAY_OF_WEEK, 1);
        }
        return dates;
    }


    public static ArrayList<String> range_number(String number_start, String number_end) {
        ArrayList<String> numbers = new ArrayList<String>();
        int start = Integer.valueOf(number_start);
        int end = Integer.valueOf(number_end);

        for (int i = start; i <= end; i++) {
            numbers.add(String.valueOf(i));
        }

        return numbers;
    }

    public static String get_current_time() {
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();

        DateFormat date = new SimpleDateFormat("hh:mm a");
// you can get seconds by adding  "...:ss" to it
        //  date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        String localTime = date.format(currentLocalTime);

        return localTime;
    }
    public static String get_current_time_hour() {
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("kk:mm");
        int hours = currentLocalTime.getHours();
// you can get seconds by adding  "...:ss" to it
        //  date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        String hour = String.valueOf(hours);

        return hour;
    }


    public static String get_current_time_min() {
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("kk:mm");
        int min = currentLocalTime.getMinutes();
// you can get seconds by adding  "...:ss" to it
        //  date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        String min_ = String.valueOf(min);

        return min_;
    }


    public static String get_next_four_hr_time() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("kk:mm");
// you can get seconds by adding  "...:ss" to it
        String localTime = date.format(currentLocalTime);

        return localTime;
    }

    public static boolean compare_time(String start, String next) {
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
        Date after_4_hr = null;
        try {
            after_4_hr = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date curnt_time = null;
        try {
            curnt_time = sdf.parse(next);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (curnt_time.compareTo(after_4_hr) >= 0) {
            return true;
        } else {
            return false;
        }
    }


    public static String convert_time_12hour(String time) {
        Date dateObj = null;
        String changed_time = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            dateObj = sdf.parse(time);
            System.out.println(dateObj);
            System.out.println(new SimpleDateFormat("K:mm").format(dateObj));
            changed_time = new SimpleDateFormat("K:mm aa").format(dateObj);
        } catch (final ParseException e) {
            changed_time = "";
        }
        return changed_time;
    }

    public static void snackbar_method(String text, View view) {
        snackbar = Snackbar
                .make(view, text, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }


    public static boolean compare_dates(String currentdate, String prefr_date) {
        Date start_date = null;
        Date end_date = null;
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Set your date format
            DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            start_date = sdf.parse(currentdate);
            end_date = sdf.parse(prefr_date);

        } catch (Exception ex) {
            String dex=ex.toString();
            Log.e("exceptn", dex);
            return false;
        }
        if (start_date.before(end_date)) {
            return false;
        }
        else {
            return true;
        }

    }

    public static boolean compare_dates_main_clas(String currentdate, String prefr_date) {
        Date start_date = null;
        Date end_date = null;
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Set your date format
            DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            start_date = sdf.parse(currentdate);
            end_date = sdf.parse(prefr_date);

        } catch (Exception ex) {
            String dex=ex.toString();
            Log.e("exceptn", dex);
            return false;
        }
        if (start_date.after(end_date)) {
            return false;
        }
        else {
            return true;
        }

    }
    public static boolean compare_dates_db(String currentdate, String prefr_date) {
        Date start_date = null;
        Date end_date = null;
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Set your date format
            DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            start_date = sdf.parse(currentdate);
            end_date = sdf.parse(prefr_date);

        } catch (Exception ex) {
            String dex=ex.toString();
            Log.e("exceptn", dex);
            return false;
        }
        if (start_date.equals(end_date)) {
            return true;
        }
        else {
            return false;
        }

    }


    public static String method_get_current_date() {
        String date = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("MM-dd-yyyy");
        date = df1.format(c.getTime());

        return date;
    }

    public static String get_next_day(Integer day, String entered_date) {
        String date = null;
        Calendar startDate = Calendar.getInstance();


        SimpleDateFormat df1 = new SimpleDateFormat("MM-dd-yyyy");
        try {
            startDate.setTime(df1.parse(entered_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startDate.add(Calendar.DATE, 1);
        df1 = new SimpleDateFormat("MM-dd-yyyy");
        Date resultdate = new Date(startDate.getTimeInMillis());
        date = df1.format(resultdate);
        System.out.println("String date:" + date);

        Log.e("date", date);
        return date;
    }



}
