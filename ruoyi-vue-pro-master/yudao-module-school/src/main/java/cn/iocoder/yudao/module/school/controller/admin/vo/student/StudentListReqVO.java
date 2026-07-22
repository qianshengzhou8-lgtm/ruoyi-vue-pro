package cn.iocoder.yudao.module.school.controller.admin.vo.student;

import cn.iocoder.yudao.module.school.controller.admin.vo.common.PageReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 学生列表 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentListReqVO extends PageReqVO {

    @Schema(description = "姓名，模糊匹配", example = "李")
    private String name;

    @Schema(description = "所属班级ID", example = "1")
    private Long classId;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
