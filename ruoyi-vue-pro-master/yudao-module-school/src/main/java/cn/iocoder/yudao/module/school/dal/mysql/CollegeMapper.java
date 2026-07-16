package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeListReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.CollegeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollegeMapper extends BaseMapperX<CollegeDO> {

    default List<CollegeDO> selectList(CollegeListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CollegeDO>()
                .likeIfPresent(CollegeDO::getName, reqVO.getName())
                .eqIfPresent(CollegeDO::getStatus, reqVO.getStatus())
                .orderByAsc(CollegeDO::getSort));
    }

}
