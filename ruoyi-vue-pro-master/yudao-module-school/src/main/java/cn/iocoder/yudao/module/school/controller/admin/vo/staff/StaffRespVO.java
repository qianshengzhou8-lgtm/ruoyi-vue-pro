package cn.iocoder.yudao.module.school.controller.admin.vo.staff;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 教职工信息 Response VO")
@Data
public class StaffRespVO {

    @Schema(description = "教职工编号", example = "1")
    private Long id;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "登录账号", example = "zhangsan")
    private String username;

    @Schema(description = "手机号", example = "13800138000")
    private String mobile;

    @Schema(description = "所属学院ID", example = "1")
    private Long collegeId;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
