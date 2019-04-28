package cn.zqtao.monster.model.pojo.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Data
public class MessageQueryBO implements Serializable {
    private String clearComment;
    private String ipCnAddr;
    private Long userId;
}
