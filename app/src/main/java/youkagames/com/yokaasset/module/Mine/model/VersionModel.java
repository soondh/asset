package youkagames.com.yokaasset.module.Mine.model;

import youkagames.com.yokaasset.model.BaseModel;

/**
 * Created by songdehua on 2018/12/24.
 */

public class VersionModel extends BaseModel{

    /*
    * data:{"latest_version":"1.1","url":"xxx"}
     */

    public DataBean data;


    public static class DataBean {
        public String name;
        public String url;
        public int version_code;
        public String message;

    }
}
