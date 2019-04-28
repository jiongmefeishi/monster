package cn.zqtao.monster.service.content;

import cn.hutool.core.util.StrUtil;
import cn.zqtao.monster.dao.repository.CommentRepository;
import cn.zqtao.monster.model.entity.NBComment;
import cn.zqtao.monster.model.entity.permission.NBSysUser;
import cn.zqtao.monster.model.pojo.bo.CommentQueryBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Page<NBComment> findPageInfo(Pageable pageable, CommentQueryBO commentQueryBO) {
        return commentRepository.findAll((Specification<NBComment>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (commentQueryBO.getArticleId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("articleId"), commentQueryBO.getArticleId()));
            }
            if (commentQueryBO.getClearComment() != null && StrUtil.isNotEmpty(commentQueryBO.getClearComment())) {
                predicates.add(criteriaBuilder.like(root.get("clearComment"), "%" + commentQueryBO.getClearComment() + "%"));
            }
            if (commentQueryBO.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), commentQueryBO.getUserId()));
            }
            if (commentQueryBO.getIpCnAddr() != null && StrUtil.isNotEmpty(commentQueryBO.getIpCnAddr())) {
                predicates.add(criteriaBuilder.like(root.get("ipCnAddr"), "%" + commentQueryBO.getIpCnAddr() + "%"));
            }
            Join<NBComment, NBSysUser> userJoin = root.join(root.getModel().getSingularAttribute("user", NBSysUser.class), JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(userJoin.get("id").as(Long.class), root.get("userId")));
            Predicate[] pres = new Predicate[predicates.size()];
            return query.where(predicates.toArray(pres)).orderBy(criteriaBuilder.desc(root.get("post").as(LocalDateTime.class))).getRestriction();
        }, pageable);
    }
}
