package cn.iocoder.yudao.module.school.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专业表
 *
 * @author school
 */
@TableName("school_major")
@KeySequence("school_major_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class MajorDO extends TenantBaseDO {

    @TableId
    private Long id;
    /** 专业名称 */
    private String name;
    /** 所属学院ID */
    private Long collegeId;
    /** 排序 */
    private Integer sort;
    /** 状态(0:开启,1:禁用) */
    private Integer status;

}
