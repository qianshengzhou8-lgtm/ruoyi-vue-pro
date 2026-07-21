package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.course.*;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseDO;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseSelectionDO;
import cn.iocoder.yudao.module.school.dal.mysql.CourseMapper;
import cn.iocoder.yudao.module.school.dal.mysql.CourseSelectionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.*;

@Service
@Validated
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseMapper courseMapper;
    @Resource
    private CourseSelectionMapper selectionMapper;

    @Override
    public Long createCourse(CourseSaveReqVO reqVO) {
        CourseDO course = BeanUtils.toBean(reqVO, CourseDO.class);
        courseMapper.insert(course);
        return course.getId();
    }

    @Override
    public void updateCourse(CourseSaveReqVO reqVO) {
        validateCourseExists(reqVO.getId());
        CourseDO course = BeanUtils.toBean(reqVO, CourseDO.class);
        courseMapper.updateById(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourse(Long id) {
        validateCourseExists(id);
        courseMapper.deleteById(id);
    }

    @Override
    public CourseDO getCourse(Long id) {
        return courseMapper.selectById(id);
    }

    @Override
    public List<CourseDO> getCourseList(CourseListReqVO reqVO) {
        return courseMapper.selectList(reqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String selectCourse(Long courseId, Long studentId) {
        CourseDO course = courseMapper.selectById(courseId);
        if (course == null) throw exception(COURSE_NOT_EXISTS);
        // 检查是否已选
        CourseSelectionDO existing = selectionMapper.selectByCourseAndStudent(courseId, studentId);
        if (existing != null) throw exception(COURSE_ALREADY_SELECTED);
        // 选修课检查人数上限
        if (course.getType() != null && course.getType() == 1 && course.getCapacity() != null) {
            Long currentCount = selectionMapper.selectCountByCourseId(courseId);
            if (currentCount >= course.getCapacity()) throw exception(COURSE_CAPACITY_FULL);
        }
        CourseSelectionDO selection = new CourseSelectionDO();
        selection.setCourseId(courseId);
        selection.setStudentId(studentId);
        selectionMapper.insert(selection);
        return "选课成功";
    }

    @Override
    public void deselectCourse(Long courseId, Long studentId) {
        CourseSelectionDO existing = selectionMapper.selectByCourseAndStudent(courseId, studentId);
        if (existing == null) throw exception(COURSE_NOT_SELECTED);
        selectionMapper.deleteById(existing.getId());
    }

    @Override
    public CourseStatisticsRespVO getStatistics() {
        CourseStatisticsRespVO vo = new CourseStatisticsRespVO();
        List<CourseDO> allCourses = courseMapper.selectList(new CourseListReqVO());
        vo.setTotalCourses((long) allCourses.size());
        vo.setRequiredCount(allCourses.stream().filter(c -> c.getType() != null && c.getType() == 0).count());
        vo.setElectiveCount(allCourses.stream().filter(c -> c.getType() != null && c.getType() == 1).count());
        // 统计选课数据（单条 COUNT 查询，避免 N+1 问题）
        vo.setTotalSelections(selectionMapper.selectAllCount());
        vo.setTotalStudents(selectionMapper.selectDistinctStudentCount());
        return vo;
    }

    private void validateCourseExists(Long id) {
        if (id == null || courseMapper.selectById(id) == null)
            throw exception(COURSE_NOT_EXISTS);
    }

}
