package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.CollegeDO;

import java.util.List;

public interface CollegeService {

    Long createCollege(CollegeSaveReqVO createReqVO);

    void updateCollege(CollegeSaveReqVO updateReqVO);

    void deleteCollege(Long id);

    CollegeDO getCollege(Long id);

    List<CollegeDO> getCollegeList(CollegeListReqVO reqVO);

    List<CollegeDO> getCollegeListByStatus(Integer status);

}
