package cn.netkiller.okhttp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);

                    Intent intent = new Intent(MainActivity.this, RunnableActivity.class);
                    startActivity(intent);

                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);

                    intent = new Intent(MainActivity.this, Oauth2jwtActivity.class);
                    startActivity(intent);

                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);

                    intent = new Intent(MainActivity.this, HttpActivity.class);
                    startActivity(intent);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Button button = (Button) findViewById(R.id.lang);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                changeLanguage(Locale.SIMPLIFIED_CHINESE);
            }
        });


        Button buttonPut = (Button) findViewById(R.id.buttonPut);

        buttonPut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //实例化SharedPreferences对象
                SharedPreferences sharedPreferences = getSharedPreferences("test", Activity.MODE_PRIVATE);

                //实例化SharedPreferences.Editor对象
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //用putString的方法保存数据
                editor.putString("name", "Neo");
                editor.putString("nickname", "netkiller");
                editor.putBoolean("sex", true);
                editor.putInt("age", 30);
                editor.putFloat("tall", 180.23f);
                Set<String> books = new HashSet<String>();
                books.add("Netkiller Linux 手札");
                books.add("Netkiller Java 手札");
                books.add("Netkiller Android 手札");
                editor.putStringSet("books", books);

                //提交当前数据
                editor.commit();

            }
        });

        Button buttonGet = (Button) findViewById(R.id.buttonGet);

        buttonGet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //实例化SharedPreferences对象
                SharedPreferences sharedPreferences = getSharedPreferences("test", Activity.MODE_PRIVATE);

                //使用getString方法获得value，
                String name = sharedPreferences.getString("name", "");
                String nickname = sharedPreferences.getString("nickname", "");
                boolean sex = sharedPreferences.getBoolean("sex", false);
                int age = sharedPreferences.getInt("age", 0);
                float tall = sharedPreferences.getFloat("tall", 0f);
                Set<String> books = sharedPreferences.getStringSet("books", null);

                Log.i("SharedPreferences", String.format("%s,%s,%s,%s,%s,%s", name, nickname, sex, age, tall, books.toString()));

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("nickname");
                editor.commit();

                if (sharedPreferences.contains("nickname")) {
                    Log.i("SharedPreferences", sharedPreferences.getString("nickname", ""));
                }else{
                    Log.i("SharedPreferences", "key: nickname 不存在");
                }




                editor.clear();
                editor.commit();

                Log.i("SharedPreferences", sharedPreferences.getAll().toString());

            }
        });

    }

    public void changeLanguage(Locale locale) {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, metrics);


//        Configuration config = getBaseContext().getResources().getConfiguration();
//        config.setLocales(locale);
//        Context context  = createConfigurationContext(configuration);
//        Resources resources = context.getResources();


//        configuration.setLocale(locale);
//        getApplicationContext().createConfigurationContext(configuration);

//
//        Context context  = context.createConfigurationContext(configuration);
//        Configuration configuration = context.getResources().getConfiguration();
//        configuration.setLocale(locale);


        //重新启动Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
