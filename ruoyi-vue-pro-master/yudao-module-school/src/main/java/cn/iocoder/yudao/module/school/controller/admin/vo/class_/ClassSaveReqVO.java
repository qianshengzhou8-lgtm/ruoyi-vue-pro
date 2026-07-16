package cn.iocoder.yudao.module.school.controller.admin.vo.class_;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 班级创建/修改 Request VO")
@Data
public class ClassSaveReqVO {

    @Schema(description = "班级编号", example = "1")
    private Long id;

    @Schema(description = "班级名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "软工2026-1班")
    @NotBlank(message = "班级名称不能为空")
    private String name;

    @Schema(description = "所属专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "所属专业不能为空")
    private Long majorId;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
