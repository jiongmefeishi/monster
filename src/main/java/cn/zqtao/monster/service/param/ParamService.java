package cn.zqtao.monster.service.param;

import java.util.Map;

/**
 * 参数查询Service
 */
public interface ParamService {

    /**
     * 根据key查询值
     *
     * @param name
     * @param <T>
     * @return
     */
    <T> T getValueByName(String name);

    /**
     * 初始化设置的保存
     *
     * @param map
     */
    void saveInitParam(Map<String, String[]> map);

    /**
     * 判断是否开启网站统计
     *
     * @return
     */
    boolean isOpenStatisticAnalysis();
}
