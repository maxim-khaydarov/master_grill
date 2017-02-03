package mkh.ua.grillbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.defaultValue;


public class NewOrder  extends Activity implements View.OnClickListener{

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    //int savedText;

    Button bt1, bt2, bt3, bt5, bt6;

    ListView zakaz;
    int menu = 0;

    TextView textView, summa;
    int summa_int = 0;
    EditText ed1;
    DBHelper dbHelper;
    SQLiteDatabase db;
    ContentValues cv;

    ArrayAdapter<Food> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        gridView = (GridView) findViewById(R.id.gridView1);


        //adapter = new CatAdapter(this);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                String itemText = ((TextView) v.findViewById(R.id.text)).getText().toString();

                if (menu == 1){
                    if(itemText.contains("Назад")){

                        start_grid();
                    }
                    else {
                        add_vremenno(itemText);
                    }
                }
                else{
                    if(itemText.contains("Назад")){

                        start_grid();
                    }
                    else {

                        add_grid(itemText);
                    }
                }
            }

        });



        textView = (TextView) findViewById(R.id.textView);
        summa = (TextView) findViewById(R.id.textView2);
        ed1 = (EditText) findViewById(R.id.editText);
        ed1.setInputType(InputType.TYPE_NULL);
        bt1 = (Button) findViewById(R.id.button16);
        bt2 = (Button) findViewById(R.id.button17);
        bt3 = (Button) findViewById(R.id.button18);
        bt5 = (Button) findViewById(R.id.button5);
        bt6 = (Button) findViewById(R.id.button6);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt5.setOnClickListener(this);



        zakaz = (ListView) findViewById(R.id.zakaz);

        // Привяжем массив через адаптер к ListView
        adapter = new CatAdapter(this);
        zakaz.setAdapter(adapter);
        zakaz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {

                Log.e("JOOOO", "UUUUUUUUUUUUUU");

                //int delCount = db.delete("vremenno", "name = " + adapter.getItem(position), null);

                //Log.e("LOG_TAG", String.valueOf(position));

                //Log.d("LOG_TAG", "deleted rows count = " + delCount);
/*
                if(((TextView) itemClicked).getText().equals("Пицца")){
                    summa_int = summa_int - 40;
                    summa.setText(String.valueOf(summa_int) + " грн.");
                }
                else if(((TextView) itemClicked).getText().equals("Шаурма")){
                    summa_int = summa_int - 20;
                    summa.setText(String.valueOf(summa_int) + " грн.");
                }
                else if(((TextView) itemClicked).getText().equals("Чай")){
                    summa_int = summa_int - 5;
                    summa.setText(String.valueOf(summa_int) + " грн.");
                }
*/

                String itemSelected = ((TextView) itemClicked.findViewById(R.id.textView22)).getText().toString();
                int delCount1 = db.delete("vremenno", "name = " + "'" + itemSelected + "'" , null);

                Log.e("LOG_TAG", String.valueOf(delCount1));
                String itemname = new Integer(position).toString();
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
                Log.e("LOG_TAG", itemname);
            }
        });

    }

    private void add_vremenno(String search_str) {
        String query = "SELECT * FROM food where name LIKE '%"+search_str+"%'";

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        if (mCursor.moveToFirst()) {

            int id = mCursor.getColumnIndex("id");
            int name = mCursor.getColumnIndex("name");
            int coast = mCursor.getColumnIndex("coast");
            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("LOG_TAG",
                        "id = " + mCursor.getString(id) +
                                ", name = " + mCursor.getString(name) +
                                ", coast = " + mCursor.getString(coast));
                //ginger = new Food(mCursor.getString(name), mCursor.getString(id), mCursor.getString(coast) );

///////////////////////////////////////////////////////////Проверка на наличие данного продукта в базе vremenno
                String querys = "SELECT * FROM vremenno where name LIKE '%"+mCursor.getString(name)+"%'";

                Cursor mCursors = db.rawQuery(querys, null);
                if (mCursors != null) {
                    mCursors.moveToFirst();
                }


                if (mCursors.moveToFirst()) {

                    int ids = mCursors.getColumnIndex("id");
                    int names = mCursors.getColumnIndex("name");
                    int coasts = mCursors.getColumnIndex("coast");
                    int quantity = mCursors.getColumnIndex("quantity");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d("LOG_TAG",
                                "id = " + mCursors.getString(id) +
                                        ", name = " + mCursors.getString(name) +
                                        ", coast = " + mCursors.getString(coast));





                        ContentValues newValues = new ContentValues();
                        newValues.put("quantity", String.valueOf(Integer.valueOf(mCursors.getString(quantity))+1));

                        db.update("vremenno", newValues, "id=" + mCursors.getString(id), null);

                        //db.update("vremenno", cv, "id = ?", new String[] { id });




                    } while (mCursors.moveToNext());
                } else {
                    Log.d("LOG_TAG", "НЕТ ТАКОГО ИМЕНИ!");
                    //Добавляем в базу vremenno значение

                    cv.put("name",  mCursor.getString(name));
                    cv.put("quantity", "1");
                    cv.put("coast", mCursor.getString(coast));

                    // вставляем запись и получаем ее ID
                    long rowID = db.insert("vremenno", null, cv);
                    Log.e("LOOOG", String.valueOf(rowID));
                }
////////////////////////////////////////////////////////////


                //Обновляем базу vremenno
               get_base_vremenno();

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла

            } while (mCursor.moveToNext());
        } else
            Log.d("LOG_TAG", "0 rows");
    }

    private void get_base_vremenno() {
        Food ginger = null;
        Cursor c  = db.rawQuery("Select * from vremenno", null);
        //db.delete("vremenno", null, null);
        adapter.clear();
        int gd = 0;
        try {

            if (c != null ) {

                if  (c.moveToFirst()) {


                    do {

                        String ids = c.getString(c.getColumnIndex("id"));
                        String names = c.getString(c.getColumnIndex("name"));
                        String quantitys = c.getString(c.getColumnIndex("quantity"));
                        String coasts = c.getString(c.getColumnIndex("coast"));
                        ginger = new Food(names, quantitys, coasts );

                        int kol_vo = Integer.valueOf(c.getString(c.getColumnIndex("quantity")));
                        int kost = Integer.valueOf(c.getString(c.getColumnIndex("coast")));

                        int ob = kol_vo * kost;
                        gd = gd + ob;

                        adapter.add(ginger);
                        adapter.notifyDataSetChanged();

                    }while (c.moveToNext());
                    summa.setText(String.valueOf(gd) + " грн.");

                }
                else{
                    Log.e("!!", "пустая таблица");
                    summa.setText("0 грн.");

                }

            }


        } catch (Exception se ) {

        }
    }


    private ArrayList<ImageItem> getData_kategories() {

        final ArrayList<ImageItem> imageItems = new ArrayList<>();

        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        //TypedArray col = getResources().obtainTypedArray(R.array.colum_ids);

        Cursor c  = db.rawQuery("Select * from kategories", null);

        try {

            if (c != null ) {

                if  (c.moveToFirst()) {

                    int i = 0;
                    do {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));

                        Resources res = getResources();
                        String[] myBooks = res.getStringArray(R.array.image_ids);
                        String id = c.getString(c.getColumnIndex("id"));
                        String name = c.getString(c.getColumnIndex("name"));



                        imageItems.add(new ImageItem(myBooks[i], name, get_colums(name)));
                        i++;
                    }while (c.moveToNext());

                }

            }

        } catch (SQLiteException se ) {

        }

        return imageItems;

    }

    private String get_colums(String search_str) {
        String query = "SELECT * FROM food where kat LIKE '%" + search_str + "%'";

        Cursor mCursor = null;

        mCursor = db.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        int i = 0;
        if (mCursor.moveToFirst()) {

            int name = mCursor.getColumnIndex("name");
            int coast = mCursor.getColumnIndex("coast");


            do {

                // получаем значения по номерам столбцов и пишем все в лог
               /* Log.d("MainActivity INFO",
                        "data = " + mCursor.getString(data) +
                                ", summa = " + mCursor.getString(summa) +
                                ", number = " + mCursor.getString(number)+
                ", json = " + mCursor.getString(json));
*/


                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                i++;
            } while (mCursor.moveToNext());
        } else
            Log.d("LOG_TAG", "0 rows");


        return String.valueOf(i);
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

    @Override
    public void onResume() {
        super.onResume();

    Log.e("!!", "OnResume");

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        get_base_vremenno();
        //adapter.clear();



        cv = new ContentValues();
        try {
            db.execSQL("create table vremenno ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "quantity text,"
                    + "coast" + ");");
            Log.e("LOG_TAG", "СОЗДАНИЕ БАЗЫ VREMENNO С onResume");
        }catch(Exception e){

        }

        try {
            start_grid();
        }
        catch(Exception c){
            Toast.makeText(this, "Нет категорий! Зайдите в настройки и добавьте категории и товар!", Toast.LENGTH_LONG).show();
        }

        bt6.setText("№ "+String.valueOf(number_zakaza()));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.delete("vremenno", null, null);
        dbHelper.close();
        adapter.clear();
                }

    public Food getInfo(String search_str) {

        Food ginger = null;
        ///////////////
        String query = "SELECT * FROM food where indef LIKE '%"+search_str+"%'";

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        if (mCursor.moveToFirst()) {

            int indef = mCursor.getColumnIndex("indef");
            int name = mCursor.getColumnIndex("name");
            int coast = mCursor.getColumnIndex("coast");
            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("LOG_TAG",
                        "Indef = " + mCursor.getString(indef) +
                                ", name = " + mCursor.getString(name) +
                                ", coast = " + mCursor.getString(coast));
                ginger = new Food(mCursor.getString(name), mCursor.getString(indef), mCursor.getString(coast) );

                summa_int = summa_int + mCursor.getInt(coast);
                summa.setText(summa_int + " грн.");
                //catnames.add(0, mCursor.getString(name));
                adapter.notifyDataSetChanged();
                cv.put("name",  mCursor.getString(name));
                cv.put("quantity", "1");
                cv.put("coast", mCursor.getString(coast));
                // вставляем запись и получаем ее ID
                long rowID = db.insert("vremenno", null, cv);
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (mCursor.moveToNext());
        } else
            Log.d("LOG_TAG", "0 rows");

        return ginger;
    }



        @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button5:

                Intent payment = new Intent (this, Payment.class);
                payment.putExtra("pay", removeLastChar(String.valueOf(summa.getText())));
                startActivity(payment);

                break;
        }


    }
    private static final List<Food> cats = new ArrayList<Food>();


    private static String removeLastChar(String str) {
        return str.substring(0,str.length()-5);
    }



    public class CatAdapter extends ArrayAdapter<Food> {

        ArrayList<String> pagelist;

        public CatAdapter(Context context) {
            super(context, R.layout.list_zakaz, cats);
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final Food food = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_zakaz, null);
            }
            ((TextView) convertView.findViewById(R.id.textView22))
                    .setText(food.name);
            ((TextView) convertView.findViewById(R.id.textView21))
                    .setText(food.coast+ " грн.");
            ((Button) convertView.findViewById(R.id.button30))
                    .setText(food.quantity);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
               public void onClick(View view) {



                    int delCount1 = db.delete("vremenno", "name = " + "'" + food.name + "'" , null);

                    adapter.remove(adapter.getItem(position));
                    adapter.notifyDataSetChanged();
                    get_base_vremenno();

                    }
                });

            return convertView;
        }
    }

public void start_grid(){
    menu = 0;
    gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData_kategories());
    gridView.setAdapter(gridAdapter);
    setGridViewHeightBasedOnChildren(gridView, 3);
}





    public void add_grid(String search_str){
        menu = 1;
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        String query = "SELECT * FROM food where kat LIKE '%"+search_str+"%'";

        Cursor mCursor = null;

        mCursor = db.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        Resources res = getResources();
        String[] myBooks = res.getStringArray(R.array.image_ids);
        imageItems.add(new ImageItem(myBooks[0], "Назад", ""));
        if (mCursor.moveToFirst()) {

            int name = mCursor.getColumnIndex("name");
            int coast = mCursor.getColumnIndex("coast");
            int i = 0;

            do {
                // получаем значения по номерам столбцов и пишем все в лог
               /* Log.d("MainActivity INFO",
                        "data = " + mCursor.getString(data) +
                                ", summa = " + mCursor.getString(summa) +
                                ", number = " + mCursor.getString(number)+
                ", json = " + mCursor.getString(json));
*/

                imageItems.add(new ImageItem(myBooks[i+1], mCursor.getString(name), mCursor.getString(coast) + " грн."));


                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                i++;
            } while (mCursor.moveToNext());
        } else
            Log.d("LOG_TAG", "0 rows");
        GridViewAddAdapter gridAdapter;
        gridAdapter = new GridViewAddAdapter(this, R.layout.grid_item_layout, imageItems);
        gridView.setAdapter(gridAdapter);
        setGridViewHeightBasedOnChildren(gridView, 3);
    }


    public int number_zakaza (){
        Cursor cr  = db.rawQuery("Select * from number_z", null);
        int co = 0;
        try {
            if (cr != null ) {
                if  (cr.moveToFirst()) {
                    do {
                        String id = cr.getString(cr.getColumnIndex("id"));
                        co = cr.getInt(cr.getColumnIndex("number"));
                    }while (cr.moveToNext());
                }
            }


        } catch (SQLiteException se ) {

        }
        return co;
    }
}
