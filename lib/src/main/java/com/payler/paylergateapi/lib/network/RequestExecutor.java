package com.payler.paylergateapi.lib.network;

import com.google.common.base.CaseFormat;
import com.payler.paylergateapi.lib.model.ConnectionException;
import com.payler.paylergateapi.lib.model.request.GateRequest;
import com.payler.paylergateapi.lib.model.response.AdvancedStatusResponse;
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
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RequestExecutor {

    private HttpClient mClient;

    private List<NameValuePair> mParams;

    private ObjectMapper mObjectMapper;

    public RequestExecutor() {
        mObjectMapper = new ObjectMapper();
        mObjectMapper.setPropertyNamingStrategy(new PropertyNamingStrategy
                .LowerCaseWithUnderscoresStrategy());
        mObjectMapper.setVisibilityChecker(mObjectMapper.getSerializationConfig()
                                                   .getDefaultVisibilityChecker()
                                                   .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                                                   .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                                                   .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                                                   .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        mObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        SimpleModule module = new SimpleModule("AdvancesStatusResponseDesealizer", Version.unknownVersion());
        module.addDeserializer(AdvancedStatusResponse.UserEnteredParams.class, new UserEnteredParamsStdDeserializer());
        mObjectMapper.registerModule(module);

    }

    public Response executeRequest(String url, GateRequest request, Class<? extends Response> cls)
            throws ConnectionException {

        if (mClient == null) {
            mClient = HttpClientFactory.createPinnedHttpClient();
//            mClient = HttpClientFactory.createHttpClient();
        }

        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/x-www-form-urlencoded");
        post.setHeader("Accept", "application/json");

        mParams = getParams(request);

        Response response = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(mParams));

            HttpResponse httpResponse = mClient.execute(post);

            StatusLine statusLine = httpResponse.getStatusLine();
            HttpEntity entity = httpResponse.getEntity();

            if (entity == null) {
                throw new ClientProtocolException("Response contains no content");
            }

            response = generateResponse(entity, cls);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionException();
        }

        return response;
    }

    private Response generateResponse(HttpEntity entity, Class<? extends Response> cls)
            throws IOException {
        InputStream dataStream = entity.getContent();
        if (dataStream == null) {
            throw new JsonMappingException("Input stream can't be null");
        }

        BufferedReader rd = new BufferedReader(new InputStreamReader(dataStream));
        String line = "";
        StringBuilder builder = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            builder.append(line);
        }

        String content = builder.toString();
        Response response;
        try {
            response = mObjectMapper.readValue(content, cls);
        } catch (UnrecognizedPropertyException e) {
            response = mObjectMapper.readValue(content, GateError.class);
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
                if ("java.util.HashMap".equals(field.getType().getName())) {
                    HashMap<String, String> hashmapFieldValue = (HashMap<String, String>) value;
                    for (final Map.Entry<String, String> customParamsMapEntry : hashmapFieldValue.entrySet()) {
                        String currentName = customParamsMapEntry.getKey();
                        String currentValue = customParamsMapEntry.getValue();
                        if (currentName != null && currentValue != null) {
                            currentName = "pay_page_param_" + currentName;
                            currentName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, currentName);
                            nameValuePairs.add(new BasicNameValuePair(currentName, currentValue));
                        }
                    }
                } else if (value != null) {
                    fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
                    nameValuePairs.add(new BasicNameValuePair(fieldName, value.toString()));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return nameValuePairs;
    }

    public static class UserEnteredParamsStdDeserializer
            extends StdDeserializer<AdvancedStatusResponse.UserEnteredParams> {

        public UserEnteredParamsStdDeserializer() {
            super(AdvancedStatusResponse.UserEnteredParams.class);
        }

        @Override
        public AdvancedStatusResponse.UserEnteredParams deserialize(
                final JsonParser jp,
                final DeserializationContext ctxt)

                throws IOException {
            AdvancedStatusResponse.UserEnteredParams result = new AdvancedStatusResponse.UserEnteredParams();

            JsonNode jsonNode = jp.getCodec().readTree(jp);
            Iterator<String> fieldNames = jsonNode.getFieldNames();
            while (fieldNames.hasNext()) {
                String name = fieldNames.next();
                String value = jsonNode.get(name).getTextValue();
                result.put(name, value);
            }
            return result;
        }
    }
}
