package cn.zqtao.monster.model.pojo.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Data
public class ArticleQueryBO implements Serializable {

    /**
     * 包括 标题、内容等
     */
    private String searchStr;
    private Long cateId;

    /**
     * tag搜索为单独搜索，不和其余两个一起两个联合搜索
     */
    private String tagSearch;

}
