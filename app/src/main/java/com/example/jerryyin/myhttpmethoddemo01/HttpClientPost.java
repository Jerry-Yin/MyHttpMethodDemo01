package com.example.jerryyin.myhttpmethoddemo01;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 7/12/15.
 */
public class HttpClientPost extends Activity implements View.OnClickListener {

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
                HttpPostMethod postMethod = new HttpPostMethod();
                postMethod.start();
                break;
            default:
                break;
        }
    }

    class HttpPostMethod extends Thread{
        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("Http://www.baidu.com");
            try {
                //设置要发送给服务器的数据
                List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("username", "admin"));
                params.add(new BasicNameValuePair("password", "123456"));

                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "utf-8");
                httpPost.setEntity(urlEncodedFormEntity);
                HttpResponse response = httpClient.execute(httpPost);

                if (response.getStatusLine().getStatusCode() == 200){
                    //请求成功

                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity, "utf-8");

                    Message message = new Message();
                    message.what = ShowAnswer;
                    message.obj = result.toString();
                    mhandler.sendMessage(message);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class UiHandler extends Handler {
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
