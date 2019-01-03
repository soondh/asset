package youkagames.com.yokaasset.module.Game.model;

import youkagames.com.yokaasset.model.BaseModel;

import java.util.ArrayList;
/**
 * Created by songdehua on 2018/12/6.
 */

public class GameInfoModel extends BaseModel{
    public DataBeanX data;

    public static class DataBeanX {
        public int num;
        public ArrayList<DataBean> gamelist;
    }

    public static class DataBean {
        public int id;
        public String eName;
        public String cName;
        public String jName;
        public String oName;
        public ArrayList<Integer> languageId;
        public String code;
        public String publisher;
        public int maxpepnum;
        public int minpepnum;
        public int maxTime;
        public int minTime;
        public int maxAge;
        public int minAge;
        public String label;
        public int addressId;
        public int statu;
        public int gstatuId;
        public String msg;
        public String departmentUser;
        public String user;
        public String borrowDate;
        public String returnDate;
        public String statuName;
        public String department;
        public int departmentId;
        public ArrayList<String> language;
        public String adress;
        public String gstatuname;
        public boolean opened;
    }
}
