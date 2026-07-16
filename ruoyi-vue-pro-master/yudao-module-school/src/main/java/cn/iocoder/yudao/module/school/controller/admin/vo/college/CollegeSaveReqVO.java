package cn.iocoder.yudao.module.school.controller.admin.vo.college;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Schema(description = "管理后台 - 学院创建/修改 Request VO")
@Data
public class CollegeSaveReqVO {

    @Schema(description = "学院编号", example = "1")
    private Long id;

    @Schema(description = "学院名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "计算机学院")
    @NotBlank(message = "学院名称不能为空")
    private String name;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
