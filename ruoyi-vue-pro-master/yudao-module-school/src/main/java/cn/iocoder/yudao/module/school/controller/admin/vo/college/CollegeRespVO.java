package cn.iocoder.yudao.module.school.controller.admin.vo.college;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 学院信息 Response VO")
@Data
public class CollegeRespVO {

    @Schema(description = "学院编号", example = "1")
    private Long id;

    @Schema(description = "学院名称", example = "计算机学院")
    private String name;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
