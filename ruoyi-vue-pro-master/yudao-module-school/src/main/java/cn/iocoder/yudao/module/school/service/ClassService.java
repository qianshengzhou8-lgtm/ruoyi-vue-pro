package cn.iocoder.yudao.module.school.service;

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

}
