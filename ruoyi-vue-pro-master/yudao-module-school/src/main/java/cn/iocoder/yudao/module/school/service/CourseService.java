package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.controller.admin.vo.course.*;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseDO;

import java.util.List;

public interface CourseService {

    Long createCourse(CourseSaveReqVO reqVO);

    void updateCourse(CourseSaveReqVO reqVO);

    void deleteCourse(Long id);

    CourseDO getCourse(Long id);

    List<CourseDO> getCourseList(CourseListReqVO reqVO);

    PageResult<CourseDO> getCoursePage(CourseListReqVO reqVO);

    /** 选课，返回选课结果消息 */
    String selectCourse(Long courseId, Long studentId);

    /** 退课 */
    void deselectCourse(Long courseId, Long studentId);

    /** 课程统计 */
    CourseStatisticsRespVO getStatistics();

    /** 获取课程已选人数 */
    Long getSelectedCountByCourseId(Long courseId);

    /** 批量获取课程已选人数（避免N+1查询） */
    java.util.Map<Long, Long> getSelectedCountMapByCourseIds(java.util.List<Long> courseIds);

}
