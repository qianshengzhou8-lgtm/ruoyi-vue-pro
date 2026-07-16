package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import cn.iocoder.yudao.module.school.service.StudentService;
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
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.STUDENT_NOT_EXISTS;

@Tag(name = "管理后台 - 学生管理")
@RestController
@RequestMapping("/school/student")
@Validated
public class StudentController {

    @Resource
    private StudentService studentService;

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
        List<StudentDO> list = studentService.getStudentList(reqVO);
        return success(BeanUtils.toBean(list, StudentRespVO.class));
    }

}
