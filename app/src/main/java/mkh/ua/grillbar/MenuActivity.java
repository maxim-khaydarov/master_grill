package mkh.ua.grillbar;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.ToggleButton;

/**
 * Created by Lenovo on 29.01.2017.
 */
public class MenuActivity extends TabActivity implements View.OnClickListener{


    ToggleButton tgb1, tgb2;
    TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tgb1 = (ToggleButton) findViewById(R.id.toggleButton);
        tgb2 = (ToggleButton) findViewById(R.id.toggleButton2);
        tgb1.setOnClickListener(this);
        tgb2.setOnClickListener(this);

        // получаем TabHost
        tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("ОТЧЕТ");
        tabSpec.setContent(new Intent(this, ActivityOtchet.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("НАСТРОЙКИ");
        tabSpec.setContent(new Intent(this, ActivityKassa.class));
        tabHost.addTab(tabSpec);


        //tabHost.setCurrentTab(0);
        tgb1.setChecked(true);
        }




    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.toggleButton:
                tabHost.setCurrentTab(0);
                tgb2.setChecked(false);

                break;
            case R.id.toggleButton2:
                tabHost.setCurrentTab(1);
                tgb1.setChecked(false);
        }

    }
}