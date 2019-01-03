package youkagames.com.yokaasset.module.Device.model;

import youkagames.com.yokaasset.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by songdehua on 2018/12/20.
 */

public class DeviceTypeModel extends BaseModel{
    public ArrayList<DeviceTypeData> data;
    public static class DeviceTypeData {
        public String type;
        public int type_id;
        public String icon;
    }
}
