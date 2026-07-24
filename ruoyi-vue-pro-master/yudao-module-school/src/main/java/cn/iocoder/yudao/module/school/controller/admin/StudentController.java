package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import cn.iocoder.yudao.module.school.service.StudentService;
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
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.*;

@Tag(name = "管理后台 - 学生管理")
@RestController
@RequestMapping("/school/student")
@Validated
public class StudentController {

    @Resource
    private StudentService studentService;
    @Resource
    private SchoolDataPermissionUtil dataPermissionUtil;

    @PostMapping("/create")
    @Operation(summary = "创建学生")
    @PreAuthorize("@ss.hasPermission('school:student:create')")
    public CommonResult<Long> createStudent(@Valid @RequestBody StudentSaveReqVO createReqVO) {
        return success(studentService.createStudent(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新学生")
    @PreAuthorize("@ss.hasPermission('school:student:update')")
    public CommonResult<Boolean> updateStudent(@Valid @RequestBody StudentSaveReqVO updateReqVO) {
        studentService.updateStudent(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除学生")
    @PreAuthorize("@ss.hasPermission('school:student:delete')")
    public CommonResult<Boolean> deleteStudent(@RequestParam("id") Long id) {
        studentService.deleteStudent(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得学生信息")
    @PreAuthorize("@ss.hasPermission('school:student:query')")
    public CommonResult<StudentRespVO> getStudent(@RequestParam("id") Long id) {
        // 先做数据权限检查，避免通过错误信息差异枚举用户是否存在
        Integer role = dataPermissionUtil.getCurrentStaffRole();
        if (role == 0) {
            throw exception(DATA_PERMISSION_DENIED);
        }
        if (role == 1) {
            Long currentClassId = dataPermissionUtil.getCurrentStaffClassId();
            if (currentClassId == null) {
                throw exception(DATA_PERMISSION_DENIED);
            }
            StudentDO s = studentService.getStudent(id);
            if (s == null || !currentClassId.equals(s.getClassId())) {
                throw exception(DATA_PERMISSION_DENIED);
            }
            return success(BeanUtils.toBean(s, StudentRespVO.class));
        }
        if (role == 2) {
            Long currentCollegeId = dataPermissionUtil.getCurrentStaffCollegeId();
            if (currentCollegeId == null) {
                throw exception(DATA_PERMISSION_DENIED);
            }
            StudentDO s = studentService.getStudent(id);
            if (s == null || !studentService.hasStudentInCollege(s.getId(), currentCollegeId)) {
                throw exception(DATA_PERMISSION_DENIED);
            }
            return success(BeanUtils.toBean(s, StudentRespVO.class));
        }
        // role == 3: 校长可查看所有学生
        StudentDO s = studentService.getStudent(id);
        if (s == null) { throw exception(STUDENT_NOT_EXISTS); }
        return success(BeanUtils.toBean(s, StudentRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得学生列表")
    @PreAuthorize("@ss.hasPermission('school:student:query')")
    public CommonResult<List<StudentRespVO>> getStudentList(@Valid StudentListReqVO reqVO) {
        // 根据登录教职工角色做数据权限过滤
        Integer role = dataPermissionUtil.getCurrentStaffRole();
        List<StudentDO> list;
        switch (role) {
            case 3: // 校长 → 全校数据
                list = studentService.getStudentList(reqVO);
                break;
            case 2: // 学院院长 → 本学院数据
                list = studentService.getStudentListByCollegeId(reqVO, dataPermissionUtil.getCurrentStaffCollegeId());
                break;
            case 1: // 班主任 → 只看本班学生
                Long classId = dataPermissionUtil.getCurrentStaffClassId();
                if (classId != null) {
                    reqVO.setClassId(classId);
                    list = studentService.getStudentList(reqVO);
                } else {
                    list = Collections.emptyList(); // 未分配班级则无数据
                }
                break;
            case 0: // 教师 → 无数据权限
            default:
                list = Collections.emptyList();
                break;
        }
        return success(BeanUtils.toBean(list, StudentRespVO.class));
    }

    // ========== 导入导出 ==========

    @GetMapping("/export-excel")
    @Operation(summary = "导出学生 Excel")
    @PreAuthorize("@ss.hasPermission('school:student:export')")
    public void exportStudentList(StudentListReqVO reqVO, HttpServletResponse response) throws IOException {
        List<StudentDO> list = studentService.getStudentList(reqVO);
        ExcelUtils.write(response, "学生数据.xls", "数据", StudentRespVO.class, BeanUtils.toBean(list, StudentRespVO.class));
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得学生导入模板")
    @PermitAll
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtils.write(response, "学生导入模板.xls", "数据", StudentImportExcelVO.class,
                Collections.singletonList(StudentImportExcelVO.builder().build()));
    }

    @PostMapping("/import")
    @Operation(summary = "导入学生")
    @PreAuthorize("@ss.hasPermission('school:student:import')")
    public CommonResult<ImportRespVO> importStudentList(@RequestParam("file") MultipartFile file) throws IOException {
        List<StudentImportExcelVO> list = ExcelUtils.read(file, StudentImportExcelVO.class);
        return success(studentService.importStudentList(list));
    }

}
