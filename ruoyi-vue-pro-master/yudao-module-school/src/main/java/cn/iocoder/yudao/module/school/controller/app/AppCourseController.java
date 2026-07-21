package cn.iocoder.yudao.module.school.controller.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.course.CourseRespVO;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseDO;
import cn.iocoder.yudao.module.school.dal.mysql.CourseSelectionMapper;
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

    @GetMapping("/list")
    @Operation(summary = "浏览可选课程列表（含已选人数）")
    public CommonResult<List<CourseRespVO>> getCourseList() {
        List<CourseDO> list = courseService.getCourseList(null);
        List<CourseRespVO> result = BeanUtils.toBean(list, CourseRespVO.class);
        Long studentId = getLoginUserId();
        for (int i = 0; i < list.size(); i++) {
            CourseDO c = list.get(i);
            result.get(i).setSelectedCount(selectionMapper.selectCountByCourseId(c.getId()));
            // 标记当前学生是否已选
            boolean selected = selectionMapper.selectByCourseAndStudent(c.getId(), studentId) != null;
            // 通过 capacity 为 null 表示该字段暂用于标记已选状态（前端使用）
        }
        return success(result);
    }

    @PostMapping("/{courseId}/select")
    @Operation(summary = "选课")
    public CommonResult<String> selectCourse(@PathVariable("courseId") Long courseId) {
        Long studentId = getLoginUserId();
        return success(courseService.selectCourse(courseId, studentId));
    }

    @PostMapping("/{courseId}/deselect")
    @Operation(summary = "退课")
    public CommonResult<Boolean> deselectCourse(@PathVariable("courseId") Long courseId) {
        Long studentId = getLoginUserId();
        courseService.deselectCourse(courseId, studentId);
        return success(true);
    }

}
