package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentListReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapperX<StudentDO> {

    default List<StudentDO> selectList(StudentListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StudentDO>()
                .likeIfPresent(StudentDO::getName, reqVO.getName())
                .eqIfPresent(StudentDO::getClassId, reqVO.getClassId())
                .eqIfPresent(StudentDO::getStatus, reqVO.getStatus())
                .orderByDesc(StudentDO::getCreateTime));
    }

    default List<StudentDO> selectListByCollegeId(StudentListReqVO reqVO, Long collegeId) {
        return selectList(new LambdaQueryWrapperX<StudentDO>()
                .likeIfPresent(StudentDO::getName, reqVO.getName())
                .eqIfPresent(StudentDO::getStatus, reqVO.getStatus())
                .inSql(StudentDO::getClassId, "SELECT id FROM school_class WHERE major_id IN (SELECT id FROM school_major WHERE college_id = " + collegeId + ")")
                .orderByDesc(StudentDO::getCreateTime));
    }

    default StudentDO selectByUsername(String username) {
        return selectOne(StudentDO::getUsername, username);
    }

    default StudentDO selectByMobile(String mobile) {
        return selectOne(StudentDO::getMobile, mobile);
    }

}
