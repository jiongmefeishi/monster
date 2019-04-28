package cn.zqtao.monster.model.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QqLoginModel implements Serializable {

    private String callbackDomain;
    private String code;


}
