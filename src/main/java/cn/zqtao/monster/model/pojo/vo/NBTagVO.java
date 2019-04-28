package cn.zqtao.monster.model.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import cn.zqtao.monster.model.entity.NBTag;

@Setter
@Getter
@ToString
public class NBTagVO extends NBTag {
    private String selected;
}
