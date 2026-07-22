package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.CollegeDO;
import cn.iocoder.yudao.module.school.service.CollegeService;
import cn.iocoder.yudao.module.school.util.SchoolDataPermissionUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
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
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.COLLEGE_NOT_EXISTS;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.DATA_PERMISSION_DENIED;

@Tag(name = "管理后台 - 学院管理")
@RestController
@RequestMapping("/school/college")
@Validated
public class CollegeController {

    @Resource
    private CollegeService collegeService;
    @Resource
    private SchoolDataPermissionUtil dataPermissionUtil;

    @PostMapping("/create")
    @Operation(summary = "创建学院")
    @PreAuthorize("@ss.hasPermission('school:college:create')")
    public CommonResult<Long> createCollege(@Valid @RequestBody CollegeSaveReqVO createReqVO) {
        return success(collegeService.createCollege(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新学院")
    @PreAuthorize("@ss.hasPermission('school:college:update')")
    public CommonResult<Boolean> updateCollege(@Valid @RequestBody CollegeSaveReqVO updateReqVO) {
        collegeService.updateCollege(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除学院")
    @PreAuthorize("@ss.hasPermission('school:college:delete')")
    public CommonResult<Boolean> deleteCollege(@RequestParam("id") Long id) {
        collegeService.deleteCollege(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得学院信息")
    @PreAuthorize("@ss.hasPermission('school:college:query')")
    public CommonResult<CollegeRespVO> getCollege(@RequestParam("id") Long id) {
        CollegeDO college = collegeService.getCollege(id);
        if (college == null) { throw exception(COLLEGE_NOT_EXISTS); }
        // 数据权限检查：教师无法查看详情
        Integer role = dataPermissionUtil.getCurrentStaffRole();
        if (role == 0) throw exception(DATA_PERMISSION_DENIED);
        if (role == 2 && !dataPermissionUtil.hasAccessToCollege(college.getId())) {
            throw exception(DATA_PERMISSION_DENIED);
        }
        return success(BeanUtils.toBean(college, CollegeRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得学院列表")
    @PreAuthorize("@ss.hasPermission('school:college:query')")
    public CommonResult<List<CollegeRespVO>> getCollegeList(@Valid CollegeListReqVO reqVO) {
        // 根据登录教职工角色做数据权限过滤
        Integer role = dataPermissionUtil.getCurrentStaffRole();
        if (role == 0 || role == 1) return success(Collections.emptyList()); // 教师/班主任无学院权限
        List<CollegeDO> list = collegeService.getCollegeList(reqVO);
        if (role == 2) {
            // 院长 → 只看本院
            Long collegeId = dataPermissionUtil.getCurrentStaffCollegeId();
            list.removeIf(c -> !c.getId().equals(collegeId));
        }
        // role==3 校长看全部
        return success(BeanUtils.toBean(list, CollegeRespVO.class));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取学院精简列表", description = "只包含开启的学院，用于下拉框")
    public CommonResult<List<CollegeRespVO>> getSimpleCollegeList() {
        List<CollegeDO> list = collegeService.getCollegeListByStatus(CommonStatusEnum.ENABLE.getStatus());
        return success(BeanUtils.toBean(list, CollegeRespVO.class));
    }

    // ========== 导入导出 ==========

    @GetMapping("/export-excel")
    @Operation(summary = "导出学院 Excel")
    @PreAuthorize("@ss.hasPermission('school:college:export')")
    public void exportCollegeList(@Valid CollegeListReqVO reqVO, HttpServletResponse response) throws IOException {
        List<CollegeDO> list = collegeService.getCollegeList(reqVO);
        ExcelUtils.write(response, "学院数据.xls", "数据", CollegeRespVO.class, BeanUtils.toBean(list, CollegeRespVO.class));
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得学院导入模板")
    @PermitAll
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtils.write(response, "学院导入模板.xls", "数据", CollegeImportExcelVO.class,
                Collections.singletonList(CollegeImportExcelVO.builder().build()));
    }

    @PostMapping("/import")
    @Operation(summary = "导入学院")
    @PreAuthorize("@ss.hasPermission('school:college:import')")
    public CommonResult<ImportRespVO> importCollegeList(@RequestParam("file") MultipartFile file) throws IOException {
        List<CollegeImportExcelVO> list = ExcelUtils.read(file, CollegeImportExcelVO.class);
        ImportRespVO result = collegeService.importCollegeList(list);
        return success(result);
    }

}
