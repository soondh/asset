package youkagames.com.yokaasset.utils;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import yokastore.youkagames.com.support.utils.LogUtil;

import java.lang.reflect.Type;

/**
 * Created by songdehua on 2018/11/23.
 * 用来帮助获得带有几个处理解析异常的gson对象
 */

public class GsonHelper {
    private static Gson defaultGson;

    static {
        defaultGson = GsonHelper.getGsonInstance();
    }

    /**
     * 获得gson对象
     * @return
     */
    public static Gson getGsonInstance(){
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(Integer.class , new IntegerDefaultAdapter())
                .registerTypeAdapter(Object.class , new DefaultAdapter())
                .registerTypeAdapter(String.class , new StringAdapter());
        return builder.create();
    }

    /**
     * int类型的基础解析类
     */
    private static class IntegerDefaultAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

        @Override
        public Integer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            try{
                if(TextUtils.isEmpty(jsonElement.getAsString())){
                    // 如果返回的是一个空值，那么返回默认0
                    return 0;
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            try{
                // 如果是一个正常的int值 ， 那么在这里返回
                return jsonElement.getAsInt();
            } catch (Exception e){
                // 发生任何异常返回默认0
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        public JsonElement serialize(Integer integer, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(integer);
        }
    }

    /**
     * 默认解析处理，如果发生错误那么给一个null值
     */
    private static class DefaultAdapter implements JsonSerializer, JsonDeserializer {

        @Override
        public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Object result = null;
            try{
                result = defaultGson.fromJson(jsonElement , type);
            } catch (Exception e){
                LogUtil.i("http" , "发生解析异常，给一个默认值");
                e.printStackTrace();
                try {
                    result = type.getClass().newInstance();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
            return result;
        }

        @Override
        public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
            return defaultGson.toJsonTree(o);
        }
    }

    /**
     * 解析字符串的适配器
     */
    private static class StringAdapter implements JsonSerializer<String>, JsonDeserializer<String> {

        @Override
        public String deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String result = "";
            try{
                result = jsonElement.getAsString();
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public JsonElement serialize(String o, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(o);
        }
    }
}

