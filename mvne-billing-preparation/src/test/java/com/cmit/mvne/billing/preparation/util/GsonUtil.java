package com.cmit.mvne.billing.preparation.util;

import com.cmit.mvne.billing.preparation.entity.CdrError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2019/11/1
 */
public class GsonUtil {

    private static JsonParser jsonParser = new JsonParser();

    public static String toPrettyFormat(String json) {
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    public static String parseToJsonString(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    public static void main(String[] args) {
        CdrError cdrError = new CdrError();
        cdrError.setId(1L);
        cdrError.setStatus("aa");
        cdrError.setCreateTime(new Date());

        List<String> idList = new ArrayList<String>();
        idList.add("111");
        idList.add("222");

        System.out.println(parseToJsonString(idList));
    }
}
