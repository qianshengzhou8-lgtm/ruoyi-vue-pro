package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseSelectionDO;
import org.apache.ibatis.annotations.Mapper;
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

    @Select("SELECT COUNT(DISTINCT student_id) FROM school_course_selection")
    Long selectDistinctStudentCount();

    default Long selectAllCount() {
        return selectCount(null);
    }

}
