package cn.iocoder.yudao.module.school.controller.admin.vo.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 学生创建/修改 Request VO")
@Data
public class StudentSaveReqVO {

    @Schema(description = "学生编号", example = "1")
    private Long id;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @Schema(description = "登录账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "lisi")
    @NotBlank(message = "登录账号不能为空")
    private String username;

    @Schema(description = "登录密码(创建时必填，修改时留空则不变)", example = "123456")
    private String password;

    @Schema(description = "手机号", example = "13800138001")
    private String mobile;

    @Schema(description = "所属班级ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "所属班级不能为空")
    private Long classId;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    private Integer status;

}
