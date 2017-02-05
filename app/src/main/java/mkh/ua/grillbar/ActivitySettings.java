package mkh.ua.grillbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Lenovo on 05.02.2017.
 */
public class ActivitySettings  extends Activity implements View.OnClickListener{

    Button button_import, button_export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        button_import = (Button) findViewById(R.id.button42);
        button_export = (Button) findViewById(R.id.button41);
        button_import.setOnClickListener(this);
        button_export.setOnClickListener(this);

    }

    public void import_database (){
        String db_name = "myDB";
        File sd = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Master Grill"+
                File.separator );
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String backupDBPath = "/data/"+ getPackageName() +"/databases/"+db_name;
        String currentDBPath = db_name;
        File currentDB = new File(sd, currentDBPath);
        File backupDB = new File(data, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Подождите...", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Импорт данных завершен", Toast.LENGTH_LONG).show();
    }

    public void export_database (){
        String db_name = "myDB";
        //File sd = new File(Environment.getExternalStorageDirectory() +
        //        File.separator + "GrillMaster" + File.separator + "database" + File.separator +
        //        File.separator );
        File sd = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Master Grill"+
                File.separator );

        //sd.mkdir();
        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
            Log.e("E", "No sd exist");
        }
        if (success) {

            File data = Environment.getDataDirectory();
            FileChannel source=null;
            FileChannel destination=null;
            String currentDBPath = "/data/"+ getPackageName() +"/databases/"+db_name;
            String backupDBPath = db_name;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toast.makeText(this, "Подождите...", Toast.LENGTH_SHORT).show();
            } catch(IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Экспорт данных завершен", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button42:
                import_database();
                break;
            case R.id.button41:
                export_database();
                break;

            default:
                break;
        }
    }
}
