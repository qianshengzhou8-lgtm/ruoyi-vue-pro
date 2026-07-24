package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
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
import java.util.Objects;

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
        // 先删除该课程的选课记录，避免孤儿数据
        selectionMapper.delete(new LambdaQueryWrapperX<CourseSelectionDO>().eq(CourseSelectionDO::getCourseId, id));
        // 再删除课程本身
        courseMapper.deleteById(id);
    }

    @Override
    public CourseDO getCourse(Long id) {
        return courseMapper.selectById(id);
    }

    @Override
    public List<CourseDO> getCourseList(CourseListReqVO reqVO) {
        if (reqVO == null) {
            reqVO = new CourseListReqVO();
        }
        LambdaQueryWrapperX<CourseDO> wrapper = new LambdaQueryWrapperX<CourseDO>()
                .likeIfPresent(CourseDO::getName, reqVO.getName())
                .eqIfPresent(CourseDO::getType, reqVO.getType())
                .eqIfPresent(CourseDO::getTeacherId, reqVO.getTeacherId())
                .eqIfPresent(CourseDO::getCollegeId, reqVO.getCollegeId())
                .eqIfPresent(CourseDO::getStatus, reqVO.getStatus());
        wrapper.orderByAsc(CourseDO::getSort);
        return courseMapper.selectList(wrapper);
    }

    @Override
    public PageResult<CourseDO> getCoursePage(CourseListReqVO reqVO) {
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(reqVO.getPageNo());
        pageParam.setPageSize(reqVO.getPageSize());
        LambdaQueryWrapperX<CourseDO> wrapper = new LambdaQueryWrapperX<CourseDO>()
                .likeIfPresent(CourseDO::getName, reqVO.getName())
                .eqIfPresent(CourseDO::getType, reqVO.getType())
                .eqIfPresent(CourseDO::getTeacherId, reqVO.getTeacherId())
                .eqIfPresent(CourseDO::getCollegeId, reqVO.getCollegeId())
                .eqIfPresent(CourseDO::getStatus, reqVO.getStatus());
        wrapper.orderByAsc(CourseDO::getSort);
        return courseMapper.selectPage(pageParam, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String selectCourse(Long courseId, Long studentId) {
        // 先检查是否已选（快速失败，无需加锁）
        CourseSelectionDO existing = selectionMapper.selectByCourseAndStudent(courseId, studentId);
        if (existing != null) throw exception(COURSE_ALREADY_SELECTED);
        // 行级锁查询课程，防止选修课超卖
        CourseDO course = courseMapper.selectByIdForUpdate(courseId);
        if (course == null) throw exception(COURSE_NOT_EXISTS);
        // 选修课：检查容量
        if (Objects.equals(course.getType(), 1) && course.getCapacity() != null) {
            long currentCount = selectionMapper.selectCountByCourseId(courseId);
            if (currentCount >= course.getCapacity()) {
                throw exception(COURSE_CAPACITY_FULL);
            }
        }
        // 使用 MyBatis-Plus 正常插入，自动填充 tenant_id/creator/create_time
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
        vo.setTotalCourses(courseMapper.selectCount(null));
        vo.setRequiredCount(courseMapper.selectCountByType(0));
        vo.setElectiveCount(courseMapper.selectCountByType(1));
        vo.setTotalSelections(selectionMapper.selectAllCount());
        vo.setTotalStudents(selectionMapper.selectDistinctStudentCount());
        return vo;
    }

    @Override
    public Long getSelectedCountByCourseId(Long courseId) {
        return selectionMapper.selectCountByCourseId(courseId);
    }

    @Override
    public java.util.Map<Long, Long> getSelectedCountMapByCourseIds(java.util.List<Long> courseIds) {
        return selectionMapper.selectCountByCourseIds(courseIds);
    }

    private void validateCourseExists(Long id) {
        if (id == null || courseMapper.selectById(id) == null)
            throw exception(COURSE_NOT_EXISTS);
    }

}