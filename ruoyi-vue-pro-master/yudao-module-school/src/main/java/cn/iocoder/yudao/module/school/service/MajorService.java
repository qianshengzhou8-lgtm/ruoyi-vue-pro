package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.MajorDO;

import java.util.List;

public interface MajorService {

    Long createMajor(MajorSaveReqVO createReqVO);

    void updateMajor(MajorSaveReqVO updateReqVO);

    void deleteMajor(Long id);

    MajorDO getMajor(Long id);

    List<MajorDO> getMajorList(MajorListReqVO reqVO);

    ImportRespVO importMajorList(List<MajorImportExcelVO> list);

}
