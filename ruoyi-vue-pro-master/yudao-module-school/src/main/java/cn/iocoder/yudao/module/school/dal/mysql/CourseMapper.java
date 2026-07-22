package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.controller.admin.vo.course.CourseListReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapperX<CourseDO> {

    default List<CourseDO> selectList(CourseListReqVO reqVO) {
        LambdaQueryWrapperX<CourseDO> wrapper = new LambdaQueryWrapperX<CourseDO>();
        if (reqVO != null) {
            wrapper.likeIfPresent(CourseDO::getName, reqVO.getName())
                   .eqIfPresent(CourseDO::getType, reqVO.getType())
                   .eqIfPresent(CourseDO::getTeacherId, reqVO.getTeacherId())
                   .eqIfPresent(CourseDO::getCollegeId, reqVO.getCollegeId())
                   .eqIfPresent(CourseDO::getStatus, reqVO.getStatus());
        }
        return selectList(wrapper.orderByAsc(CourseDO::getSort));
    }

    
    default Long selectCountByType(Integer type) {
        return selectCount(CourseDO::getType, type);
    }

}
