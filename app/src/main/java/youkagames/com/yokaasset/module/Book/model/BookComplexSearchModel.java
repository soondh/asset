package youkagames.com.yokaasset.module.Book.model;

import youkagames.com.yokaasset.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by songdehua on 2018/12/3.
 */

public class BookComplexSearchModel extends BaseModel{
    public DataBeanX data;

    public static class DataBeanX {
        public int num;
        public int total;
        public ArrayList<DataBean> booklist;

        public static class DataBean {
            public int id;
            public String name;
            public String code;
            public String isbn;
            public String author;
            public String publisher;
            public String pubTime;
            public int statu;
            public int category;
            public String msg;
            public String departmentUserId;
            public String departmentUser;
            public String user;
            public String borrowDate;
            public String returnDate;
            public String statuName;
            public String categoryName;
            public String department;
            public int departmentId;
            public boolean opened;
        }
    }
}
