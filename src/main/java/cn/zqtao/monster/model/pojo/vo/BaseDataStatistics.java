package cn.zqtao.monster.model.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BaseDataStatistics implements Serializable {

    private String text;
    private long sum;
    @Builder.Default
    private String url = "";
}
