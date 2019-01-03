package youkagames.com.yokaasset.module.Game.model;

import youkagames.com.yokaasset.model.BaseModel;

import java.util.ArrayList;
/**
/**
 * Created by songdehua on 2018/12/6.
 */

public class DepartmentListModel extends BaseModel{
    public ArrayList<DepartmentData> data;

    public static class DepartmentData {
        public int department_id;
        public String name;
    }
}
