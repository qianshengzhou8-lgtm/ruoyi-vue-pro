package cn.iocoder.yudao.module.school.controller.admin.vo.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 专业创建/修改 Request VO")
@Data
public class MajorSaveReqVO {

    @Schema(description = "专业编号", example = "1")
    private Long id;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "软件工程")
    @NotBlank(message = "专业名称不能为空")
    private String name;

    @Schema(description = "所属学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "所属学院不能为空")
    private Long collegeId;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
