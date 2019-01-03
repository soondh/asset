package youkagames.com.yokaasset.module.AssetList.model;

import youkagames.com.yokaasset.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by songdehua on 2018/11/29.
 */

public class AssetListModel extends BaseModel{
    public ArrayList<AssetListData> data;

    public static class AssetListData{
        public int id;
        public String name;
        public String icon;
    }
}
