package cn.iocoder.yudao.module.school.service;

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

    StaffDO getStaffByUsername(String username);

}
