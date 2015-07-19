package com.example.jerryyin.myhttpmethoddemo01.ParaseXml;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jerryyin.myhttpmethoddemo01.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;


/**
 * Created by JerryYin on 7/12/15.
 *
 */
public class HttpClientGetXmlToParase extends Activity implements View.OnClickListener {

    /**Constants*/
    public static final int SHOW_RESPONSE = 0;

    /**Views*/
    private Button mbtnSendRequest;
    private TextView mtvResult;
    private EditText metContext;

    /**Values*/
    private MyHandler mhandler;
    private String url = "http://fanyi.youdao.com/openapi.do?keyfrom=MyDictionaryyoudao&key=1718920469&type=data&doctype=xml&version=1.1&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
    }

    public void setupView() {
        mbtnSendRequest = (Button) findViewById(R.id.btn_sendrequest);
        mtvResult = (TextView) findViewById(R.id.tv_show);
        metContext = (EditText) findViewById(R.id.et_context);
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
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url + metContext.getText());
                    HttpResponse response = httpClient.execute(httpGet);

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

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String result = msg.obj.toString();
                    System.out.println("result = " + result);

                    String data = PullParaseXML(result);
                    mtvResult.setText(data);

                    break;

                default:
                    break;
            }
        }
    }

    /**
     * Pul方式解析XM文件
     * */
    public String PullParaseXML(String xmlData){
        String data_basic = null;
        String data_web = null;
        try {
            XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlData));
            int eventType = parser.getEventType();      //当前的解析事件

            String phonetic = "";   //音标
            String usPhonetic = "";
            String ukPhonetic = "";
            String ex = "";         //基本释义
            String paragraph = "";  //句子
            String key = null;        // 网络释义
            String exWeb = null;
            int i = 0;
            int k = 0;

            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = parser.getName();     //当前节点的名字
                switch (eventType){
                    //开始解析某个节点
                    case XmlPullParser.START_TAG:{
                        if ("phonetic".equals(nodeName)) {
                            phonetic = "音  标  ：[" + parser.nextText() + "]";
                            System.out.println("phonetic = " + phonetic);

                        }
                        else if ("us-phonetic".equals(nodeName)){
                            usPhonetic = "美式音标：[" + parser.nextText() + "]";
                            System.out.println("usPhonetic = " + usPhonetic);

                        }
                        else if ("uk-phonetic".equals(nodeName)){
                            ukPhonetic = "英式音标：[" + parser.nextText() + "]";
                            System.out.println("ukPhonetic = " + ukPhonetic);

                        }
                        else if ("ex".equals(nodeName)){
                            ex += "      [" + parser.nextText() + "]" + "\n";     //注意，解释ex有多行，需要累加
                        }
                        //网络释义：
                        else if ("key".equals(nodeName)){
                            key += "例 句 ： " + parser.nextText() + "\n";
//                            System.out.println("key = " + key[i]);
//                            i++;
                        }
                        else if ("value".equals(nodeName)){
                            exWeb += "      [" + parser.nextText() + "]" + "\n";
//                            System.out.println("exWeb = " + exWeb[1]);
//                            k++;
                        }

//                        else if ("web".equals(nodeName)){
//                            System.out.println("web = " + nodeName);
//                            if ("explain".equals(nodeName)){
//                                System.out.println("explain = " + nodeName);
//                                if ("key".equals(nodeName)){
//                                    key[i] = "例 句 ： " + parser.nextText();
//                                    System.out.println("key = " + key[i]);
//                                }
//                                else if ("ex".equals(nodeName)){
//                                    exWeb[i] += "      [" + parser.nextText() + "]" + "\n";
//                                    System.out.println("exWeb = " + exWeb[i]);
//                                }
//                                i++;
//                                System.out.println("i = " + i);
//                            }
//                        }
                        break;
                    }

                    //完成解析这个节点
                    case XmlPullParser.END_TAG:{
                        if ("basic".equals(nodeName)){
                            data_basic = phonetic + "\n" +usPhonetic + "\n" +ukPhonetic + "\n" + "基本释义 ：" + "\n" + ex;
                            System.out.println("data_basic = " + data_basic);
                        }
                        else if ("web".equals(nodeName)){
//                            for (int j = 0; j <i ; j++){
//                                data_web += key[j] + "\n" + exWeb[j] + "\n";
//                            }
                            data_web = key + "\n" + exWeb + "\n";
                        }

                       break;
                    }
                    default:
                        break;
                }

                eventType = parser.next();  //获取下一个解析事件
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = data_basic + "网络释义 ：" + data_web;
        return result;
    }


}
