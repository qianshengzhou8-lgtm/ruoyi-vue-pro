package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import cn.iocoder.yudao.module.school.dal.mysql.StaffMapper;
import cn.iocoder.yudao.module.school.service.StudentService;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.STUDENT_NOT_EXISTS;

@Tag(name = "管理后台 - 学生管理")
@RestController
@RequestMapping("/school/student")
@Validated
public class StudentController {

    @Resource
    private StudentService studentService;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private AdminUserMapper adminUserMapper;

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
        StudentDO s = studentService.getStudent(id);
        if (s == null) { throw exception(STUDENT_NOT_EXISTS); }
        return success(BeanUtils.toBean(s, StudentRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得学生列表")
    @PreAuthorize("@ss.hasPermission('school:student:query')")
    public CommonResult<List<StudentRespVO>> getStudentList(@Valid StudentListReqVO reqVO) {
        // 根据登录教职工角色做数据权限过滤
        // getLoginUserId() 返回 system_users 的 id，需通过 username 关联查到 school_staff
        AdminUserDO adminUser = adminUserMapper.selectById(getLoginUserId());
        StaffDO staff = adminUser != null ? staffMapper.selectByUsername(adminUser.getUsername()) : null;
        int role = staff != null && staff.getRole() != null ? staff.getRole() : 0;
        List<StudentDO> list;
        switch (role) {
            case 3: // 校长 → 全校数据
                list = studentService.getStudentList(reqVO);
                break;
            case 2: // 学院院长 → 本学院数据
                list = studentService.getStudentListByCollegeId(reqVO, staff.getCollegeId());
                break;
            case 1: // 班主任 → 本班数据
                reqVO.setClassId(staff.getClassId());
                list = studentService.getStudentList(reqVO);
                break;
            default: // 教师 → 无限制（或后续收紧）
                list = studentService.getStudentList(reqVO);
                break;
        }
        return success(BeanUtils.toBean(list, StudentRespVO.class));
    }

}
