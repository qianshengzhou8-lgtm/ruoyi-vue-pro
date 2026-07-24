package cn.iocoder.yudao.module.school.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 班级表
 *
 * @author school
 */
@TableName("school_class")
@KeySequence("school_class_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClassDO extends TenantBaseDO {

    @TableId
    private Long id;
    /** 班级名称 */
    private String name;
    /** 所属专业ID */
    private Long majorId;
    /** 排序 */
    private Integer sort;
    /** 状态(0:开启,1:禁用) */
    private Integer status;

}
