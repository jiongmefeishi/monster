package cn.zqtao.monster.model.entity.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_role")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBSysRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 11)
    private Long id;

    @Column(nullable = false, length = 50)
    @NotEmpty
    @Length(max = 50)
    private String name;

    @Column(length = 50)
    @NotEmpty
    @Length(max = 50)
    private String cnName;
}
