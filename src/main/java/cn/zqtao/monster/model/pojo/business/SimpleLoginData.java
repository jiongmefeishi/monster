package cn.zqtao.monster.model.pojo.business;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Data
public class SimpleLoginData implements Serializable {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private String bmyName;
    private String bmyPass;
    private Boolean remember;
    private String vercode;

}
