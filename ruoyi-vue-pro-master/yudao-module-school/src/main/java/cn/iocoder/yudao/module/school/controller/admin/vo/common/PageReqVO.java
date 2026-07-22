package cn.iocoder.yudao.module.school.controller.admin.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;

@Schema(description = "分页 Request VO")
@Data
public class PageReqVO {

    @Schema(description = "页码，从 1 开始", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Min(value = 1, message = "页码必须大于 0")
    private Integer pageNo = 1;

    @Schema(description = "每页条数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @Min(value = 1, message = "每页条数必须大于 0")
    private Integer pageSize = 10;

}