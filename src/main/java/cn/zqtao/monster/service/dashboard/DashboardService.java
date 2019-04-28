package cn.zqtao.monster.service.dashboard;

import cn.zqtao.monster.model.pojo.vo.BaseDataStatistics;
import cn.zqtao.monster.model.pojo.vo.LatestComment;

import java.util.List;

public interface DashboardService {

    /**
     * 首页统计数据面板
     *
     * @return
     */
    List<BaseDataStatistics> calculateData();


    /**
     * 找出最新的一条评论
     *
     * @return
     */
    LatestComment findLatestComment();


    /**
     * 统计图的数据
     *
     * @return
     */
    List<Object[]> findTableStatistics();


}
