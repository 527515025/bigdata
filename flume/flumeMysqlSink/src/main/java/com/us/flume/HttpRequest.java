package com.us.flume;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyibo on 17/2/18.
 */
public class HttpRequest {
    public static String sendPost(String url, String par) throws IOException {
        StringBuffer buffer = new StringBuffer(); //用来拼接参数
        StringBuffer result = new StringBuffer(); //用来接受返回值
        URL httpUrl = null; //HTTP URL类 用这个类来创建连接
        URLConnection connection = null; //创建的http连接
        PrintWriter printWriter = null;
        BufferedReader bufferedReader; //接受连接受的参数
        //创建URL
        httpUrl = new URL(url);
        //建立连接
        connection = httpUrl.openConnection();
        connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        connection.setRequestProperty("connection", "keep-alive");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        printWriter = new PrintWriter(connection.getOutputStream());

                buffer.append("content").append("=").append(URLEncoder.encode(par, "utf-8"));

        printWriter.print(buffer.toString());
        printWriter.flush();
        connection.connect();
        //接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        bufferedReader.close();
        return result.toString();
}
    public static void main(String [] agers){
        String url="http://localhost:8080/dataCollect/rawEvents";
        String body="Parsing events: Omegamon_Base;cms_hostname='itmserver';cms_port='370$i’;integration_type='U';master_reset_flag='';appl_label='';situation_name='disk';situation_type='S';situation_origin='itmserver:LZ';situation_time='01/06/2017 11:33:$i.000';situation_status='N';situation_thrunode='TEMS_TEST';situation_fullname='home_disk_error';situation_displayitem='';source='ITM';sub_source='itmserver:LZ';hostname='itmserver';origin='192.168.100.$i’;adapter_host='itmserver';date=‘$i/06/2017';severity='CRITICAL';msg='itm server home directory > 80%';situation_eventdata='~';END";

        Map<String, Object> params=new HashMap<>();
        params.put("content",body);
        try{
            System.out.println("------------"+sendPost(url,body));
        }catch (Exception ex){
            ex.getMessage();
        }
    }
}
