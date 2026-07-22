package cn.iocoder.yudao.module.school.controller.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.school.controller.admin.vo.course.CourseRespVO;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseDO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import cn.iocoder.yudao.module.school.service.CourseService;
import cn.iocoder.yudao.module.school.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.STUDENT_NOT_EXISTS;

@Tag(name = "用户 APP - 课程选课")
@RestController
@RequestMapping("/app/school/course")
@Validated
public class AppCourseController {

    @Resource
    private CourseService courseService;
    @Resource
    private MemberUserService memberUserService;
    @Resource
    private StudentService studentService;

    /** 解析当前登录的 member 用户对应的 StudentDO.id，确保学生存在 */
    private Long getCurrentStudentId() {
        Long userId = getLoginUserId();
        if (userId == null) {
            throw exception(STUDENT_NOT_EXISTS);
        }
        MemberUserDO memberUser = memberUserService.getUser(userId);
        if (memberUser == null) {
            throw exception(STUDENT_NOT_EXISTS);
        }
        StudentDO student = studentService.getStudentByUsername(memberUser.getMobile());
        if (student == null) {
            throw exception(STUDENT_NOT_EXISTS);
        }
        return student.getId();
    }

    @GetMapping("/list")
    @Operation(summary = "浏览可选课程列表（含已选人数）")
    @PreAuthorize("@ss.hasScope('member')") // 确保通过会员认证
    public CommonResult<List<CourseRespVO>> getCourseList() {
        List<CourseDO> list = courseService.getCourseList(null);
        List<CourseRespVO> result = BeanUtils.toBean(list, CourseRespVO.class);
        // 批量查询选课人数，单次查询避免 N+1 问题
        List<Long> courseIds = list.stream().map(CourseDO::getId).collect(Collectors.toList());
        Map<Long, Long> countMap = courseService.getSelectedCountMapByCourseIds(courseIds);
        for (CourseRespVO vo : result) {
            vo.setSelectedCount(countMap.getOrDefault(vo.getId(), 0L));
        }
        return success(result);
    }

    @PostMapping("/{courseId}/select")
    @Operation(summary = "选课")
    @PreAuthorize("@ss.hasScope('member')") // 确保通过会员认证
    public CommonResult<String> selectCourse(@PathVariable("courseId") @NotNull Long courseId) {
        Long studentId = getCurrentStudentId();
        return success(courseService.selectCourse(courseId, studentId));
    }

    @PostMapping("/{courseId}/deselect")
    @Operation(summary = "退课")
    @PreAuthorize("@ss.hasScope('member')") // 确保通过会员认证
    public CommonResult<Boolean> deselectCourse(@PathVariable("courseId") @NotNull Long courseId) {
        Long studentId = getCurrentStudentId();
        courseService.deselectCourse(courseId, studentId);
        return success(true);
    }

}
