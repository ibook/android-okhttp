package cn.netkiller.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpActivity extends AppCompatActivity {

    TextView textView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        textView = (TextView) findViewById(R.id.textView);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                textView.setText((String) msg.obj);
            }
        };


//        String url = "https://www.baidu.com";
        String url = "https://dev.bitvaluebk.com/member/json";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("okhttp", "Connect timeout. " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String text = response.body().string();
                Log.d("okhttp", "HTTP Code " + response.code() + " TEXT " + text);
                Log.d("okhttp", "Protocol: " + response.protocol());

                Message msg = new Message();
                msg.what = 0;
                msg.obj = text;
                mHandler.sendMessage(msg);

            }
        });

    }
}
