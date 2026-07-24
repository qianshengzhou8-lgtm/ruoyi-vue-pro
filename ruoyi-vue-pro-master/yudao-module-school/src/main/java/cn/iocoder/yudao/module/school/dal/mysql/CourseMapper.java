package cn.iocoder.yudao.module.school.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.school.dal.dataobject.CourseDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourseMapper extends BaseMapperX<CourseDO> {

    /**
     * 行级锁查询课程，用于选课时防止超卖
     */
    @Select("SELECT * FROM school_course WHERE id = #{id} FOR UPDATE")
    CourseDO selectByIdForUpdate(@Param("id") Long id);

    default Long selectCountByType(Integer type) {
        return selectCount(CourseDO::getType, type);
    }

}
