package youkagames.com.yokaasset.module.Game.model;

import youkagames.com.yokaasset.model.BaseModel;

import java.util.ArrayList;
/**
 * Created by songdehua on 2018/12/7.
 */

public class GameLanguageModel extends BaseModel{
    public DataBeanX data;

    public static class DataBeanX {
        public int num;
        public ArrayList<DataBean> languagelist;

        public static class DataBean {
            public int id;
            public String language;
        }
    }
}
