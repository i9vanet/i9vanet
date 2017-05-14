/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common.util;

import br.com.virtualVanets.common.Command;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.OperationRequestDevice;
import br.com.virtualVanets.common.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class RestClient {

    public Result sendCommandRest(String ip, Device device, Command command)
            throws Exception {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            Gson gson = new GsonBuilder().setDateFormat(OperationRequestDevice.PARTTERN_DATE).create();
            String jsonDevice = gson.toJson(device);
            String jsonCommand = gson.toJson(command);
            StringBuffer sbUrl = new StringBuffer(ip);
            // Encode url, para tratar os caracteres do padrão JSON, tipo o '{' e '}'
            //jsonDevice = URLEncoder.encode(jsonDevice, "UTF-8");
            //jsonCommand = URLEncoder.encode(jsonCommand, "UTF-8");
            //sbUrl.append(jsonDevice).append(",").append(jsonCommand);
            //System.out.println("url " + sbUrl);
            System.out.println("IP " + ip);
            HttpPost httpPost = new HttpPost(ip);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("device", jsonDevice));
            params.add(new BasicNameValuePair("cmd", jsonCommand));
//            httpPost.setEntity(new StringEntity(gson.toJson(device)));
            //params.add(new StringEntity(gson.toJson(device)));
            //params.add(new StringEntity(gson.toJson(command)));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            //HttpResponse httpResponse = httpclient.execute(new HttpGet(sbUrl.toString()));
            //HttpResponse httpResponse = httpclient.execute(new HttpPost(sbUrl.toString()));
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // receive response as inputStream
            String resultJson = "";
            try {
                InputStream inputStream = httpResponse.getEntity().getContent();
                // convert inputstream to string
                if (inputStream != null) {
                    resultJson = convertInputStreamToString(inputStream);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                throw e;
            }
            System.out.println("json  " + resultJson);
            Result result = gson.fromJson(resultJson, Result.class);
            return result;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        inputStream.close();
        return result.toString();
    }

}
