package cn.iocoder.yudao.module.school.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程表
 */
@TableName("school_course")
@KeySequence("school_course_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseDO extends TenantBaseDO {

    @TableId
    private Long id;
    /** 课程名称 */
    private String name;
    /** 类型(0:必修课,1:选修课) */
    private Integer type;
    /** 选修课人数上限(null=不限) */
    private Integer capacity;
    /** 授课教师ID */
    private Long teacherId;
    /** 所属学院ID */
    private Long collegeId;
    /** 排序 */
    private Integer sort;
    /** 状态(0:开启,1:禁用) */
    private Integer status;

}
