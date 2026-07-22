package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentListReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapperX<StudentDO> {

    
    default StudentDO selectByUsername(String username) {
        return selectOne(StudentDO::getUsername, username);
    }

    default StudentDO selectByMobile(String mobile) {
        return selectOne(StudentDO::getMobile, mobile);
    }

    default boolean hasStudentInCollege(Long studentId, Long collegeId) {
        return selectCount(new LambdaQueryWrapperX<StudentDO>()
                .eq(StudentDO::getId, studentId)
                .apply("class_id IN (SELECT c.id FROM school_class c INNER JOIN school_major m ON c.major_id = m.id WHERE m.college_id = {0})", collegeId)) > 0;
    }

}
