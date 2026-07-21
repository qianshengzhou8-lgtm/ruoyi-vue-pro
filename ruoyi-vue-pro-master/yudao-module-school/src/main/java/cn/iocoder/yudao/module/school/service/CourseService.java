package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.module.school.controller.admin.vo.course.*;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseDO;

import java.util.List;

public interface CourseService {

    Long createCourse(CourseSaveReqVO reqVO);

    void updateCourse(CourseSaveReqVO reqVO);

    void deleteCourse(Long id);

    CourseDO getCourse(Long id);

    List<CourseDO> getCourseList(CourseListReqVO reqVO);

    /** 选课，返回选课结果消息 */
    String selectCourse(Long courseId, Long studentId);

    /** 退课 */
    void deselectCourse(Long courseId, Long studentId);

    /** 课程统计 */
    CourseStatisticsRespVO getStatistics();

}
