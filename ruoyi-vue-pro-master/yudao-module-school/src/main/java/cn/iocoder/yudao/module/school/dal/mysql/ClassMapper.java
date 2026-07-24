package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassListReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.ClassDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassMapper extends BaseMapperX<ClassDO> {

    default List<ClassDO> selectList(ClassListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ClassDO>()
                .likeIfPresent(ClassDO::getName, reqVO.getName())
                .eqIfPresent(ClassDO::getMajorId, reqVO.getMajorId())
                .eqIfPresent(ClassDO::getStatus, reqVO.getStatus())
                .orderByAsc(ClassDO::getSort));
    }

    /**
     * 根据专业ID获取班级列表
     */
    default List<ClassDO> selectListByMajorId(Long majorId) {
        return selectList(new LambdaQueryWrapperX<ClassDO>()
                .eq(ClassDO::getMajorId, majorId)
                .orderByAsc(ClassDO::getSort));
    }

    /**
     * 根据学院ID获取班级列表（通过班级→专业→学院链路过滤，下推到数据库）
     */
    default List<ClassDO> selectListByCollegeId(ClassListReqVO reqVO, Long collegeId) {
        LambdaQueryWrapperX<ClassDO> wrapper = new LambdaQueryWrapperX<ClassDO>()
                .likeIfPresent(ClassDO::getName, reqVO.getName())
                .eqIfPresent(ClassDO::getStatus, reqVO.getStatus());
        wrapper.apply("major_id IN (SELECT m.id FROM school_major m WHERE m.college_id = {0})", collegeId);
        wrapper.orderByAsc(ClassDO::getSort);
        return selectList(wrapper);
    }

}
