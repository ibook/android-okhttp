package cn.netkiller.okhttp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleActivity extends AppCompatActivity {

    private TextView clock;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    update(msg.obj.toString());
                    break;
            }
        }

        void update(String c) {

            clock.setText(c);
        }
    };

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        public void run() {
            Message message = new Message();
            message.what = 1;
            message.obj = dateFormat.format(new Date());
            handler.sendMessage(message);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        clock = (TextView) findViewById(R.id.clock);
        clock.setText("Today is ...");
        timer.schedule(task, 1000 * 5, 1000); //启动timer

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

}
