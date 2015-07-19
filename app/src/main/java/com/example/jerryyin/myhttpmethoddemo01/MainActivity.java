package com.example.jerryyin.myhttpmethoddemo01;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * HttpClient 方式的Get方式，此处用了两种方式实现：
 *  1.借助AsyncTask<>实现；
 *  2.直接开启一个线程，再加上Handle刷新界面；
 * */

public class MainActivity extends Activity implements View.OnClickListener {

    /**Constants*/
    public static final int SHOW_RESPONSE = 0;

    /**Views*/
    private Button mbtnSendRequest;
    private TextView mtvResult;

    /**Values*/
    private MyHandler mhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
    }

    public void setupView() {
        mbtnSendRequest = (Button) findViewById(R.id.btn_sendrequest);
        mtvResult = (TextView) findViewById(R.id.tv_show);
        mbtnSendRequest.setOnClickListener(this);
        mhandler = new MyHandler();         //注意，千万不能在线程中 new handler，会产生严重的错误；
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendrequest:
                Log.d("--------onClick(v)", String.valueOf(v.getId()));
                sendRequestWithHttpClient();
//                methord2();
                break;
            default:
                break;
        }
    }

    public void sendRequestWithHttpClient() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.d("--------run()", "1");

                    HttpClient httpClient = new DefaultHttpClient();
//                    Log.d("--------run()", "2");
                    HttpGet httpGet = new HttpGet("http://www.baidu.com");
//                    Log.d("--------run()", "3");
                    HttpResponse response = httpClient.execute(httpGet);
//                    Log.d("--------execute()", "1");

                    if (response.getStatusLine().getStatusCode() == 200) {
                        //服务器返回的状态码为 200，说明请求成功，进行数据接收操作：
                        HttpEntity entity = response.getEntity();
//                      String result = EntityUtils.toString(entity);       //如果返回的结果里卖弄含有中文字符的话，直接toString（）会乱码，此时指定编码方式“UTF-8”，如下
                        String result = EntityUtils.toString(entity, "utf-8");

                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = result.toString();
                        mhandler.sendMessage(message);    //将结果发送给handler机制，更新UI
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String result = msg.obj.toString();
                    mtvResult.setText(result);
                    break;

                default:
                    break;
            }
        }
    }

    public void methord2() {
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://www.baidu.com");
                    HttpResponse response = httpClient.execute(httpGet);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        //服务器返回的状态码为 200，说明请求成功，进行数据接收操作：
                        HttpEntity entity = response.getEntity();
//                      String result = EntityUtils.toString(entity);       //如果返回的结果里卖弄含有中文字符的话，直接toString（）会乱码，此时指定编码方式“UTF-8”，如下
                        String result = EntityUtils.toString(entity, "utf-8");
                        return result;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mtvResult.setText(s.toString());
            }
        }.execute("http://www.baidu.com");
    }

}
