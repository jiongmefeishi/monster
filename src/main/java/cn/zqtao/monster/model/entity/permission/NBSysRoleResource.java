package cn.zqtao.monster.model.entity.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.zqtao.monster.model.entity.permission.pk.RoleResourceKey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "sys_role_resource")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBSysRoleResource implements Serializable {

    @EmbeddedId
    private RoleResourceKey pk;

    @Column(nullable = false, columnDefinition = "tinyint(1)")
    @Builder.Default
    private Boolean enable = Boolean.TRUE;
}
