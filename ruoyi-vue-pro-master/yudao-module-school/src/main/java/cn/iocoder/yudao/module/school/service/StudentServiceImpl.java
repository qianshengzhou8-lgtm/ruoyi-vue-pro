package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.mysql.user.MemberUserMapper;
import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import cn.iocoder.yudao.module.school.dal.dataobject.ClassDO;
import cn.iocoder.yudao.module.school.dal.mysql.ClassMapper;
import cn.iocoder.yudao.module.school.dal.mysql.StaffMapper;
import cn.iocoder.yudao.module.school.dal.mysql.StudentMapper;
import cn.iocoder.yudao.module.school.util.SchoolDataPermissionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
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
    private MemberUserMapper memberUserMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private SchoolDataPermissionUtil dataPermissionUtil;

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
        // 同步创建 member_user 记录，使学生可通过 /app-api/member/auth/login 登录移动端
        String mobile = createReqVO.getMobile() != null ? createReqVO.getMobile() : createReqVO.getUsername();
        MemberUserDO memberUser = new MemberUserDO();
        memberUser.setMobile(mobile);
        memberUser.setPassword(encodedPwd);
        memberUser.setNickname(createReqVO.getName());
        memberUser.setName(createReqVO.getName());
        memberUser.setStatus(createReqVO.getStatus() != null ? createReqVO.getStatus() : 0);
        memberUser.setTenantId(TenantContextHolder.getRequiredTenantId());
        memberUserMapper.insert(memberUser);
        return student.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStudent(StudentSaveReqVO updateReqVO) {
        validateStudentExists(updateReqVO.getId());
        // 数据权限检查
        if (!dataPermissionUtil.hasAccessToStudent(updateReqVO.getId())) {
            throw exception(DATA_PERMISSION_DENIED);
        }
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
        // 同步更新 member_user
        String oldMobile = old.getMobile() != null ? old.getMobile() : old.getUsername();
        MemberUserDO memberUser = memberUserMapper.selectByMobile(oldMobile);
        if (memberUser != null) {
            String newMobile = updateReqVO.getMobile() != null ? updateReqVO.getMobile() : updateReqVO.getUsername();
            memberUser.setMobile(newMobile);
            memberUser.setNickname(updateReqVO.getName());
            memberUser.setName(updateReqVO.getName());
            memberUser.setStatus(updateReqVO.getStatus() != null ? updateReqVO.getStatus() : 0);
            if (updateReqVO.getPassword() != null && !updateReqVO.getPassword().isEmpty()) {
                memberUser.setPassword(passwordEncoder.encode(updateReqVO.getPassword()));
            }
            memberUserMapper.updateById(memberUser);
        } else {
            log.warn("[updateStudent] 未找到对应的 member_user 记录, mobile={}", oldMobile);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStudent(Long id) {
        validateStudentExists(id);
        // 数据权限检查
        if (!dataPermissionUtil.hasAccessToStudent(id)) {
            throw exception(DATA_PERMISSION_DENIED);
        }
        StudentDO student = studentMapper.selectById(id);
        // 先删本地学生记录，再同步删除 member_user
        studentMapper.deleteById(id);
        String mobile = student.getMobile() != null ? student.getMobile() : student.getUsername();
        MemberUserDO memberUser = memberUserMapper.selectByMobile(mobile);
        if (memberUser != null) {
            memberUserMapper.deleteById(memberUser.getId());
        } else {
            log.warn("[deleteStudent] 未找到对应的 member_user 记录, mobile={}", mobile);
        }
    }

    @Override
    public StudentDO getStudent(Long id) {
        return studentMapper.selectById(id);
    }

    @Override
    public List<StudentDO> getStudentList(StudentListReqVO reqVO) {
        LambdaQueryWrapperX<StudentDO> wrapper = new LambdaQueryWrapperX<StudentDO>()
                .likeIfPresent(StudentDO::getName, reqVO.getName())
                .eqIfPresent(StudentDO::getClassId, reqVO.getClassId())
                .eqIfPresent(StudentDO::getStatus, reqVO.getStatus());
        wrapper.orderByDesc(StudentDO::getCreateTime);
        return studentMapper.selectList(wrapper);
    }

    @Override
    public List<StudentDO> getStudentListByCollegeId(StudentListReqVO reqVO, Long collegeId) {
        LambdaQueryWrapperX<StudentDO> wrapper = new LambdaQueryWrapperX<StudentDO>()
                .likeIfPresent(StudentDO::getName, reqVO.getName())
                .eqIfPresent(StudentDO::getStatus, reqVO.getStatus());
        // collegeId 为内部 Long 类型，非用户输入，安全
        wrapper.apply("class_id IN (SELECT c.id FROM school_class c INNER JOIN school_major m ON c.major_id = m.id WHERE m.college_id = {0})", collegeId);
        wrapper.orderByDesc(StudentDO::getCreateTime);
        return studentMapper.selectList(wrapper);
    }

    @Override
    public PageResult<StudentDO> getStudentPage(StudentListReqVO reqVO) {
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(reqVO.getPageNo());
        pageParam.setPageSize(reqVO.getPageSize());
        LambdaQueryWrapperX<StudentDO> wrapper = new LambdaQueryWrapperX<StudentDO>()
                .likeIfPresent(StudentDO::getName, reqVO.getName())
                .eqIfPresent(StudentDO::getClassId, reqVO.getClassId())
                .eqIfPresent(StudentDO::getStatus, reqVO.getStatus())
                .orderByDesc(StudentDO::getCreateTime);
        return studentMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public PageResult<StudentDO> getStudentPageByCollegeId(StudentListReqVO reqVO, Long collegeId) {
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(reqVO.getPageNo());
        pageParam.setPageSize(reqVO.getPageSize());
        LambdaQueryWrapperX<StudentDO> wrapper = new LambdaQueryWrapperX<StudentDO>()
                .likeIfPresent(StudentDO::getName, reqVO.getName())
                .eqIfPresent(StudentDO::getStatus, reqVO.getStatus());
        // collegeId 为内部 Long 类型，非用户输入，安全
        wrapper.apply("class_id IN (SELECT c.id FROM school_class c INNER JOIN school_major m ON c.major_id = m.id WHERE m.college_id = {0})", collegeId);
        wrapper.orderByDesc(StudentDO::getCreateTime);
        return studentMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public StudentDO getStudentByUsername(String username) {
        return studentMapper.selectByUsername(username);
    }

    @Override
    public boolean hasStudentInCollege(Long studentId, Long collegeId) {
        StudentDO student = studentMapper.selectById(studentId);
        if (student == null) {
            return false;
        }
        return studentMapper.hasStudentInCollege(studentId, collegeId);
    }

    @Override
    public ImportRespVO importStudentList(List<StudentImportExcelVO> list) {
        ImportRespVO result = new ImportRespVO();
        for (StudentImportExcelVO vo : list) {
            try {
                StudentSaveReqVO saveVO = BeanUtils.toBean(vo, StudentSaveReqVO.class);
                createStudent(saveVO);
                result.setCreateCount(result.getCreateCount() + 1);
            } catch (Exception e) {
                result.setFailureCount(result.getFailureCount() + 1);
                result.getFailureReasons().add(vo.getName() + ": " + e.getMessage());
            }
        }
        return result;
    }

    private void validateStudentExists(Long id) {
        if (id == null || studentMapper.selectById(id) == null) {
            throw exception(STUDENT_NOT_EXISTS);
        }
    }

    private void validateUsernameUnique(String username, Long id) {
        // 检查学生表
        StudentDO existingStudent = studentMapper.selectByUsername(username);
        if (existingStudent != null && !existingStudent.getId().equals(id)) {
            throw exception(STUDENT_USERNAME_DUPLICATE);
        }
        // 检查教职工表
        if (staffMapper.selectByUsername(username) != null) {
            throw exception(STUDENT_USERNAME_DUPLICATE);
        }
        // 检查会员表（通过mobile检查，因为student的username通常作为mobile）
        MemberUserDO existingMember = memberUserMapper.selectByMobile(username);
        if (existingMember != null && !existingMember.getId().equals(id)) {
            throw exception(STUDENT_USERNAME_DUPLICATE);
        }
    }

}