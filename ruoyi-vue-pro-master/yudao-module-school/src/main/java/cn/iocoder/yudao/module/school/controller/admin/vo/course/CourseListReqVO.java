package cn.iocoder.yudao.module.school.controller.admin.vo.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 课程列表 Request VO")
@Data
public class CourseListReqVO {

    @Schema(description = "课程名称，模糊匹配", example = "数学")
    private String name;

    @Schema(description = "类型(0:必修课,1:选修课)", example = "0")
    private Integer type;

    @Schema(description = "授课教师ID", example = "1")
    private Long teacherId;

    @Schema(description = "所属学院ID", example = "1")
    private Long collegeId;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
