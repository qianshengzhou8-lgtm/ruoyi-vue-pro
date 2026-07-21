package cn.iocoder.yudao.module.school.controller.admin.vo.student;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 学生信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StudentRespVO {

    @Schema(description = "学生编号", example = "1")
    @ExcelProperty("学生编号")
    private Long id;

    @Schema(description = "姓名", example = "李四")
    @ExcelProperty("姓名")
    private String name;

    @Schema(description = "登录账号", example = "lisi")
    @ExcelProperty("登录账号")
    private String username;

    @Schema(description = "手机号", example = "13800138001")
    @ExcelProperty("手机号")
    private String mobile;

    @Schema(description = "所属班级ID", example = "1")
    @ExcelProperty("所属班级ID")
    private Long classId;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
