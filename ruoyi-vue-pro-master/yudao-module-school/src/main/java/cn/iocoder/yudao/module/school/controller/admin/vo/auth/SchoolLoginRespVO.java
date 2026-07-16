package cn.iocoder.yudao.module.school.controller.admin.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "学校管理后台 - 登录 Response VO")
@Data
public class SchoolLoginRespVO {

    @Schema(description = "访问令牌", example = "abc123")
    private String accessToken;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户类型：staff-教职工, student-学生", example = "staff")
    private String userType;

    @Schema(description = "用户姓名", example = "张三")
    private String name;

}
