package cn.zqtao.monster.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nb_cloud_file")
@Entity
@Builder
public class NBCloudFile implements Serializable {

    /**
     * 主键id
     * 自增长生成策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, length = 11)
    private Long id;

    @Column(length = 11, nullable = false)
    @NotNull(message = "项目必须属于一个分类下")
    private Long cateId;

    @ManyToOne
    @JoinColumn(name = "cate_refer_id")
    @NotNull
    private NBCloudFileCate cloudFileCate;

    @Column(updatable = false)
    private LocalDateTime post;

    private LocalDateTime modify;

    @NotEmpty(message = "名称不能为空")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "描述不能为空")
    @Column(length = 500)
    private String description;

    @NotEmpty(message = "主页地址不能为空")
    private String url;

}
