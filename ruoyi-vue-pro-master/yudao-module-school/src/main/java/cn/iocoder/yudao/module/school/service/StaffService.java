package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StaffDO;

import java.util.List;

public interface StaffService {

    Long createStaff(StaffSaveReqVO createReqVO);

    void updateStaff(StaffSaveReqVO updateReqVO);

    void deleteStaff(Long id);

    StaffDO getStaff(Long id);

    List<StaffDO> getStaffList(StaffListReqVO reqVO);

    /**
     * 根据学院ID获取教职工列表（数据权限过滤，下推到数据库）
     */
    List<StaffDO> getStaffListByCollegeId(StaffListReqVO reqVO, Long collegeId);

    StaffDO getStaffByUsername(String username);

    /**
     * 导入教职工列表
     */
    ImportRespVO importStaffList(List<StaffImportExcelVO> list);

}
