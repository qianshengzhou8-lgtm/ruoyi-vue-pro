package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseSelectionDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseSelectionMapper extends BaseMapperX<CourseSelectionDO> {

    default Long selectCountByCourseId(Long courseId) {
        return selectCount(CourseSelectionDO::getCourseId, courseId);
    }

    default CourseSelectionDO selectByCourseAndStudent(Long courseId, Long studentId) {
        return selectOne(new LambdaQueryWrapperX<CourseSelectionDO>()
                .eq(CourseSelectionDO::getCourseId, courseId)
                .eq(CourseSelectionDO::getStudentId, studentId));
    }

    /**
     * 使用 LambdaQueryWrapper 统计不同学生数量，自动应用多租户过滤
     */
    default Long selectDistinctStudentCount() {
        LambdaQueryWrapperX<CourseSelectionDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.select(CourseSelectionDO::getStudentId);
        wrapper.groupBy(CourseSelectionDO::getStudentId);
        return (long) selectList(wrapper).size();
    }

    default Long selectAllCount() {
        return selectCount(null);
    }

    /**
     * 批量查询课程选课人数，避免 N+1 查询
     */
    default java.util.Map<Long, Long> selectCountByCourseIds(java.util.List<Long> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        java.util.List<CourseSelectionDO> list = selectList(
                new LambdaQueryWrapperX<CourseSelectionDO>().in(CourseSelectionDO::getCourseId, courseIds));
        return list.stream().collect(java.util.stream.Collectors.groupingBy(
                CourseSelectionDO::getCourseId, java.util.stream.Collectors.counting()));
    }

}
