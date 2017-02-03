package mkh.ua.grillbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    Button new_order, menu;
    ListView lv;

    SQLiteDatabase db;
    DBHelper dbHelper;
    ArrayAdapter<Zakaz> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new_order = (Button) findViewById(R.id.button);
        new_order.setOnClickListener(this);
        menu = (Button) findViewById(R.id.button31);
        menu.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        lv = (ListView) findViewById(R.id.listview);


        adapter = new CatAdapter3(this);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {

                String itemJson = ((TextView) itemClicked.findViewById(R.id.json)).getText().toString();
                String itemNumber = ((TextView) itemClicked.findViewById(R.id.number_zakaza)).getText().toString();
                String itemSumma = ((TextView) itemClicked.findViewById(R.id.summa)).getText().toString();


                showDialog(itemNumber, itemSumma, itemJson);
            }
        });


    }

    private void showDialog(String number_zakaza, String summa, String json) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_order_oplacheno);


        ListView list = (ListView) dialog.findViewById(R.id.zakaz);

        Button close = (Button) dialog.findViewById(R.id.button39);


        // set the custom dialog components - text, image and button
        //TextView text = (TextView) dialog.findViewById(R.id.text);
       // text.setText("Android custom dialog example!");
        ArrayList<HashMap<String, String>> contactList = null;
        contactList = new ArrayList<>();
        
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONArray spisok = jsonObj.getJSONArray("spisok");

            // looping through All Contacts
            for (int i = 0; i < spisok.length(); i++) {
                JSONObject c = spisok.getJSONObject(i);


                String name = c.getString("name");
                String quantity = c.getString("quantity");
                String coast = c.getString("coast");

                Log.d("", "name:" + name+"quan:"+quantity+"coast:"+coast);
                // tmp hash map for single contact
                HashMap<String, String> contact1 = new HashMap<>();

                // adding each child node to HashMap key => value

                contact1.put("name", name);
                contact1.put("quantity", quantity);
                contact1.put("coast", coast + " грн.");

                // adding contact to contact list
                contactList.add(contact1);

                }


        }
        catch (Exception v){
Log.e("", v.toString());
        }
        ListAdapter adapter = new SimpleAdapter(this, contactList, R.layout.list_zakaz, new String[]{"name", "quantity",
                "coast"}, new int[]{R.id.textView22,R.id.button30, R.id.textView21});

        list.setAdapter(adapter);



        Button Button6 = (Button) dialog.findViewById(R.id.button6);
        Button6.setText(number_zakaza);
        TextView itog = (TextView) dialog.findViewById(R.id.textView2);
        itog.setText(summa);
        // if button is clicked, close the custom dialog
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


        }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent order = new Intent(this, NewOrder.class);
                order.putExtra("sample_name", 1);

                startActivity(order);
                break;

            case R.id.button31:
                Intent menu = new Intent(this, MenuActivity.class);
                startActivity(menu);
                break;


                default:

                    break;

        }


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);



    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            db.execSQL("create table bludo ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "kol text,"
                    + "coast"
                    + ");");
        }
        catch(Exception t){

        }
        try {
            db.execSQL("create table number_z ("
                    + "id integer primary key autoincrement,"
                    + "number" + ");");

            ContentValues cvf = new ContentValues();
            cvf.put("number", "1");
            long h = db.insert("number_z", null, cvf);
        }
        catch (Exception f){

        }

        try {
            db.execSQL("create table food ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "coast text,"
                    + "kat"
                    + ");");
            /*
            ContentValues cvf = new ContentValues();
            cvf.put("name", "Класическая");
            cvf.put("coast", "40");
            cvf.put("kat", "Шаурма");
            db.insert("food", null, cvf);

            cvf.put("name", "Двойная");
            cvf.put("coast", "40");
            cvf.put("kat", "Шаурма");
            db.insert("food", null, cvf);

            cvf.put("name", "Куриная");
            cvf.put("coast", "40");
            cvf.put("kat", "Шаурма");
            db.insert("food", null, cvf);

            cvf.put("name", "Детская");
            cvf.put("coast", "20");
            cvf.put("kat", "Шаурма");
            db.insert("food", null, cvf);

            cvf.put("name", "Грибная");
            cvf.put("coast", "30");
            cvf.put("kat", "Шаурма");
            db.insert("food", null, cvf);

            cvf.put("name", "Чай");
            cvf.put("coast", "7");
            cvf.put("kat", "Горячие напитки");
            db.insert("food", null, cvf);

            cvf.put("name", "Кофе");
            cvf.put("coast", "10");
            cvf.put("kat", "Горячие напитки");
            db.insert("food", null, cvf);

            cvf.put("name", "Вода");
            cvf.put("coast", "10");
            cvf.put("kat", "Холодные напитки");
            db.insert("food", null, cvf);

            cvf.put("name", "Лимонад");
            cvf.put("coast", "5");
            cvf.put("kat", "Холодные напитки");
            db.insert("food", null, cvf);
*/
        }catch(Exception r){

        }

        try {
            db.execSQL("create table kategories ("
                    + "id integer primary key autoincrement,"
                    + "name"
                    +");");
/*
            ContentValues cvf = new ContentValues();
            cvf.put("name", "Шаурма");
            db.insert("kategories", null, cvf);
            cvf.put("name", "Горячие напитки");
            db.insert("kategories", null, cvf);
            cvf.put("name", "Холодные напитки");
            db.insert("kategories", null, cvf);*/
        }catch(Exception e){
            //Log.e("LOG_TAG", "ОШИБКА СОЗДАНИЕ БАЗЫ group С OnResume");
        }

        try {
            db.execSQL("create table zakazi ("
                    + "id integer primary key autoincrement,"
                    + "date text,"
                    + "json text,"
                    + "number_zakaz text,"
                    + "summa_zakaz"+ ");");
            Log.e("LOG_TAG", "СОЗДАНИЕ БАЗЫ ZAKAZI");
        }catch(Exception e){
            Log.e("LOG_TAG", "БАЗЫ ZAKAZI УЖЕ ЕСТЬ");
        }


        adapter.clear();
        Date ff = new Date();

        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyyMMdd");
        String strDt = simpleDate.format(ff);


        try {
            getInfo(strDt);
            //adapter.add(getInfo("20170124"));
            //adapter.notifyDataSetChanged();
        }catch (Exception b){
            Log.e("LOG_TAG_MAIN", "TROUBLE ADD - " + b.getMessage());
            System.out.print(b.getMessage());
        }
/*
        try {

            Cursor c = db.query("zakazi", null, null, null, null, null, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    String str;
                    do {
                        str = "";
                        for (String cn : c.getColumnNames()) {
                            str = str.concat(cn + " = "
                                    + c.getString(c.getColumnIndex(cn)) + "; ");
                        }
                        Log.d("LOG_TAG_onResume_zakazi", str);

                    } while (c.moveToNext());
                }
            }
            else{
                Log.e("LOG_TAG", "КУРСОР = null");
            }
        }catch(Exception n){
            Log.e("LOG_MAIN", "TROUBLE");
        }
*/
    }


    private static class Zakaz {
        public final String date;
        public final String number_zakaz;
        public final String summa_zakaz;
        public final String json;
        public final String id;


        public Zakaz(String date, String number_zakaz, String summa_zakaz, String json, String id) {
            this.date = date;
            this.number_zakaz = number_zakaz;
            this.summa_zakaz = summa_zakaz;
            this.json = json;
            this.id = id;
        }
    }



    private static final List<Zakaz> cats1 = new ArrayList<Zakaz>();

    private class CatAdapter3 extends ArrayAdapter<Zakaz> {

        ArrayList<String> pagelist;

        public CatAdapter3(Context context) {
            super(context, android.R.layout.simple_list_item_2, cats1);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Zakaz hi = getItem(position);

            String dtStart = hi.date;
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String strDt = null;
            try {
                Date date = format.parse(dtStart);
                SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
                strDt = simpleDate.format(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_item_main, null);
            }
            ((TextView) convertView.findViewById(R.id.date))
                    .setText(strDt);
            ((TextView) convertView.findViewById(R.id.number_zakaza))
                    .setText(hi.number_zakaz);
            ((TextView) convertView.findViewById(R.id.summa))
                    .setText(hi.summa_zakaz + " грн.");
            //Log.e("CAT_ADAPTER", "hi.date = " + strDt + ". hi.number_zakaz = " + hi.number_zakaz);
            ((TextView) convertView.findViewById(R.id.json))
                    .setText(hi.json);


            return convertView;
        }
    }



    public Zakaz getInfo(String search_str) {
        adapter.clear();
        Zakaz ho = null;
        ///////////////

        String query = "SELECT * FROM zakazi where date LIKE '%"+search_str+"%'";

        Cursor mCursor = null;

        mCursor = db.rawQuery(query, null);
       // mCursor = db.query("zakazi", null, "date = ?", new String[] {search_str} , null, null, "number_zakaz DESC");
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        if (mCursor.moveToFirst()) {

            int id = mCursor.getColumnIndex("id");
            int data = mCursor.getColumnIndex("date");
            int summa = mCursor.getColumnIndex("summa_zakaz");
            int number = mCursor.getColumnIndex("number_zakaz");
            int json = mCursor.getColumnIndex("json");
            do {
                // получаем значения по номерам столбцов и пишем все в лог
               /* Log.d("MainActivity INFO",
                        "data = " + mCursor.getString(data) +
                                ", summa = " + mCursor.getString(summa) +
                                ", number = " + mCursor.getString(number)+
                ", json = " + mCursor.getString(json));
*/
                ho = new Zakaz(mCursor.getString(data), "№"+mCursor.getString(number), mCursor.getString(summa),  mCursor.getString(json), mCursor.getString(id));

                adapter.add(ho);
                adapter.notifyDataSetChanged();

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (mCursor.moveToNext());
        } else
            Log.d("LOG_TAG", "0 rows");

        return ho;
    }



}
