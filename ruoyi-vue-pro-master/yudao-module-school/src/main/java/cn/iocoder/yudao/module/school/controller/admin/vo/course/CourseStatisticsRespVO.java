package cn.iocoder.yudao.module.school.controller.admin.vo.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "课程统计 Response VO")
@Data
public class CourseStatisticsRespVO {

    @Schema(description = "课程总数", example = "100")
    private Long totalCourses;

    @Schema(description = "必修课数量", example = "60")
    private Long requiredCount;

    @Schema(description = "选修课数量", example = "40")
    private Long electiveCount;

    @Schema(description = "选课人次", example = "500")
    private Long totalSelections;

    @Schema(description = "选课人数(去重)", example = "300")
    private Long totalStudents;

}
