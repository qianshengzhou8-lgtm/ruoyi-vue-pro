package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.MajorDO;
import cn.iocoder.yudao.module.school.service.MajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.MAJOR_NOT_EXISTS;

@Tag(name = "管理后台 - 专业管理")
@RestController
@RequestMapping("/school/major")
@Validated
public class MajorController {

    @Resource
    private MajorService majorService;

    @PostMapping("/create")
    @Operation(summary = "创建专业")
    @PreAuthorize("@ss.hasPermission('school:major:create')")
    public CommonResult<Long> createMajor(@Valid @RequestBody MajorSaveReqVO createReqVO) {
        return success(majorService.createMajor(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新专业")
    @PreAuthorize("@ss.hasPermission('school:major:update')")
    public CommonResult<Boolean> updateMajor(@Valid @RequestBody MajorSaveReqVO updateReqVO) {
        majorService.updateMajor(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除专业")
    @PreAuthorize("@ss.hasPermission('school:major:delete')")
    public CommonResult<Boolean> deleteMajor(@RequestParam("id") Long id) {
        majorService.deleteMajor(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得专业信息")
    @PreAuthorize("@ss.hasPermission('school:major:query')")
    public CommonResult<MajorRespVO> getMajor(@RequestParam("id") Long id) {
        MajorDO major = majorService.getMajor(id);
        if (major == null) { throw exception(MAJOR_NOT_EXISTS); }
        return success(BeanUtils.toBean(major, MajorRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得专业列表")
    @PreAuthorize("@ss.hasPermission('school:major:query')")
    public CommonResult<List<MajorRespVO>> getMajorList(@Valid MajorListReqVO reqVO) {
        List<MajorDO> list = majorService.getMajorList(reqVO);
        return success(BeanUtils.toBean(list, MajorRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出专业 Excel")
    @PreAuthorize("@ss.hasPermission('school:major:export')")
    public void exportMajorList(@Valid MajorListReqVO reqVO, HttpServletResponse response) throws IOException {
        List<MajorDO> list = majorService.getMajorList(reqVO);
        ExcelUtils.write(response, "专业数据.xls", "数据", MajorRespVO.class, BeanUtils.toBean(list, MajorRespVO.class));
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得专业导入模板")
    @PermitAll
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtils.write(response, "专业导入模板.xls", "数据", MajorImportExcelVO.class,
                Collections.singletonList(MajorImportExcelVO.builder().build()));
    }

    @PostMapping("/import")
    @Operation(summary = "导入专业")
    @PreAuthorize("@ss.hasPermission('school:major:import')")
    public CommonResult<ImportRespVO> importMajorList(@RequestParam("file") MultipartFile file) throws IOException {
        List<MajorImportExcelVO> list = ExcelUtils.read(file, MajorImportExcelVO.class);
        return success(majorService.importMajorList(list));
    }

}
