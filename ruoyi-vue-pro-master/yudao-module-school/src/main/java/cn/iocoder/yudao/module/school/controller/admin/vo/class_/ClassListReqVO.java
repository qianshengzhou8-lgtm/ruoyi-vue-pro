package cn.iocoder.yudao.module.school.controller.admin.vo.class_;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 班级列表 Request VO")
@Data
public class ClassListReqVO {

    @Schema(description = "班级名称，模糊匹配", example = "软工")
    private String name;

    @Schema(description = "所属专业ID", example = "1")
    private Long majorId;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
