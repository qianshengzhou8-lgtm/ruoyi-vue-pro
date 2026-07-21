package cn.iocoder.yudao.module.school.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 选课记录表
 */
@TableName("school_course_selection")
@KeySequence("school_course_selection_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseSelectionDO extends TenantBaseDO {

    @TableId
    private Long id;
    /** 课程ID */
    private Long courseId;
    /** 学生ID */
    private Long studentId;

}
