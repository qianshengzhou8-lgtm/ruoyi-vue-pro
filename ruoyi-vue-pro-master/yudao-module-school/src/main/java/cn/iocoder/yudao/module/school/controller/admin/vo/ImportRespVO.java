package cn.iocoder.yudao.module.school.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "导入结果 Response VO")
@Data
public class ImportRespVO {

    @Schema(description = "创建成功数量", example = "10")
    private int createCount;

    @Schema(description = "更新成功数量", example = "5")
    private int updateCount;

    @Schema(description = "失败数量", example = "2")
    private int failureCount;

    @Schema(description = "失败原因列表")
    private List<String> failureReasons = new ArrayList<>();

}
