package youkagames.com.yokaasset.module.Device.model;

import youkagames.com.yokaasset.model.BaseModel;

import java.util.ArrayList;
/**
 * Created by songdehua on 2018/12/20.
 */

public class DeviceComplexSearchModel extends BaseModel{

    public DataBeanX data;

    public static class DataBeanX {
        public int total;
        public ArrayList<DataBean> data;

        public static class DataBean {
            public int device_id;
            public String device_name;
            public String device_status;
            public String os;
            public String user;
            public String date;
        }
    }
}
