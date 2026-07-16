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

}
