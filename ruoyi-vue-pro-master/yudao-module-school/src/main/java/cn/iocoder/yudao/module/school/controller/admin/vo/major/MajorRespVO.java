package cn.iocoder.yudao.module.school.controller.admin.vo.major;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 专业信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MajorRespVO {

    @Schema(description = "专业编号", example = "1")
    @ExcelProperty("专业编号")
    private Long id;

    @Schema(description = "专业名称", example = "软件工程")
    @ExcelProperty("专业名称")
    private String name;

    @Schema(description = "所属学院ID", example = "1")
    @ExcelProperty("所属学院ID")
    private Long collegeId;

    @Schema(description = "排序", example = "1")
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "状态(0:开启,1:禁用)", example = "0")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
