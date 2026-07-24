package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.ClassDO;

import java.util.List;

public interface ClassService {

    Long createClass(ClassSaveReqVO createReqVO);

    void updateClass(ClassSaveReqVO updateReqVO);

    void deleteClass(Long id);

    ClassDO getClass(Long id);

    List<ClassDO> getClassList(ClassListReqVO reqVO);

    /**
     * 根据专业ID获取班级列表
     */
    List<ClassDO> getClassListByMajorId(Long majorId);

    /**
     * 根据学院ID获取班级列表（数据权限过滤，下推到数据库）
     */
    List<ClassDO> getClassListByCollegeId(ClassListReqVO reqVO, Long collegeId);

    /**
     * 导入班级列表
     */
    ImportRespVO importClassList(List<ClassImportExcelVO> list);

}
