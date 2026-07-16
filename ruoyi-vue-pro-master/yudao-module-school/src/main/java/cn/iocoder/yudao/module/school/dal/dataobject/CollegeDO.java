package cn.iocoder.yudao.module.school.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 学院表
 *
 * @author school
 */
@TableName("school_college")
@KeySequence("school_college_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class CollegeDO extends TenantBaseDO {

    @TableId
    private Long id;
    /** 学院名称 */
    private String name;
    /** 排序 */
    private Integer sort;
    /** 状态(0:开启,1:禁用) */
    private Integer status;

}
