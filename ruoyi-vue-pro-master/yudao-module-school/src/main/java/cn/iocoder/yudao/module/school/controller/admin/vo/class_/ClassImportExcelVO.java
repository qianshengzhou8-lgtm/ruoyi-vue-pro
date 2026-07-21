package cn.iocoder.yudao.module.school.controller.admin.vo.class_;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassImportExcelVO {

    @ExcelProperty("班级名称")
    private String name;

    @ExcelProperty("所属专业ID")
    private Long majorId;

    @ExcelProperty("排序")
    private Integer sort;

    @ExcelProperty("状态(0:开启,1:禁用)")
    private Integer status;

}
