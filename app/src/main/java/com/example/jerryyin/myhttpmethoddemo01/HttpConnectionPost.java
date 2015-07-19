package com.example.jerryyin.myhttpmethoddemo01;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpConnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by JerryYin on 7/11/15.
 */
public class HttpConnectionPost extends Activity implements View.OnClickListener {

    /**Constants*/
    private static final int ShowAnswer = 0;

    /**Views*/
    private Button mbtnSendRequest;
    private TextView mtvResult;

    /**Valuse*/
    private UiHandler mhandler;

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
        mhandler = new UiHandler();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendrequest:
                HttpConnectionPost();
                break;
            default:
                break;
        }
    }

    public void HttpConnectionPost(){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("Http://www.baidu.com");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("ecoding", "UTF-8");
                    connection.setDoInput(true);    //可以接受数据
                    connection.setDoOutput(true);   //可以发送数据
                    connection.setRequestMethod("POST");

                    //连接建立后先设置输出流
                    OutputStream outputStream = connection.getOutputStream();
                    OutputStreamWriter osWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(osWriter);

                    //对服务器输出
                    bufferedWriter.write(1);
                    bufferedWriter.flush();     //  强制将数据发出，避免锁死的状态

                    //处理输入流接受返回数据
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(inputStreamReader);

                    //接收数据
                    String line = null;
                    StringBuffer buffer = new StringBuffer();
                    while ((line = reader.readLine()) != null ){
                        buffer.append(line);
                    }
                    Message message = new Message();
                    message.what = ShowAnswer;
                    message.obj =buffer.toString();
                    mhandler.sendMessage(message);

                            //关闭数据流
                    outputStream.close();
                    osWriter.close();
                    bufferedWriter.close();
                    inputStream.close();
                    inputStreamReader.close();
                    reader.close();



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public class UiHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ShowAnswer:
                    String result = String.valueOf(msg.what);
                    mtvResult.setText(result);
                    break;
            }
        }
    }
}
