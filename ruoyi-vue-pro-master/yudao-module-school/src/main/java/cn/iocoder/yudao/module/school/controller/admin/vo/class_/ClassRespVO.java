package cn.iocoder.yudao.module.school.controller.admin.vo.class_;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 班级信息 Response VO")
@Data
public class ClassRespVO {

    @Schema(description = "班级编号", example = "1")
    private Long id;

    @Schema(description = "班级名称", example = "软工2026-1班")
    private String name;

    @Schema(description = "所属专业ID", example = "1")
    private Long majorId;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
