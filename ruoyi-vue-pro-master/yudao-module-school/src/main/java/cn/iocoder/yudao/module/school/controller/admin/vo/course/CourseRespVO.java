package cn.iocoder.yudao.module.school.controller.admin.vo.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 课程信息 Response VO")
@Data
public class CourseRespVO {

    @Schema(description = "课程编号", example = "1")
    private Long id;

    @Schema(description = "课程名称", example = "高等数学")
    private String name;

    @Schema(description = "类型(0:必修课,1:选修课)", example = "0")
    private Integer type;

    @Schema(description = "选修课人数上限", example = "50")
    private Integer capacity;

    @Schema(description = "授课教师ID", example = "1")
    private Long teacherId;

    @Schema(description = "所属学院ID", example = "1")
    private Long collegeId;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

    @Schema(description = "已选人数", example = "30")
    private Long selectedCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
