package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorListReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.MajorDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MajorMapper extends BaseMapperX<MajorDO> {

    default List<MajorDO> selectList(MajorListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MajorDO>()
                .likeIfPresent(MajorDO::getName, reqVO.getName())
                .eqIfPresent(MajorDO::getCollegeId, reqVO.getCollegeId())
                .eqIfPresent(MajorDO::getStatus, reqVO.getStatus())
                .orderByAsc(MajorDO::getSort));
    }

}
