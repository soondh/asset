package youkagames.com.yokaasset.module.Game.model;

import youkagames.com.yokaasset.model.BaseModel;

import java.util.ArrayList;
/**
 * Created by songdehua on 2018/12/6.
 */

public class GameTypeModel {
    public ArrayList<GameTypeData> data;

    public static class GameTypeData{
        public int type_id;
        public String name;
    }
}
