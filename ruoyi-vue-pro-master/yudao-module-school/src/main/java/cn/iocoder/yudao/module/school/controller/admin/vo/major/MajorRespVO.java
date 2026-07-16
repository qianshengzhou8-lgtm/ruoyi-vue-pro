package cn.iocoder.yudao.module.school.controller.admin.vo.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 专业信息 Response VO")
@Data
public class MajorRespVO {

    @Schema(description = "专业编号", example = "1")
    private Long id;

    @Schema(description = "专业名称", example = "软件工程")
    private String name;

    @Schema(description = "所属学院ID", example = "1")
    private Long collegeId;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
