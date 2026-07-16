package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import cn.iocoder.yudao.module.school.dal.mysql.ClassMapper;
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
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private ClassMapper classMapper;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private PermissionService permissionService;
    @Resource
    private PasswordEncoder passwordEncoder;

    /** 学生默认角色：普通用户(2) */
    private static final Long STUDENT_DEFAULT_ROLE_ID = 2L;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createStudent(StudentSaveReqVO createReqVO) {
        validateUsernameUnique(createReqVO.getUsername(), null);
        if (createReqVO.getPassword() == null || createReqVO.getPassword().isEmpty()) {
            throw exception(STUDENT_PASSWORD_BLANK);
        }
        if (classMapper.selectById(createReqVO.getClassId()) == null) {
            throw exception(CLASS_NOT_EXISTS);
        }
        StudentDO student = BeanUtils.toBean(createReqVO, StudentDO.class);
        String encodedPwd = passwordEncoder.encode(createReqVO.getPassword());
        student.setPassword(encodedPwd);
        studentMapper.insert(student);
        // 同步创建 system_users 记录，使学生可登录后台（受限权限）
        AdminUserDO adminUser = new AdminUserDO();
        adminUser.setUsername(createReqVO.getUsername());
        adminUser.setPassword(encodedPwd);
        adminUser.setNickname(createReqVO.getName());
        adminUser.setStatus(createReqVO.getStatus() != null ? createReqVO.getStatus() : 0);
        adminUser.setTenantId(TenantContextHolder.getRequiredTenantId());
        adminUserMapper.insert(adminUser);
        // 分配学生角色（受限权限）
        permissionService.assignUserRole(adminUser.getId(), Collections.singleton(STUDENT_DEFAULT_ROLE_ID));
        return student.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStudent(StudentSaveReqVO updateReqVO) {
        validateStudentExists(updateReqVO.getId());
        StudentDO old = studentMapper.selectById(updateReqVO.getId());
        validateUsernameUnique(updateReqVO.getUsername(), updateReqVO.getId());
        if (classMapper.selectById(updateReqVO.getClassId()) == null) {
            throw exception(CLASS_NOT_EXISTS);
        }
        StudentDO updateObj = BeanUtils.toBean(updateReqVO, StudentDO.class);
        if (updateReqVO.getPassword() != null && !updateReqVO.getPassword().isEmpty()) {
            updateObj.setPassword(passwordEncoder.encode(updateReqVO.getPassword()));
        } else {
            updateObj.setPassword(old.getPassword());
        }
        studentMapper.updateById(updateObj);
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
        } else {
            log.warn("[updateStudent] 未找到对应的 system_users 记录, username={}", old.getUsername());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStudent(Long id) {
        StudentDO student = studentMapper.selectById(id);
        if (student == null) { throw exception(STUDENT_NOT_EXISTS); }
        // 先删本地学生记录，再同步删除 system_users
        studentMapper.deleteById(id);
        AdminUserDO adminUser = adminUserMapper.selectOne(AdminUserDO::getUsername, student.getUsername());
        if (adminUser != null) {
            adminUserMapper.deleteById(adminUser.getId());
        } else {
            log.warn("[deleteStudent] 未找到对应的 system_users 记录, username={}", student.getUsername());
        }
    }

    @Override
    public StudentDO getStudent(Long id) {
        return studentMapper.selectById(id);
    }

    @Override
    public List<StudentDO> getStudentList(StudentListReqVO reqVO) {
        return studentMapper.selectList(reqVO);
    }

    @Override
    public StudentDO getStudentByUsername(String username) {
        return studentMapper.selectByUsername(username);
    }

    private void validateStudentExists(Long id) {
        if (id == null || studentMapper.selectById(id) == null) {
            throw exception(STUDENT_NOT_EXISTS);
        }
    }

    private void validateUsernameUnique(String username, Long id) {
        StudentDO existingStudent = studentMapper.selectByUsername(username);
        if (existingStudent != null && !existingStudent.getId().equals(id)) {
            throw exception(STUDENT_USERNAME_DUPLICATE);
        }
        if (staffMapper.selectByUsername(username) != null) {
            throw exception(STUDENT_USERNAME_DUPLICATE);
        }
        // system_users 有数据库 UNIQUE 约束兜底，此处不再预查
    }

}
