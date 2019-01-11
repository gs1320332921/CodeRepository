package com.gs.coderepository.utils;

import com.gs.coderepository.bean.HttpResult;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by 13203 on 2019-01-11.
 */

public class HttpUrlConnectionUtils {

    public static HttpResult doPost(Map map, String postUrl){
        try {
            URL url = new URL(postUrl);
            //2.开水闸---openCOnnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            byte[] data = getRequestData(map, "utf-8").toString().getBytes();
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            int code = httpURLConnection.getResponseCode();

            InputStream is = httpURLConnection.getInputStream();
            ByteArrayOutputStream message = new ByteArrayOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = is.read(bytes)) != -1) {
                // 根据读取的长度写入到os对象中
                message.write(bytes, 0, len);
            }
            is.close();
            message.close();
            String str = new String(message.toByteArray(), "utf-8");
            System.out.println(code+"|"+str);
            return new HttpResult(str,code);
        }catch (Exception e){
            return null;
        }
    }


    /*
   * Function  :   封装请求体信息
   * Param     :   params请求体内容，encode编码格式
   */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }
}
