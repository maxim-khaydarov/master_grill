package mkh.ua.grillbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 29.01.2017.
 */

public class ActivityKassa extends Activity implements View.OnClickListener{

    SQLiteDatabase db;
    DBHelper dbHelper;
    ListView group, lv_bludo;

    Button add_group, add_bludo;

    final ArrayList<String> group_array = new ArrayList<String>();
    final ArrayList<String> bludo_array = new ArrayList<String>();
    ArrayAdapter<String> adapter = null;
    ArrayAdapter<BludoAll> adapter_bludo;


            protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kassa);

                dbHelper = new DBHelper(this);
                db = dbHelper.getWritableDatabase();

                add_group = (Button) findViewById(R.id.button32);
                add_group.setOnClickListener(this);
                add_bludo = (Button) findViewById(R.id.button35);
                add_bludo.setOnClickListener(this);

                group = (ListView) findViewById(R.id.group);
                lv_bludo = (ListView) findViewById(R.id.bludo);


                // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView

                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, group_array);

                adapter_bludo = new AdapterBludoAll(this);
                // Привяжем массив через адаптер к ListView
                group.setAdapter(adapter);
                lv_bludo.setAdapter(adapter_bludo);

                group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                            long id) {

                        String name = ((TextView) itemClicked.findViewById(android.R.id.text1)).getText().toString();

                        //String itemJson = ((TextView) itemClicked.findViewById(R.id.json)).getText().toString();
                        //String itemNumber = ((TextView) itemClicked.findViewById(R.id.number_zakaza)).getText().toString();
                        //String itemSumma = ((TextView) itemClicked.findViewById(R.id.summa)).getText().toString();

                        dialog_update_group(name, position);
                    }
                });

                lv_bludo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                            long id) {

                        String name = ((TextView) itemClicked.findViewById(R.id.textView37)).getText().toString();
                        String coast = ((TextView) itemClicked.findViewById(R.id.textView35)).getText().toString();
                        String group = ((TextView) itemClicked.findViewById(R.id.textView36)).getText().toString();
                        String idi = ((TextView) itemClicked.findViewById(R.id.textView50)).getText().toString();

                        //String itemJson = ((TextView) itemClicked.findViewById(R.id.json)).getText().toString();
                        //String itemNumber = ((TextView) itemClicked.findViewById(R.id.number_zakaza)).getText().toString();
                        //String itemSumma = ((TextView) itemClicked.findViewById(R.id.summa)).getText().toString();

                        //dialog_update_group(name, position);
                        dialog_update_bludo(name, position, coast, group, idi);
                    }
                });

        }






    public void getGroup (){
        TextView f = (TextView)findViewById(R.id.textView44);
        adapter.clear();
        Cursor cr  = db.rawQuery("Select * from kategories", null);

        try {
            if (cr != null ) {
                if  (cr.moveToFirst()) {
                    do {
                        f.setVisibility(View.GONE);
                        String id = cr.getString(cr.getColumnIndex("id"));
                        String co = cr.getString(cr.getColumnIndex("name"));
                        group_array.add(0, co);
                        adapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }
                else{

                    f.setVisibility(View.VISIBLE);
                    Log.e("EEEE", "!cr.moveToFirst()");

                }

            }
            else{
                Log.e("EEEE", "cr == null");
            }

        } catch (SQLiteException se ) {

        }


    }



    @Override
    public void onResume() {
        super.onResume();
        try {
            getGroup();

            } catch (Exception t){
                Log.e("!", t.toString());
            }

        try{
            getAllBludo();
        }
        catch (Exception r){

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button32:
                dialog_add_group();
                break;

            case R.id.button35:

                dialog_add_bludo();

                break;

            default:
                break;
        }

    }

    private void dialog_add_bludo() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bludo);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        //TextView text = (TextView) dialog.findViewById(R.id.text);
        // text.setText("Android custom dialog example!");


        final EditText name = (EditText) dialog.findViewById(R.id.editText4);
        final EditText coast = (EditText) dialog.findViewById(R.id.editText5);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        ArrayList<String> stringArrayList = new ArrayList<String>();
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getGroupSpinner());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        Button otmena = (Button) dialog.findViewById(R.id.button38);
        Button save = (Button) dialog.findViewById(R.id.button37);
        // Создаём пустой массив для хранения имен котов
        // Button6.setText(number_zakaza);
        // if button is clicked, close the custom dialog
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length() != 0 && coast.getText().toString().length() != 0) {
                    ContentValues cvf = new ContentValues();
                    cvf.put("name", name.getText().toString());
                    cvf.put("coast", coast.getText().toString());
                    cvf.put("kat", spinner.getSelectedItem().toString());
                    db.insert("food", null, cvf);
                    dialog.dismiss();
                    getAllBludo();

                }
                else{
                    dialog.dismiss();
                }
            }
        });

        otmena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private void dialog_update_bludo(final String name, final int position, final String coast, final String group, final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_bludo);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        //TextView text = (TextView) dialog.findViewById(R.id.text);
        // text.setText("Android custom dialog example!");


        final EditText name_ed = (EditText) dialog.findViewById(R.id.editText4);
        final EditText coast_ed = (EditText) dialog.findViewById(R.id.editText5);

        name_ed.setText(name);
        coast_ed.setText(coast);


        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);

        ArrayList<String> stringArrayList = new ArrayList<String>();
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getGroupSpinner());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int positions = adapter.getPosition(group);


        spinner.setAdapter(adapter);
        spinner.setSelection(positions);

        Button del = (Button) dialog.findViewById(R.id.button38);
        Button save = (Button) dialog.findViewById(R.id.button37);
        // Создаём пустой массив для хранения имен котов
        // Button6.setText(number_zakaza);
        // if button is clicked, close the custom dialog
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_ed.getText().toString().length() != 0 && coast_ed.getText().toString().length() != 0) {


                    ContentValues newValues = new ContentValues();
                    newValues.put("name", name_ed.getText().toString());
                    newValues.put("coast", coast_ed.getText().toString());
                    newValues.put("kat", spinner.getSelectedItem().toString());

                    long d = db.update("food", newValues, "id=" + id, null);
                    dialog.dismiss();
                    getAllBludo();

                }
                else{
                    dialog.dismiss();
                }
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.delete("food", "name = " + "'" + name + "'" , null);
                getAllBludo();
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private ArrayList<String> getGroupSpinner() {
        ArrayList<String> stringArrayList = new ArrayList<String>();
        Cursor cr  = db.rawQuery("Select * from kategories", null);

        try {
            if (cr != null ) {
                if  (cr.moveToFirst()) {
                    do {

                        String id = cr.getString(cr.getColumnIndex("id"));
                        String co = cr.getString(cr.getColumnIndex("name"));
                        stringArrayList.add(0, co);

                    }while (cr.moveToNext());
                }
                else{
                    Log.e("EEEE", "!cr.moveToFirst()");
                }
            }
            else{
                Log.e("EEEE", "cr == null");
            }
        } catch (SQLiteException se ) {

        }
return  stringArrayList;
    }


    private void dialog_add_group(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_group);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        //TextView text = (TextView) dialog.findViewById(R.id.text);
        // text.setText("Android custom dialog example!");


        final EditText ed1 = (EditText) dialog.findViewById(R.id.editText3);
        //ed1.setInputType(InputType.TYPE_NULL);


        Button Button33 = (Button) dialog.findViewById(R.id.button33);
        Button Button36 = (Button) dialog.findViewById(R.id.button36);
        // Создаём пустой массив для хранения имен котов



        // Button6.setText(number_zakaza);
        // if button is clicked, close the custom dialog
        Button33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().length() != 0) {
                    ContentValues cvf = new ContentValues();
                    cvf.put("name", ed1.getText().toString());
                    db.insert("kategories", null, cvf);
                    dialog.dismiss();
                    getGroup();
                }
                else{
                    dialog.dismiss();
                }
            }
        });

        Button36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void dialog_update_group(final String name, final int position){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_group);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        //TextView text = (TextView) dialog.findViewById(R.id.text);
        // text.setText("Android custom dialog example!");


        final EditText ed1 = (EditText) dialog.findViewById(R.id.editText3);
        ed1.setText(name);


        Button del = (Button) dialog.findViewById(R.id.button34);
        Button update = (Button) dialog.findViewById(R.id.button33);

        // Создаём пустой массив для хранения имен котов



        // Button6.setText(number_zakaza);
        // if button is clicked, close the custom dialog
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete("kategories", "name = " + "'" + name + "'" , null);

                //adapter.remove(adapter.getItem(position));
                //adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    getGroup();
                }


        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().length() != 0) {
                    String query = "SELECT * FROM kategories where name LIKE '%"+name+"%'";

                    Cursor mCursor = db.rawQuery(query, null);
                    if (mCursor != null) {
                        mCursor.moveToFirst();
                    }

                    if (mCursor.moveToFirst()) {

                        int id = mCursor.getColumnIndex("id");
                        int name = mCursor.getColumnIndex("name");

                        do {
                            // получаем значения по номерам столбцов и пишем все в лог
                            //Log.d("LOG_TAG",
                              //      "Indef = " + mCursor.getString(indef) +
                                //            ", name = " + mCursor.getString(name) +
                                  //          ", coast = " + mCursor.getString(coast));


                            ContentValues newValues = new ContentValues();
                            newValues.put("name", ed1.getText().toString());
                            //newValues.put("summa", String.valueOf(Integer.valueOf(mCursor.getString(summas))+Integer.valueOf(mCursor.getString(coasts))));


                            long d = db.update("kategories", newValues, "id=" + mCursor.getString(id), null);

                            // переход на следующую строку
                            // а если следующей нет (текущая - последняя), то false - выходим из цикла
                        } while (mCursor.moveToNext());
                    } else
                        Log.d("LOG_TAG", "0 rows");
                    dialog.dismiss();
                    getGroup();
                }
                else{
                    dialog.dismiss();
                }
            }

        });

        dialog.show();

    }

    public void getAllBludo (){
        BludoAll ginger = null;
        //Cursor c  = db.rawQuery("Select * from bludo", null);
        Cursor c = db.query("food", null, null, null, null, null, null);
        //db.delete("vremenno", null, null);
        adapter_bludo.clear();

        try {
            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        String id = c.getString(c.getColumnIndex("id"));
                        String name = c.getString(c.getColumnIndex("name"));
                        String coast = c.getString(c.getColumnIndex("coast"));
                        String group = c.getString(c.getColumnIndex("kat"));



                        ginger = new BludoAll(name, coast, group, id );






                        adapter_bludo.add(ginger);
                        adapter_bludo.notifyDataSetChanged();

                    }while (c.moveToNext());
                }
                else{
                    Log.e("!!", "пустая таблица");
                }
            }
        } catch (Exception se ) {

        }
    }


    private static final List<BludoAll> catsr = new ArrayList<BludoAll>();

    private class AdapterBludoAll extends ArrayAdapter<BludoAll> {

        ArrayList<String> pagelist;

        public AdapterBludoAll(Context context) {
            super(context, R.layout.list_bludo_all, catsr);
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final BludoAll food = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_bludo_all, null);
            }
            ((TextView) convertView.findViewById(R.id.textView37))
                    .setText(food.name);
            ((TextView) convertView.findViewById(R.id.textView35))
                    .setText(food.coast + " грн.");
            ((TextView) convertView.findViewById(R.id.textView36))
                    .setText(food.group);
            ((TextView) convertView.findViewById(R.id.textView50))
                    .setText(food.id);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    dialog_update_bludo(food.name, position, food.coast ,food.group, food.id);
                    //int delCount1 = db.delete("vremenno", "name = " + "'" + food.name + "'" , null);

                    //adapter.remove(adapter.getItem(position));
                    //adapter.notifyDataSetChanged();

                }
            });

            return convertView;
        }
    }

}