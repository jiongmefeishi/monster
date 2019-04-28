package cn.zqtao.monster.service.content;

import cn.hutool.core.util.StrUtil;
import cn.zqtao.monster.dao.repository.MessageRepository;
import cn.zqtao.monster.model.entity.NBMessage;
import cn.zqtao.monster.model.entity.permission.NBSysUser;
import cn.zqtao.monster.model.pojo.bo.MessageQueryBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override

    public Page<NBMessage> findPageInfo(Pageable pageable, MessageQueryBO messageQueryBO) {
        return messageRepository.findAll((Specification<NBMessage>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (messageQueryBO.getClearComment() != null && StrUtil.isNotEmpty(messageQueryBO.getClearComment())) {
                predicates.add(criteriaBuilder.like(root.get("clearComment"), "%" + messageQueryBO.getClearComment() + "%"));
            }
            if (messageQueryBO.getIpCnAddr() != null && StrUtil.isNotEmpty(messageQueryBO.getIpCnAddr())) {
                predicates.add(criteriaBuilder.like(root.get("ipCnAddr"), "%" + messageQueryBO.getIpCnAddr() + "%"));
            }
            if (messageQueryBO.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), messageQueryBO.getUserId()));
            }
            Join<NBMessage, NBSysUser> userJoin = root.join(root.getModel().getSingularAttribute("user", NBSysUser.class), JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(userJoin.get("id").as(Long.class), root.get("userId")));
            Predicate[] pres = new Predicate[predicates.size()];
            return query.where(predicates.toArray(pres)).getRestriction();
        }, pageable);
    }
}
