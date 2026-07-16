package cn.iocoder.yudao.module.school.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 学生表
 *
 * @author school
 */
@TableName("school_student")
@KeySequence("school_student_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentDO extends TenantBaseDO {

    @TableId
    private Long id;
    /** 姓名 */
    private String name;
    /** 登录账号 */
    private String username;
    /** 登录密码 */
    @ToString.Exclude
    private String password;
    /** 手机号 */
    private String mobile;
    /** 所属班级ID */
    private Long classId;
    /** 状态(0:开启,1:禁用) */
    private Integer status;

}
