package cn.iocoder.yudao.module.school.controller.admin.vo.staff;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 教职工信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StaffRespVO {

    @Schema(description = "教职工编号", example = "1")
    @ExcelProperty("教职工编号")
    private Long id;

    @Schema(description = "姓名", example = "张三")
    @ExcelProperty("姓名")
    private String name;

    @Schema(description = "登录账号", example = "zhangsan")
    @ExcelProperty("登录账号")
    private String username;

    @Schema(description = "手机号", example = "13800138000")
    @ExcelProperty("手机号")
    private String mobile;

    @Schema(description = "所属学院ID", example = "1")
    @ExcelProperty("所属学院ID")
    private Long collegeId;

    @Schema(description = "角色(0:教师,1:班主任,2:学院院长,3:校长)", example = "0")
    @ExcelProperty("角色")
    private Integer role;

    @Schema(description = "负责班级ID", example = "1")
    @ExcelProperty("负责班级ID")
    private Long classId;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
