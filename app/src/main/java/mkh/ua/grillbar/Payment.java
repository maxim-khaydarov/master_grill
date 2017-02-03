package mkh.ua.grillbar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.R.attr.defaultValue;

/**
 * Created by ASUS on 21.01.2017.
 */

public class Payment extends Activity implements View.OnClickListener {

    EditText ed1;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bDel, bt;
    Button button_oplata, button_otmena;
    TextView textView14, textView16;

    SQLiteDatabase db;
    DBHelper dbHelper;
    ContentValues cvf;

    int j = 0;
    //int savedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();




        ed1 = (EditText) findViewById(R.id.editText2);
        ed1.setInputType(InputType.TYPE_NULL);
        b1 = (Button) findViewById(R.id.button9);
        b2 = (Button) findViewById(R.id.button8);
        b3 = (Button) findViewById(R.id.button7);

        b4 = (Button) findViewById(R.id.button12);
        b5 = (Button) findViewById(R.id.button11);
        b6 = (Button) findViewById(R.id.button10);

        b7 = (Button) findViewById(R.id.button15);
        b8 = (Button) findViewById(R.id.button14);
        b9 = (Button) findViewById(R.id.button13);

        b0 = (Button) findViewById(R.id.button26);
        bDel = (Button) findViewById(R.id.button25);
        bt = (Button) findViewById(R.id.button27);

        button_oplata = (Button) findViewById(R.id.button29);
        button_oplata.setOnClickListener(this);

        button_otmena= (Button) findViewById(R.id.button28);
        button_otmena.setOnClickListener(this);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b0.setOnClickListener(this);
        bDel.setOnClickListener(this);
        bt.setOnClickListener(this);

        textView14 = (TextView) findViewById(R.id.textView14);
        textView16 = (TextView) findViewById(R.id.textView16);

        Bundle extras = getIntent().getExtras();
        String pays = extras.getString("pay");

        //savedText = extras.getInt("num");
        //Log.e("WWW", pay);
        int pay_int = Integer.valueOf(pays);
        textView14.setText(pays + " грн.");




        cvf = new ContentValues();



        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String e = (String )b.get("pay");
            j = Integer.valueOf(e);
            //j =(int) b.get("pay");
            textView14.setText(String.valueOf(j)+" грн.");
        }

        final int finalJ = j;
        ed1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (Double.valueOf(s.toString()) < finalJ) {
                        textView16.setText("- - грн.");
                    } else {
                        textView16.setText(String.valueOf(String.format("%.2f",Double.valueOf(s.toString()) - finalJ)) + " грн.");
                        Log.e("WWW", s.toString() + " = " + Double.valueOf(s.toString()) + " = " + String.valueOf(Double.valueOf(s.toString()) - finalJ));

                    }
                }catch(Exception w){
                    textView16.setText("- - грн.");
                }
            }

            public void afterTextChanged(Editable s) {

            }


        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button9:
                ed1.append("1");

                break;
            case R.id.button8:
                ed1.append("2");
                break;
            case R.id.button7:
                ed1.append("3");
                break;
            case R.id.button12:
                ed1.append("4");
                break;
            case R.id.button11:
                ed1.append("5");
                break;
            case R.id.button10:
                ed1.append("6");
                break;
            case R.id.button15:
                ed1.append("7");
                break;
            case R.id.button14:
                ed1.append("8");
                break;
            case R.id.button13:
                ed1.append("9");
                break;
            case R.id.button26:
                ed1.append("0");
                break;

            case R.id.button27:
                ed1.append(".");
                break;

            case R.id.button25:
                try {
                    String text = ed1.getText().toString();
                    ed1.setText(text.substring(0, text.length() - 1));
                } catch(StringIndexOutOfBoundsException e){

                }
                break;

            case R.id.button29:




                String json = null;

                Calendar date = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                String formattedDate = df.format(date.getTime());
                Log.e("LOG_TAG", "Current time => " + formattedDate);


                Cursor c3 = db.query("vremenno", null, null, null, null, null, null);

                try {
                    json = "{\"spisok\":[";
                    String json2 = null;
                    int p = 0;


                    // ставим позицию курсора на первую строку выборки
                    // если в выборке нет строк, вернется false
                    if (c3.moveToFirst()) {

                        // определяем номера столбцов по имени в выборке
                        int idColIndex = c3.getColumnIndex("id");
                        int nameColIndex = c3.getColumnIndex("name");
                        int emailColIndex = c3.getColumnIndex("quantity");
                        int coastColIndex = c3.getColumnIndex("coast");


                        do {
                            if(p == 0){
                                json2 = "{\"name\": \"" + c3.getString(nameColIndex) + "\", \"quantity\": \"" + c3.getString(emailColIndex) +
                                "\", \"coast\": \"" + c3.getString(coastColIndex) + "\"}";
                            }
                            else{
                                json2 = json2 + ",{\"name\": \"" + c3.getString(nameColIndex) + "\", \"quantity\": \"" + c3.getString(emailColIndex) +
                                        "\", \"coast\": \"" + c3.getString(coastColIndex) + "\"}";
                            }


                            Log.e("!!!", json2);

                            p++;
                            addBludo(c3.getString(nameColIndex), c3.getString(emailColIndex), c3.getString(coastColIndex));




                            // получаем значения по номерам столбцов и пишем все в лог
                            /*Log.d("LOG_TAG",
                                    "ID = " + c.getInt(idColIndex) +
                                            ", name = " + c.getString(nameColIndex) +
                                            ", quantity = " + c.getString(emailColIndex));*/
                            // переход на следующую строку
                            // а если следующей нет (текущая - последняя), то false - выходим из цикла
                        } while (c3.moveToNext());

                        json2 = json2 + "]}";
                        json = json + json2;
                    } else
                        Log.d("LOG_TAG", "0 rows");

                }catch(Exception f){
                    Log.e("LOG_TAG", "ОШИБКА");
                }




                System.out.println("jsonString: "+json);

                cvf.put("date", formattedDate);
                cvf.put("json", json);
                cvf.put("number_zakaz",number_zakaza());
                cvf.put("summa_zakaz", j);
                db.insert("zakazi", null, cvf);

                c3.close();

                ContentValues newValues = new ContentValues();
                newValues.put("number", number_zakaza()+1);

                long d = db.update("number_z", newValues, "id=" + 1, null);
                Log.e("LOOOOOOG", String.valueOf(d));


                db.delete("vremenno", null, null);


                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);

                break;

            case R.id.button28:

                super.onBackPressed();

                break;


            //dbHelper.close();


            default:
                break;



        }
        //dbHelper.close();

    }

    public int number_zakaza (){
        Cursor c  = db.rawQuery("Select * from number_z", null);
        int co = 0;
        try {
            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        String id = c.getString(c.getColumnIndex("id"));
                        co = c.getInt(c.getColumnIndex("number"));
                        Log.e("IDDDD", id);

                    }while (c.moveToNext());
                }


            }

        } catch (SQLiteException se ) {

        }

        return co;
    }


    public void addBludo(String name, String kol, String coast){
        String query = "SELECT * FROM bludo where name LIKE '%"+name+"%'";

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        if (mCursor.moveToFirst()) {
            Log.e("DDDD", "Заход");

            int id = mCursor.getColumnIndex("id");
            int names = mCursor.getColumnIndex("name");
            int coasts = mCursor.getColumnIndex("coast");
            int kols = mCursor.getColumnIndex("kol");
            int summas = mCursor.getColumnIndex("summa");



            do {
                // получаем значения по номерам столбцов и пишем все в лог
                //Log.d("LOG_TAG",
                //        "id = " + mCursor.getString(id) +
                 //               ", name = " + mCursor.getString(name) +
                  //              ", coast = " + mCursor.getString(coast));
                //ginger = new Food(mCursor.getString(name), mCursor.getString(id), mCursor.getString(coast) );
                ContentValues newValues = new ContentValues();
                newValues.put("kol", String.valueOf(Integer.valueOf(mCursor.getString(kols))+Integer.valueOf(kol)));
                //newValues.put("summa", String.valueOf(Integer.valueOf(mCursor.getString(summas))+Integer.valueOf(mCursor.getString(coasts))));


                long d = db.update("bludo", newValues, "id=" + mCursor.getString(id), null);
                Log.e("!!!", "Обновили значение bludo, " + name + " " + d);

///////////////////////////////////////////////////////////Проверка на наличие данного продукта в базе vremenno

                    } while (mCursor.moveToNext());
                } else {
                    Log.d("LOG_TAG", "НЕТ ТАКОГО ИМЕНИ!");
                    ContentValues cv = new ContentValues();
                    //Добавляем в базу vremenno значение
                    cv.put("name",  name);
                    cv.put("kol", kol);
                    cv.put("coast", coast);

                    // вставляем запись и получаем ее ID
                    long rowID = db.insert("bludo", null, cv);
                    Log.e("LOOOG", "Добавили в bludo " + name + " " + String.valueOf(rowID));
                }
    }



    @Override
    public void onResume() {
        super.onResume();
    }

}
