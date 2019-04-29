package cn.zqtao.monster.service.param;

import cn.zqtao.monster.dao.repository.ParamRepository;
import cn.zqtao.monster.model.entity.NBParam;
import cn.zqtao.monster.util.NBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Map;

import static cn.zqtao.monster.model.constant.Monster.Param.STATISTIC_ANALYSIS;

@Service
@Transactional(rollbackOn = Exception.class)
public class ParamServiceImpl implements ParamService {

    private final ParamRepository paramRepository;

    @Autowired
    public ParamServiceImpl(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    @Override
    public <T> T getValueByName(String name) {
        NBParam param = paramRepository.findByName(name);
        return param != null ? param.getValue() : null;
    }

    @Override
    public void saveInitParam(Map<String, String[]> map) {
        Map<String, Object> param = NBUtils.getParameterMap(map);
        for (Map.Entry<String, Object> next : param.entrySet()) {
            String key = next.getKey();
            if (!"username".equals(key) && !"password".equals(key)) {
                if (!StringUtils.isEmpty(next.getValue())) {
                    String value = (String) next.getValue();
                    paramRepository.updateInitParam(value, key);
                    if ("upload_type".equals(key) && !"LOCAL".equals(next.getValue())) {
                        paramRepository.updateInitParam("1", "is_open_oss_upload");
                    }
                }
            }
        }
    }

    @Override
    @Cacheable(value = "paramCache", key = "'statistic_analysis'")
    public boolean isOpenStatisticAnalysis() {
        NBParam p = paramRepository.findByName(STATISTIC_ANALYSIS);
        final String open = "1";
        if (p != null) {
            return open.equals(p.getValue());
        }
        return false;
    }
}
