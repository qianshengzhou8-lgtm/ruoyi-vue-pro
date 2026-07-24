package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StaffDO;
import cn.iocoder.yudao.module.school.service.StaffService;
import cn.iocoder.yudao.module.school.util.SchoolDataPermissionUtil;
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
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.DATA_PERMISSION_DENIED;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.STAFF_NOT_EXISTS;

@Tag(name = "管理后台 - 教职工管理")
@RestController
@RequestMapping("/school/staff")
@Validated
public class StaffController {

    @Resource
    private StaffService staffService;
    @Resource
    private SchoolDataPermissionUtil dataPermissionUtil;

    @PostMapping("/create")
    @Operation(summary = "创建教职工")
    @PreAuthorize("@ss.hasPermission('school:staff:create')")
    public CommonResult<Long> createStaff(@Valid @RequestBody StaffSaveReqVO createReqVO) {
        return success(staffService.createStaff(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新教职工")
    @PreAuthorize("@ss.hasPermission('school:staff:update')")
    public CommonResult<Boolean> updateStaff(@Valid @RequestBody StaffSaveReqVO updateReqVO) {
        staffService.updateStaff(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除教职工")
    @PreAuthorize("@ss.hasPermission('school:staff:delete')")
    public CommonResult<Boolean> deleteStaff(@RequestParam("id") Long id) {
        staffService.deleteStaff(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得教职工信息")
    @PreAuthorize("@ss.hasPermission('school:staff:query')")
    public CommonResult<StaffRespVO> getStaff(@RequestParam("id") Long id) {
        StaffDO s = staffService.getStaff(id);
        if (s == null) { throw exception(STAFF_NOT_EXISTS); }
        Integer role = dataPermissionUtil.getCurrentStaffRole();
        if (role == 0) throw exception(DATA_PERMISSION_DENIED);
        if (role == 2 && !dataPermissionUtil.hasAccessToCollege(s.getCollegeId())) {
            throw exception(DATA_PERMISSION_DENIED);
        }
        return success(BeanUtils.toBean(s, StaffRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得教职工列表")
    @PreAuthorize("@ss.hasPermission('school:staff:query')")
    public CommonResult<List<StaffRespVO>> getStaffList(@Valid StaffListReqVO reqVO) {
        Integer role = dataPermissionUtil.getCurrentStaffRole();
        if (role == 0 || role == 1) return success(Collections.emptyList());
        List<StaffDO> list;
        if (role == 2) {
            // 院长 → 只看本院教职工（下推到数据库过滤）
            Long collegeId = dataPermissionUtil.getCurrentStaffCollegeId();
            list = staffService.getStaffListByCollegeId(reqVO, collegeId);
        } else {
            // 校长 → 全校数据
            list = staffService.getStaffList(reqVO);
        }
        return success(BeanUtils.toBean(list, StaffRespVO.class));
    }

    // ========== 导入导出 ==========

    @GetMapping("/export-excel")
    @Operation(summary = "导出教职工 Excel")
    @PreAuthorize("@ss.hasPermission('school:staff:export')")
    public void exportStaffList(StaffListReqVO reqVO, HttpServletResponse response) throws IOException {
        List<StaffDO> list = staffService.getStaffList(reqVO);
        ExcelUtils.write(response, "教职工数据.xls", "数据", StaffRespVO.class, BeanUtils.toBean(list, StaffRespVO.class));
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得教职工导入模板")
    @PermitAll
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtils.write(response, "教职工导入模板.xls", "数据", StaffImportExcelVO.class,
                Collections.singletonList(StaffImportExcelVO.builder().build()));
    }

    @PostMapping("/import")
    @Operation(summary = "导入教职工")
    @PreAuthorize("@ss.hasPermission('school:staff:import')")
    public CommonResult<ImportRespVO> importStaffList(@RequestParam("file") MultipartFile file) throws IOException {
        List<StaffImportExcelVO> list = ExcelUtils.read(file, StaffImportExcelVO.class);
        return success(staffService.importStaffList(list));
    }

}
