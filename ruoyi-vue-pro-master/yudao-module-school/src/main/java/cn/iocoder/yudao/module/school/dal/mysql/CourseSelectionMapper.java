package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseSelectionDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
        // 使用 GROUP BY + COUNT 来统计不同的 student_id 数量
        // MP 的租户拦截器会自动添加 tenant_id 条件
        LambdaQueryWrapperX<CourseSelectionDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.select(CourseSelectionDO::getStudentId);
        wrapper.groupBy(CourseSelectionDO::getStudentId);
        return (long) selectList(wrapper).size();
    }

    /**
     * 原子性选课插入：仅当课程当前选课人数小于容量时才插入成功
     * @return 影响行数（1表示成功，0表示容量已满）
     */
    @Insert("INSERT INTO school_course_selection (course_id, student_id, creator, create_time, updater, update_time, deleted, tenant_id) " +
            "SELECT #{courseId}, #{studentId}, '1', NOW(), '1', NOW(), b'0', #{tenantId} " +
            "WHERE (SELECT COUNT(*) FROM school_course_selection WHERE course_id = #{courseId} AND deleted = 0) < #{capacity} AND " +
            "      (SELECT COUNT(*) FROM school_course_selection WHERE course_id = #{courseId} AND student_id = #{studentId} AND deleted = 0) = 0")
    Long insertWithCapacityCheck(@Param("courseId") Long courseId,
                                   @Param("studentId") Long studentId,
                                   @Param("capacity") Integer capacity,
                                   @Param("tenantId") Long tenantId);

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
