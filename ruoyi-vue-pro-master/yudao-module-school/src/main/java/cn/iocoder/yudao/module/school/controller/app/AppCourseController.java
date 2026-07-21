package cn.iocoder.yudao.module.school.controller.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.mysql.user.MemberUserMapper;
import cn.iocoder.yudao.module.school.controller.admin.vo.course.CourseRespVO;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseDO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import cn.iocoder.yudao.module.school.dal.mysql.CourseSelectionMapper;
import cn.iocoder.yudao.module.school.dal.mysql.StudentMapper;
import cn.iocoder.yudao.module.school.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 课程选课")
@RestController
@RequestMapping("/school/course")
@Validated
public class AppCourseController {

    @Resource
    private CourseService courseService;
    @Resource
    private CourseSelectionMapper selectionMapper;
    @Resource
    private MemberUserMapper memberUserMapper;
    @Resource
    private StudentMapper studentMapper;

    /** 解析当前登录的 member 用户对应的 StudentDO.id */
    private Long getCurrentStudentId() {
        MemberUserDO memberUser = memberUserMapper.selectById(getLoginUserId());
        if (memberUser == null) return null;
        StudentDO student = studentMapper.selectByMobile(memberUser.getMobile());
        return student != null ? student.getId() : null;
    }

    @GetMapping("/list")
    @Operation(summary = "浏览可选课程列表（含已选人数）")
    public CommonResult<List<CourseRespVO>> getCourseList() {
        List<CourseDO> list = courseService.getCourseList(null);
        List<CourseRespVO> result = BeanUtils.toBean(list, CourseRespVO.class);
        Long studentId = getCurrentStudentId();
        for (int i = 0; i < list.size(); i++) {
            result.get(i).setSelectedCount(selectionMapper.selectCountByCourseId(list.get(i).getId()));
        }
        return success(result);
    }

    @PostMapping("/{courseId}/select")
    @Operation(summary = "选课")
    public CommonResult<String> selectCourse(@PathVariable("courseId") Long courseId) {
        Long studentId = getCurrentStudentId();
        return success(courseService.selectCourse(courseId, studentId));
    }

    @PostMapping("/{courseId}/deselect")
    @Operation(summary = "退课")
    public CommonResult<Boolean> deselectCourse(@PathVariable("courseId") Long courseId) {
        Long studentId = getCurrentStudentId();
        courseService.deselectCourse(courseId, studentId);
        return success(true);
    }

}
