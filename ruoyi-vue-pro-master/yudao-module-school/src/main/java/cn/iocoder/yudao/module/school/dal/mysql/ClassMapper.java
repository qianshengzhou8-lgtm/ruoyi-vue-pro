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
     * @param majorId 专业ID
     * @return 班级列表
     */
    default List<ClassDO> selectListByMajorId(Long majorId) {
        return selectList(new LambdaQueryWrapperX<ClassDO>()
                .eq(ClassDO::getMajorId, majorId)
                .orderByAsc(ClassDO::getSort));
    }

}
