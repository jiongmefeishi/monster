package cn.zqtao.monster.model.pojo.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Data
public class CommentQueryBO implements Serializable {
    private Long articleId;
    private String clearComment;
    private String ipCnAddr;
    private Long userId;
}
