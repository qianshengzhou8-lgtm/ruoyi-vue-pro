package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffListReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StaffDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StaffMapper extends BaseMapperX<StaffDO> {

    default List<StaffDO> selectList(StaffListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StaffDO>()
                .likeIfPresent(StaffDO::getName, reqVO.getName())
                .eqIfPresent(StaffDO::getCollegeId, reqVO.getCollegeId())
                .eqIfPresent(StaffDO::getStatus, reqVO.getStatus())
                .orderByDesc(StaffDO::getCreateTime));
    }

    default StaffDO selectByUsername(String username) {
        return selectOne(StaffDO::getUsername, username);
    }

}
