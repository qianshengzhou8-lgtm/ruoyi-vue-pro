package cn.iocoder.yudao.module.school.controller.admin.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Schema(description = "学校管理后台 - 登录 Request VO")
@Data
public class SchoolLoginReqVO {

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "zhangsan")
    @NotBlank(message = "登录账号不能为空")
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;

}
