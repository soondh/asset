package youkagames.com.yokaasset.module.Book.model;
import youkagames.com.yokaasset.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by songdehua on 2018/12/3.
 */

public class BookStatusModel extends BaseModel{
    public ArrayList<BookStatusData> data;

    public static class BookStatusData{
        public int status_id;
        public String status_name;
    }

}
