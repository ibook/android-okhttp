package cn.netkiller.okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import cn.netkiller.okhttp.pojo.Oauth;
import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class Oauth2jwtActivity extends AppCompatActivity {

    private TextView token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth2jwt);

        token = (TextView) findViewById(R.id.token);

        try {
            get();
            post();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static Oauth accessToken() throws IOException {


        OkHttpClient client = new OkHttpClient.Builder().authenticator(
                new Authenticator() {
                    public Request authenticate(Route route, Response response) {
                        String credential = Credentials.basic("api", "secret");
                        return response.request().newBuilder().header("Authorization", credential).build();
                    }
                }
        ).build();

        String url = "http://192.168.0.185:8080/oauth/token";

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username", "blockchain")
                .add("password", "123456")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("服务器端错误: " + response);
        }

        Gson gson = new Gson();
        Oauth oauth = gson.fromJson(response.body().string(), Oauth.class);
        Log.i("oauth", oauth.toString());
        return oauth;
    }

    public void get() throws IOException {


        OkHttpClient client = new OkHttpClient.Builder().authenticator(
                new Authenticator() {
                    public Request authenticate(Route route, Response response) throws IOException {
                        return response.request().newBuilder().header("Authorization", "Bearer " + accessToken().getAccess_token()).build();
                    }
                }
        ).build();

        String url = "http://192.168.0.185:8080/misc/nfc/compatibility";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("oauth", myResponse);
                        token.setText(myResponse);
                    }
                });

            }
        });
    }


    public void post() throws IOException {


        OkHttpClient client = new OkHttpClient.Builder().authenticator(
                new Authenticator() {
                    public Request authenticate(Route route, Response response) throws IOException {
                        return response.request().newBuilder().header("Authorization", "Bearer " + accessToken().getAccess_token()).build();
                    }
                }
        ).build();

        String url = "http://192.168.0.185:8080/history/create";

        String json = "{\n" +
                "  \"assetsId\": \"5bced71c432c001c6ea31924\",\n" +
                "  \"title\": \"添加信息\",\n" +
                "  \"message\": \"信息内容\",\n" +
                "  \"status\": \"录入\"\n" +
                "}";

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        Gson gson = new Gson();
//                        Oauth oauth = gson.fromJson(myResponse, Oauth.class);
//                        Log.i("oauth", oauth.toString());

                        token.setText(myResponse);
                    }
                });

            }
        });
    }

}
