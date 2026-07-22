package cn.iocoder.yudao.module.school.controller.admin.vo.common;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "分页 Result VO")
@Data
public class PageResultVO<T> {

    @Schema(description = "总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long total;

    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer pageNo;

    @Schema(description = "每页条数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer pageSize;

    @Schema(description = "数据列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<T> list;

    public static <T> PageResultVO<T> of(PageResult<T> pageResult) {
        PageResultVO<T> resultVO = new PageResultVO<>();
        resultVO.setTotal(pageResult.getTotal());
        resultVO.setPageNo(1); // Default to page 1 since framework PageResult doesn't store page info
        resultVO.setPageSize(10); // Default page size
        resultVO.setList(pageResult.getList());
        return resultVO;
    }

}