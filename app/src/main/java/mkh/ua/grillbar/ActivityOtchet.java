package mkh.ua.grillbar;

import android.app.Activity;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Lenovo on 29.01.2017.
 */

public class ActivityOtchet extends Activity implements View.OnClickListener{

    SQLiteDatabase db;
    DBHelper dbHelper;
    TextView txt24,txt25, txt241, txt31, txt29, txt27, txt312, txt292, txt272,
    txt311, txt291, txt271;
    ToggleButton tb1, tb2, tb3;
    LinearLayout day, week, month;
    ListView bluda;
    ArrayAdapter<Bludo> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otchet);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        txt24 = (TextView) findViewById(R.id.textView24);
        txt25 = (TextView) findViewById(R.id.textView25);
        txt25.setVisibility(View.GONE);
        //txt241 = (TextView) findViewById(R.id.textView241);
        txt31 = (TextView) findViewById(R.id.textView31);
        txt29 = (TextView) findViewById(R.id.textView29);
        txt27 = (TextView) findViewById(R.id.textView27);
        txt312 = (TextView) findViewById(R.id.textView312);
        txt292 = (TextView) findViewById(R.id.textView292);
        txt272 = (TextView) findViewById(R.id.textView272);
        txt311 = (TextView) findViewById(R.id.textView311);
        txt291 = (TextView) findViewById(R.id.textView291);
        txt271 = (TextView) findViewById(R.id.textView271);
        tb1 = (ToggleButton) findViewById(R.id.toggleButton5);
        tb2 = (ToggleButton) findViewById(R.id.toggleButton4);
        tb3 = (ToggleButton) findViewById(R.id.toggleButton3);

        tb1.setOnClickListener(this);
        tb2.setOnClickListener(this);
        tb3.setOnClickListener(this);

        day = (LinearLayout) findViewById(R.id.day);
        week = (LinearLayout) findViewById(R.id.week);
        month = (LinearLayout) findViewById(R.id.month);

        bluda = (ListView) findViewById(R.id.bluda);
        adapter = new AdapterBludo(this);
        bluda.setAdapter(adapter);

        Calendar date = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(date.getTime());

        try {
            getDohod();
            getDohod7dayback();
            getDohod7day();
            getDohodMonth();
            tb1.setChecked(true);
            getTopBludo();
        }
        catch (Exception f){

        }

    }



    public void getTopBludo (){
        Bludo ginger = null;
        //Cursor c  = db.rawQuery("Select * from bludo", null);
        Cursor c = db.query("bludo", null, null, null, null, null, "kol DESC");
        //db.delete("vremenno", null, null);
        adapter.clear();

        try {
            String summa = null;
            if (c != null ) {

                if  (c.moveToFirst()) {


                    do {

                        String id = c.getString(c.getColumnIndex("id"));
                        String name = c.getString(c.getColumnIndex("name"));
                        String kol = c.getString(c.getColumnIndex("kol"));
                        String coast = c.getString(c.getColumnIndex("coast"));

                        summa = String.valueOf(Integer.valueOf(kol) * Integer.valueOf(coast));

                        ginger = new Bludo(name, kol, coast, summa );






                        adapter.add(ginger);
                        adapter.notifyDataSetChanged();

                    }while (c.moveToNext());


                }
                else{
                    Log.e("!!", "пустая таблица");


                }

            }


        } catch (Exception se ) {

        }
    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            getTopBludo();
        } catch (Exception t) {

        }

    }


    private static final List<Bludo> cats = new ArrayList<Bludo>();

    private class AdapterBludo extends ArrayAdapter<Bludo> {

        ArrayList<String> pagelist;

        public AdapterBludo(Context context) {
            super(context, R.layout.list_bludo, cats);
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final Bludo food = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_bludo, null);
            }
            ((TextView) convertView.findViewById(R.id.textView37))
                    .setText(food.name);
            ((TextView) convertView.findViewById(R.id.textView36))
                    .setText(food.kol+ " шт.");
            ((TextView) convertView.findViewById(R.id.textView35))
                    .setText(food.coast + " грн.");
            ((TextView) convertView.findViewById(R.id.textView34))
                    .setText(food.summa + " грн.");


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    //int delCount1 = db.delete("vremenno", "name = " + "'" + food.name + "'" , null);

                    //adapter.remove(adapter.getItem(position));
                    //adapter.notifyDataSetChanged();

                }
            });

            return convertView;
        }
    }

    private void getDohod() {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(date.getTime());

        String query = "SELECT * FROM zakazi where date LIKE '%"+formattedDate+"%'";

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        if (mCursor.moveToFirst()) {

            int id = mCursor.getColumnIndex("id");
            int summa_zakaz = mCursor.getColumnIndex("summa_zakaz");
            int grn = 0;
            int kol = 0;
            int sr_ch;

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("LOG_TAG",
                        "id = " + mCursor.getString(id) +
                                ", name = " + mCursor.getString(summa_zakaz));
                grn = grn + mCursor.getInt(summa_zakaz);
                kol = kol + 1;
                sr_ch = grn/kol;

                txt24.setText(String.valueOf(grn)+ " грн.");
                txt311.setText(" " + String.valueOf(grn)+ " грн.");
                txt291.setText(" " + String.valueOf(kol)+ " шт.");
                txt271.setText(" " + String.valueOf(sr_ch) + " грн.");


                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла

            } while (mCursor.moveToNext());
        } else
            Log.d("LOG_TAG", "0 rows zakazi day");
    }

    private void getDohod7day() {
        int sum = 0;
        int kol = 0;
        int sr_ch = 0;

        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(new Date());
        cal1.add(Calendar.DAY_OF_YEAR, -0);

        int weekOfYear1 = cal1.get(Calendar.WEEK_OF_YEAR);

        for (int i = 0; i < 7; i++) {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, -i);
            Date ee = cal.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String formattedDate = df.format(ee);
            int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

            if (weekOfYear1 == weekOfYear) {

                String query = "SELECT * FROM zakazi where date LIKE '%" + formattedDate + "%'";


                Cursor mCursor = db.rawQuery(query, null);
                if (mCursor != null) {
                    mCursor.moveToFirst();
                }

                if (mCursor.moveToFirst()) {

                    int id = mCursor.getColumnIndex("id");
                    int summa_zakaz = mCursor.getColumnIndex("summa_zakaz");
                    int grn = 0;

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d("LOG_TAG",
                                "id = " + mCursor.getString(id) +
                                        ", name = " + mCursor.getString(summa_zakaz));
                        sum = sum + mCursor.getInt(summa_zakaz);
                        kol = kol + 1;
                        sr_ch = sum/kol;

                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла

                    } while (mCursor.moveToNext());
                } else {
                    Log.d("LOG_TAG", "0 rows zakazi 7 day");
                }

            }
            txt312.setText(" " + String.valueOf(sum) + " грн.");
            txt292.setText(" " + String.valueOf(kol) + " шт.");
            txt272.setText(" " + String.valueOf(sr_ch) + " грн.");

        }
    }

    private void getDohod7dayback() {



        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -7);
        Date ee = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(ee);


        String query = "SELECT * FROM zakazi where date LIKE '%"+formattedDate+"%'";

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        if (mCursor.moveToFirst()) {

            int id = mCursor.getColumnIndex("id");
            int summa_zakaz = mCursor.getColumnIndex("summa_zakaz");
            int grn = 0;

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                /*Log.d("LOG_TAG",
                        "id = " + mCursor.getString(id) +
                                ", name = " + mCursor.getString(summa_zakaz));*/
                grn = grn + mCursor.getInt(summa_zakaz);

                txt25.setText("неделю назад: " + String.valueOf(grn)+ " грн.");
                txt25.setVisibility(View.VISIBLE);
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла

            } while (mCursor.moveToNext());
        } else {
            Log.e("LOG_TAG", "Нет периода неделя назад");

        }
    }


    private void getDohodMonth() {
        int sum = 0;
        int kol = 0;
        int sr_ch = 0;
        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(new Date());
        cal1.add(Calendar.DAY_OF_YEAR, -0);

        int monthOfYear1 = cal1.get(Calendar.MONTH);



        for (int i = 0; i < 30; i++) {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, -i);
            Date ee = cal.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String formattedDate = df.format(ee);
            int monthOfYear = cal.get(Calendar.MONTH);

            if (monthOfYear1 == monthOfYear) {

            }
            String query = "SELECT * FROM zakazi where date LIKE '%" + formattedDate + "%'";

            Cursor mCursor = db.rawQuery(query, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
            }

            if (mCursor.moveToFirst()) {

                int id = mCursor.getColumnIndex("id");
                int summa_zakaz = mCursor.getColumnIndex("summa_zakaz");
                int grn = 0;

                do {
                    // получаем значения по номерам столбцов и пишем все в лог
                    Log.d("LOG_TAG",
                            "id = " + mCursor.getString(id) +
                                    ", name = " + mCursor.getString(summa_zakaz));
                    sum = sum + mCursor.getInt(summa_zakaz);
                    kol = kol + 1;
                    sr_ch = sum/kol;


                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false - выходим из цикла

                } while (mCursor.moveToNext());
            } else {
                Log.d("LOG_TAG", "0 rows zakazi month");
            }

        }
        txt31.setText(" " + String.valueOf(sum) + " грн.");
        txt29.setText(" " + String.valueOf(kol) + " шт.");
        txt27.setText(" " + String.valueOf(sr_ch) + " грн.");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.toggleButton5:
                day.setVisibility(View.VISIBLE);
                week.setVisibility(View.GONE);
                month.setVisibility(View.GONE);
                tb2.setChecked(false);
                tb3.setChecked(false);
            break;

            case R.id.toggleButton4:
                day.setVisibility(View.GONE);
                week.setVisibility(View.VISIBLE);
                month.setVisibility(View.GONE);
                tb1.setChecked(false);
                tb3.setChecked(false);

                break;

            case R.id.toggleButton3:
                day.setVisibility(View.GONE);
                week.setVisibility(View.GONE);
                month.setVisibility(View.VISIBLE);
                tb2.setChecked(false);
                tb1.setChecked(false);

                break;

            default:

                break;
        }

    }
}