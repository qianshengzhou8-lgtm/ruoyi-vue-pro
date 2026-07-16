package cn.iocoder.yudao.module.school.controller.admin.vo.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 专业列表 Request VO")
@Data
public class MajorListReqVO {

    @Schema(description = "专业名称，模糊匹配", example = "软件")
    private String name;

    @Schema(description = "所属学院ID", example = "1")
    private Long collegeId;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
