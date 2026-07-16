package cn.iocoder.yudao.module.school.controller.admin.vo.staff;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 教职工列表 Request VO")
@Data
public class StaffListReqVO {

    @Schema(description = "姓名，模糊匹配", example = "张")
    private String name;

    @Schema(description = "所属学院ID", example = "1")
    private Long collegeId;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
