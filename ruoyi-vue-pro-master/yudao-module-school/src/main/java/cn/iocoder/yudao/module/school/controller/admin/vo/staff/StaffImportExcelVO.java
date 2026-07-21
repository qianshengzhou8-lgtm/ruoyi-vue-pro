package cn.iocoder.yudao.module.school.controller.admin.vo.staff;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffImportExcelVO {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("登录账号")
    private String username;

    @ExcelProperty("登录密码")
    private String password;

    @ExcelProperty("手机号")
    private String mobile;

    @ExcelProperty("所属学院ID")
    private Long collegeId;

    @ExcelProperty("角色(0:教师,1:班主任,2:学院院长,3:校长)")
    private Integer role;

    @ExcelProperty("负责班级ID")
    private Long classId;

    @ExcelProperty("状态(0:开启,1:禁用)")
    private Integer status;

}
