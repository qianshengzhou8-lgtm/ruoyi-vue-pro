package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.course.*;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseDO;
import cn.iocoder.yudao.module.school.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.COURSE_NOT_EXISTS;

@Tag(name = "管理后台 - 课程管理")
@RestController
@RequestMapping("/school/course")
@Validated
public class CourseController {

    @Resource
    private CourseService courseService;

    @PostMapping("/create")
    @Operation(summary = "创建课程")
    @PreAuthorize("@ss.hasPermission('school:course:create')")
    public CommonResult<Long> createCourse(@Valid @RequestBody CourseSaveReqVO createReqVO) {
        return success(courseService.createCourse(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新课程")
    @PreAuthorize("@ss.hasPermission('school:course:update')")
    public CommonResult<Boolean> updateCourse(@Valid @RequestBody CourseSaveReqVO updateReqVO) {
        courseService.updateCourse(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除课程")
    @PreAuthorize("@ss.hasPermission('school:course:delete')")
    public CommonResult<Boolean> deleteCourse(@RequestParam("id") Long id) {
        courseService.deleteCourse(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得课程信息")
    @PreAuthorize("@ss.hasPermission('school:course:query')")
    public CommonResult<CourseRespVO> getCourse(@RequestParam("id") Long id) {
        CourseDO course = courseService.getCourse(id);
        if (course == null) throw exception(COURSE_NOT_EXISTS);
        CourseRespVO vo = BeanUtils.toBean(course, CourseRespVO.class);
        vo.setSelectedCount(courseService.getSelectedCountByCourseId(course.getId()));
        return success(vo);
    }

    @GetMapping("/list")
    @Operation(summary = "获得课程列表")
    @PreAuthorize("@ss.hasPermission('school:course:query')")
    public CommonResult<List<CourseRespVO>> getCourseList(@Valid CourseListReqVO reqVO) {
        List<CourseDO> list = courseService.getCourseList(reqVO);
        List<CourseRespVO> result = BeanUtils.toBean(list, CourseRespVO.class);
        // 批量查询选课人数，单次查询避免 N+1 问题
        List<Long> courseIds = list.stream().map(CourseDO::getId).collect(Collectors.toList());
        Map<Long, Long> countMap = courseService.getSelectedCountMapByCourseIds(courseIds);
        for (CourseRespVO vo : result) {
            vo.setSelectedCount(countMap.getOrDefault(vo.getId(), 0L));
        }
        return success(result);
    }

    @GetMapping("/statistics")
    @Operation(summary = "课程统计")
    @PreAuthorize("@ss.hasPermission('school:course:query')")
    public CommonResult<CourseStatisticsRespVO> getStatistics() {
        return success(courseService.getStatistics());
    }

}
