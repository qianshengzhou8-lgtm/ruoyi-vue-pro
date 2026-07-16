package cn.iocoder.yudao.module.school.controller.admin.vo.college;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 学院列表 Request VO")
@Data
public class CollegeListReqVO {

    @Schema(description = "学院名称，模糊匹配", example = "计算机")
    private String name;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
