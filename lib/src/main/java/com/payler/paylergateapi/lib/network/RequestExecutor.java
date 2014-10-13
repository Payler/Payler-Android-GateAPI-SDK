package com.payler.paylergateapi.lib.network;

import com.google.common.base.CaseFormat;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.payler.paylergateapi.lib.model.request.GateRequest;
import com.payler.paylergateapi.lib.model.response.GateError;
import com.payler.paylergateapi.lib.model.response.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RequestExecutor {

    private HttpClient mClient;
    private List<NameValuePair> mParams;

    public Response executeRequest(String url, GateRequest request, Class <? extends Response> cls) {

        if (mClient == null) {
            mClient = HttpClientFactory.createPinnedHttpClient();
        }

        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/x-www-form-urlencoded");
        post.setHeader("Accept", "application/json");

        mParams = getParams(request);

        try {
            post.setEntity(new UrlEncodedFormEntity(mParams));

            HttpResponse httpResponse = mClient.execute(post);

            StatusLine statusLine = httpResponse.getStatusLine();
            HttpEntity entity = httpResponse.getEntity();

            if (entity == null) {
                throw new ClientProtocolException("Response contains no content");
            }

            Response response = generateResponse(entity, cls);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Response generateResponse(HttpEntity entity, Class<? extends Response> cls)
            throws IOException, JsonSyntaxException {
        InputStream dataStream = entity.getContent();
        if (dataStream == null) {
            throw new JsonSyntaxException("Input stream can't be null");
        }

        BufferedReader rd = new BufferedReader(new InputStreamReader(dataStream));
        String line = "";
        StringBuilder builder = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            builder.append(line);
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        String content = builder.toString();
        Response response;
        try {
            response = gson.fromJson(content, cls);
        } catch (JsonSyntaxException e) {
            response = gson.fromJson(content, GateError.class);
        }
//        validateResponse(response);
        return response;
    }

    private List<NameValuePair> getParams(GateRequest request) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        for (Field field : request.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(request);
                String fieldName = field.getName();

                if (value != null) {
                    fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
                    nameValuePairs.add(new BasicNameValuePair(fieldName, value.toString()));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return nameValuePairs;
    }

}
