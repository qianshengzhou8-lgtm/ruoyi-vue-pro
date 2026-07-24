package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.ClassDO;
import cn.iocoder.yudao.module.school.service.ClassService;
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
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.CLASS_NOT_EXISTS;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.DATA_PERMISSION_DENIED;

@Tag(name = "管理后台 - 班级管理")
@RestController
@RequestMapping("/school/class")
@Validated
public class ClassController {

    @Resource
    private ClassService classService;
    @Resource
    private SchoolDataPermissionUtil dataPermissionUtil;

    @PostMapping("/create")
    @Operation(summary = "创建班级")
    @PreAuthorize("@ss.hasPermission('school:class:create')")
    public CommonResult<Long> createClass(@Valid @RequestBody ClassSaveReqVO createReqVO) {
        return success(classService.createClass(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新班级")
    @PreAuthorize("@ss.hasPermission('school:class:update')")
    public CommonResult<Boolean> updateClass(@Valid @RequestBody ClassSaveReqVO updateReqVO) {
        classService.updateClass(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除班级")
    @PreAuthorize("@ss.hasPermission('school:class:delete')")
    public CommonResult<Boolean> deleteClass(@RequestParam("id") Long id) {
        classService.deleteClass(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得班级信息")
    @PreAuthorize("@ss.hasPermission('school:class:query')")
    public CommonResult<ClassRespVO> getClass(@RequestParam("id") Long id) {
        ClassDO c = classService.getClass(id);
        if (c == null) { throw exception(CLASS_NOT_EXISTS); }
        Integer role = dataPermissionUtil.getCurrentStaffRole();
        if (role == 0) throw exception(DATA_PERMISSION_DENIED);
        if (!dataPermissionUtil.hasAccessToClass(c.getId())) throw exception(DATA_PERMISSION_DENIED);
        return success(BeanUtils.toBean(c, ClassRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得班级列表")
    @PreAuthorize("@ss.hasPermission('school:class:query')")
    public CommonResult<List<ClassRespVO>> getClassList(@Valid ClassListReqVO reqVO) {
        Integer role = dataPermissionUtil.getCurrentStaffRole();
        if (role == 0) return success(Collections.emptyList());
        List<ClassDO> list;
        if (role == 1) {
            // 班主任 → 只看本班（下推到数据库过滤）
            Long classId = dataPermissionUtil.getCurrentStaffClassId();
            if (classId != null) {
                reqVO.setMajorId(null); // 清除可能存在的 majorId 筛选，以 class 级别为准
                list = java.util.Collections.singletonList(classService.getClass(classId));
                list.removeIf(c -> c == null);
            } else {
                list = Collections.emptyList();
            }
        } else if (role == 2) {
            // 院长 → 本学院班级（下推到数据库过滤）
            Long collegeId = dataPermissionUtil.getCurrentStaffCollegeId();
            list = classService.getClassListByCollegeId(reqVO, collegeId);
        } else {
            // 校长 → 全校数据
            list = classService.getClassList(reqVO);
        }
        return success(BeanUtils.toBean(list, ClassRespVO.class));
    }

    @GetMapping("/list-by-major")
    @Operation(summary = "根据专业ID获取班级列表", description = "用于级联选择班级")
    @PreAuthorize("@ss.hasPermission('school:class:query')")
    public CommonResult<List<ClassRespVO>> getClassListByMajorId(@RequestParam("majorId") Long majorId) {
        List<ClassDO> list = classService.getClassListByMajorId(majorId);
        return success(BeanUtils.toBean(list, ClassRespVO.class));
    }

    // ========== 导入导出 ==========

    @GetMapping("/export-excel")
    @Operation(summary = "导出班级 Excel")
    @PreAuthorize("@ss.hasPermission('school:class:export')")
    public void exportClassList(ClassListReqVO reqVO, HttpServletResponse response) throws IOException {
        List<ClassDO> list = classService.getClassList(reqVO);
        ExcelUtils.write(response, "班级数据.xls", "数据", ClassRespVO.class, BeanUtils.toBean(list, ClassRespVO.class));
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得班级导入模板")
    @PermitAll
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtils.write(response, "班级导入模板.xls", "数据", ClassImportExcelVO.class,
                Collections.singletonList(ClassImportExcelVO.builder().build()));
    }

    @PostMapping("/import")
    @Operation(summary = "导入班级")
    @PreAuthorize("@ss.hasPermission('school:class:import')")
    public CommonResult<ImportRespVO> importClassList(@RequestParam("file") MultipartFile file) throws IOException {
        List<ClassImportExcelVO> list = ExcelUtils.read(file, ClassImportExcelVO.class);
        return success(classService.importClassList(list));
    }

}
