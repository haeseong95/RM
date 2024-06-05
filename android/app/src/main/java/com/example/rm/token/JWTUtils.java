package com.example.rm.token;

import android.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class JWTUtils {

    public static JSONObject decodeJWT(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            String body = getJson(split[1]);
            return new JSONObject(body);
        } catch (UnsupportedEncodingException e) {
            throw new Exception("Error decoding JWT", e);
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
