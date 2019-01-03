package youkagames.com.yokaasset.db.converter;


import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import youkagames.com.yokaasset.utils.GsonHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by songdehua on 2018/3/20.
 * room数据库的类型转换器
 */

public class Converter {

    @TypeConverter
    public static String listStringToString(List<String> list){
        if(list != null && !list.isEmpty()){
            return GsonHelper.getGsonInstance().toJson(list);
        }
        return "";
    }

    @TypeConverter
    public static List<String> fromString(String items){
        List<String> list = new ArrayList<>();
        if(!TextUtils.isEmpty(items)){
            list = GsonHelper.getGsonInstance().fromJson(items , List.class);
        }
        return list;
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


}
