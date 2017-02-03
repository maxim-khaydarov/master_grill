package mkh.ua.grillbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lenovo on 23.01.2017.
 */

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public int gg;

    EditText ed1;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bDel, bt;
    Button button_oplata, button_otmena;
    TextView textView14, textView16;

    SQLiteDatabase db;
    DBHelper dbHelper;
    ContentValues cvf;

    public CustomDialogClass(Activity a, int g) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.gg = g;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.activity_payment);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        //yes = (Button) findViewById(R.id.btn_yes);
        //no = (Button) findViewById(R.id.btn_no);
        //yes.setOnClickListener(this);
        //no.setOnClickListener(this);

        dbHelper = new DBHelper(c);
        db = dbHelper.getWritableDatabase();
        cvf = new ContentValues();

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

        button_otmena = (Button) findViewById(R.id.button28);
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

        textView14.setText(String.valueOf(gg) + " грн.");

        final int finalJ = gg;
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
                        textView16.setText(String.valueOf(String.format("%.2f", Double.valueOf(s.toString()) - finalJ)) + " грн.");
                        Log.e("WWW", s.toString() + " = " + Double.valueOf(s.toString()) + " = " + String.valueOf(Double.valueOf(s.toString()) - finalJ));

                    }
                } catch (Exception w) {
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
                } catch (StringIndexOutOfBoundsException e) {

                }
                break;

            case R.id.button29:

                JSONObject schoolClass = new JSONObject();
                JSONObject schoolClass2 = new JSONObject();
                JSONArray pupilsArray = new JSONArray();
                String json;

                Calendar date = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                String formattedDate = df.format(date.getTime());
                Log.e("LOG_TAG", "Current time => " + formattedDate);

                Cursor c = db.query("vremenno", null, null, null, null, null, null);

                try {
                    schoolClass.put("data", formattedDate);
                    schoolClass.put("summa_zakaza", gg);
                    schoolClass.put("number_zakaza", 001);



                    // ставим позицию курсора на первую строку выборки
                    // если в выборке нет строк, вернется false
                    if (c.moveToFirst()) {

                        // определяем номера столбцов по имени в выборке
                        int idColIndex = c.getColumnIndex("id");
                        int nameColIndex = c.getColumnIndex("name");
                        int emailColIndex = c.getColumnIndex("quantity");
                        int coastColIndex = c.getColumnIndex("coast");


                        do {

                            schoolClass2.put("name",c.getString(nameColIndex));
                            schoolClass2.put("quantity",c.getString(emailColIndex));
                            schoolClass2.put("coast",c.getString(coastColIndex));

                            pupilsArray.put(schoolClass2);

                            //try {
                                //schoolClass.put("data", formattedDate);
                                //schoolClass.put("pupils_count", pupils.length);



                                schoolClass.put("spisok", pupilsArray);
                                json = schoolClass.toString();
                            //} catch (JSONException e) {
                           // }


                            // получаем значения по номерам столбцов и пишем все в лог
                            Log.d("LOG_TAG",
                                    "ID = " + c.getInt(idColIndex) +
                                            ", name = " + c.getString(nameColIndex) +
                                            ", quantity = " + c.getString(emailColIndex));
                            // переход на следующую строку
                            // а если следующей нет (текущая - последняя), то false - выходим из цикла
                        } while (c.moveToNext());
                    } else
                        Log.d("LOG_TAG", "0 rows");

                }catch(Exception f){

                }
                c.close();










                json = schoolClass.toString();


                System.out.println("jsonString: "+json);

                cvf.put("date", "");
                cvf.put("json", json);
                cvf.put("number_zakaz", "");
                cvf.put("summa_zakaz", gg);
                db.insert("zakazi", null, cvf);


                c = db.query("zakazi", null, null, null, null, null, null);
                if (c != null) {
                    if (c.moveToFirst()) {
                        String str;
                        do {
                            str = "";
                            for (String cn : c.getColumnNames()) {
                                str = str.concat(cn + " = "
                                        + c.getString(c.getColumnIndex(cn)) + "; ");
                            }
                            Log.d("LOG_TAG", str);

                        } while (c.moveToNext());
                    }
                }
                db.execSQL("DROP TABLE IF EXISTS vremenno");

                dismiss();
                dismiss();








                break;

            case R.id.button28:
                /*
                int clearCount = db.delete("vremenno", null, null);
                Log.d("LOG_TAG", "deleted rows count = " + clearCount);
                */
                //int del = db.delete("vremenno", null, null);
                Log.d("LOG_TAG", "deleted table");
                //db.execSQL("DROP TABLE IF EXISTS vremenno");
                //db.execSQL("DROP TABLE IF EXISTS zakazi");
                super.onBackPressed();

                break;


            //dbHelper.close();


            default:
                break;


        }
    }




}