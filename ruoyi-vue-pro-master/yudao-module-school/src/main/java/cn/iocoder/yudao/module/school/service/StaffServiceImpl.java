package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StaffDO;
import cn.iocoder.yudao.module.school.dal.mysql.CollegeMapper;
import cn.iocoder.yudao.module.school.dal.mysql.StaffMapper;
import cn.iocoder.yudao.module.school.dal.mysql.StudentMapper;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.yudao.module.system.service.permission.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.*;

@Service
@Validated
@Slf4j
public class StaffServiceImpl implements StaffService {

    @Resource
    private StaffMapper staffMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private CollegeMapper collegeMapper;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private PermissionService permissionService;
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 教职工角色映射：SchoolRoleEnum → system_role ID
     * 0(教师) → 5001, 1(班主任) → 5002, 2(学院院长) → 5003, 3(校长) → 5004
     */
    private static Long mapSchoolRoleToSystemRole(Integer schoolRole) {
        if (schoolRole == null) return 5001L; // 默认为教师角色
        switch (schoolRole) {
            case 1: return 5002L; // 班主任
            case 2: return 5003L; // 学院院长
            case 3: return 5004L; // 校长
            default: return 5001L; // 教师
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createStaff(StaffSaveReqVO createReqVO) {
        validateUsernameUnique(createReqVO.getUsername(), null);
        if (createReqVO.getPassword() == null || createReqVO.getPassword().isEmpty()) {
            throw exception(STAFF_PASSWORD_BLANK);
        }
        if (collegeMapper.selectById(createReqVO.getCollegeId()) == null) {
            throw exception(COLLEGE_NOT_EXISTS);
        }
        StaffDO staff = BeanUtils.toBean(createReqVO, StaffDO.class);
        String encodedPwd = passwordEncoder.encode(createReqVO.getPassword());
        staff.setPassword(encodedPwd);
        staffMapper.insert(staff);
        // 同步创建 system_users 记录，使其可登录管理后台
        AdminUserDO adminUser = new AdminUserDO();
        adminUser.setUsername(createReqVO.getUsername());
        adminUser.setPassword(encodedPwd);
        adminUser.setNickname(createReqVO.getName());
        adminUser.setStatus(createReqVO.getStatus() != null ? createReqVO.getStatus() : 0);
        adminUser.setTenantId(TenantContextHolder.getRequiredTenantId());
        adminUserMapper.insert(adminUser);
        // 根据教职工role字段分配对应的系统角色，避免统一赋超管的安全风险
        Long systemRoleId = mapSchoolRoleToSystemRole(staff.getRole());
        permissionService.assignUserRole(adminUser.getId(), Collections.singleton(systemRoleId));
        return staff.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStaff(StaffSaveReqVO updateReqVO) {
        validateStaffExists(updateReqVO.getId());
        StaffDO old = staffMapper.selectById(updateReqVO.getId());
        validateUsernameUnique(updateReqVO.getUsername(), updateReqVO.getId());
        if (collegeMapper.selectById(updateReqVO.getCollegeId()) == null) {
            throw exception(COLLEGE_NOT_EXISTS);
        }
        StaffDO updateObj = BeanUtils.toBean(updateReqVO, StaffDO.class);
        if (updateReqVO.getPassword() != null && !updateReqVO.getPassword().isEmpty()) {
            updateObj.setPassword(passwordEncoder.encode(updateReqVO.getPassword()));
        } else {
            updateObj.setPassword(old.getPassword());
        }
        staffMapper.updateById(updateObj);
        // 同步更新 system_users
        AdminUserDO adminUser = adminUserMapper.selectOne(AdminUserDO::getUsername, old.getUsername());
        if (adminUser != null) {
            adminUser.setUsername(updateReqVO.getUsername());
            adminUser.setNickname(updateReqVO.getName());
            adminUser.setStatus(updateReqVO.getStatus() != null ? updateReqVO.getStatus() : 0);
            if (updateReqVO.getPassword() != null && !updateReqVO.getPassword().isEmpty()) {
                adminUser.setPassword(passwordEncoder.encode(updateReqVO.getPassword()));
            }
            adminUserMapper.updateById(adminUser);
            // 同步更新角色：如果教职工角色变更，则更新对应的系统角色
            Integer oldRole = old.getRole();
            Integer newRole = updateObj.getRole();
            if (oldRole != null && newRole != null && !oldRole.equals(newRole)) {
                Long newSystemRoleId = mapSchoolRoleToSystemRole(newRole);
                permissionService.assignUserRole(adminUser.getId(), Collections.singleton(newSystemRoleId));
            }
        } else {
            log.warn("[updateStaff] 未找到对应的 system_users 记录, username={}", old.getUsername());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStaff(Long id) {
        StaffDO staff = staffMapper.selectById(id);
        if (staff == null) { throw exception(STAFF_NOT_EXISTS); }
        // 先删本地教职工记录，再同步删除 system_users
        staffMapper.deleteById(id);
        AdminUserDO adminUser = adminUserMapper.selectOne(AdminUserDO::getUsername, staff.getUsername());
        if (adminUser != null) {
            adminUserMapper.deleteById(adminUser.getId());
        } else {
            log.warn("[deleteStaff] 未找到对应的 system_users 记录, username={}", staff.getUsername());
        }
    }

    @Override
    public StaffDO getStaff(Long id) {
        return staffMapper.selectById(id);
    }

    @Override
    public List<StaffDO> getStaffList(StaffListReqVO reqVO) {
        return staffMapper.selectList(reqVO);
    }

    @Override
    public StaffDO getStaffByUsername(String username) {
        return staffMapper.selectByUsername(username);
    }

    @Override
    public ImportRespVO importStaffList(List<StaffImportExcelVO> list) {
        ImportRespVO result = new ImportRespVO();
        for (StaffImportExcelVO vo : list) {
            try {
                StaffSaveReqVO saveVO = BeanUtils.toBean(vo, StaffSaveReqVO.class);
                createStaff(saveVO);
                result.setCreateCount(result.getCreateCount() + 1);
            } catch (Exception e) {
                result.setFailureCount(result.getFailureCount() + 1);
                result.getFailureReasons().add(vo.getName() + ": " + e.getMessage());
            }
        }
        return result;
    }

    private void validateStaffExists(Long id) {
        if (id == null || staffMapper.selectById(id) == null) {
            throw exception(STAFF_NOT_EXISTS);
        }
    }

    private void validateUsernameUnique(String username, Long id) {
        StaffDO existingStaff = staffMapper.selectByUsername(username);
        if (existingStaff != null && !existingStaff.getId().equals(id)) {
            throw exception(STAFF_USERNAME_DUPLICATE);
        }
        if (studentMapper.selectByUsername(username) != null) {
            throw exception(STAFF_USERNAME_DUPLICATE);
        }
        // system_users 有数据库 UNIQUE 约束兜底，此处不再预查
    }

}
