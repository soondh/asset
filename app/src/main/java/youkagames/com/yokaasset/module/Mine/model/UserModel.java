package youkagames.com.yokaasset.module.Mine.model;

import youkagames.com.yokaasset.model.BaseModel;

/**
 * Created by songdehua on 2018/12/11.
 */

public class UserModel extends BaseModel{
    public DataModel data;
    public static class DataModel {
        public String user_id;
        public String nickname;
        public String img_url;
        public String token;
    }
}
