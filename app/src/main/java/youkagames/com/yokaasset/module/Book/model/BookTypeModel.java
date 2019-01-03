package youkagames.com.yokaasset.module.Book.model;

import youkagames.com.yokaasset.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by songdehua on 2018/12/3.
 */

public class BookTypeModel extends BaseModel{
    public ArrayList<BookTypeData> data;

    public static class BookTypeData{
        public int type_id;
        public String name;
    }

}
