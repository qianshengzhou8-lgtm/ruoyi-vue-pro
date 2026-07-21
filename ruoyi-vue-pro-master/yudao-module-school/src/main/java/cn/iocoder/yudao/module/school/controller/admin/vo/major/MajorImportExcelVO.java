package cn.iocoder.yudao.module.school.controller.admin.vo.major;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorImportExcelVO {

    @ExcelProperty("专业名称")
    private String name;

    @ExcelProperty("所属学院ID")
    private Long collegeId;

    @ExcelProperty("排序")
    private Integer sort;

    @ExcelProperty("状态(0:开启,1:禁用)")
    private Integer status;

}
