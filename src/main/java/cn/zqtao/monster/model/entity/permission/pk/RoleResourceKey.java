package cn.zqtao.monster.model.entity.permission.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class RoleResourceKey implements Serializable {

    @Column(length = 11, nullable = false)
    private Long roleId;

    @Column(length = 11, nullable = false)
    private Long resourceId;
}