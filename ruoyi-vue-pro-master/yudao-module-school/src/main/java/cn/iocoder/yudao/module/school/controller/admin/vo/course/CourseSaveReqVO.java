package cn.iocoder.yudao.module.school.controller.admin.vo.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 课程创建/修改 Request VO")
@Data
public class CourseSaveReqVO {

    @Schema(description = "课程编号", example = "1")
    private Long id;

    @Schema(description = "课程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "高等数学")
    @NotBlank(message = "课程名称不能为空")
    private String name;

    @Schema(description = "类型(0:必修课,1:选修课)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "课程类型不能为空")
    private Integer type;

    @Schema(description = "选修课人数上限(null=不限)", example = "50")
    private Integer capacity;

    @Schema(description = "授课教师ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "授课教师不能为空")
    private Long teacherId;

    @Schema(description = "所属学院ID", example = "1")
    private Long collegeId;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
