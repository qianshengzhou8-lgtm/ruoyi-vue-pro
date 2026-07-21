package cn.iocoder.yudao.module.school.controller.admin.vo.student;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentImportExcelVO {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("登录账号")
    private String username;

    @ExcelProperty("登录密码")
    private String password;

    @ExcelProperty("手机号")
    private String mobile;

    @ExcelProperty("所属班级ID")
    private Long classId;

    @ExcelProperty("状态(0:开启,1:禁用)")
    private Integer status;

}
