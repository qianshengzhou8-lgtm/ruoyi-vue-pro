package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.school.controller.admin.vo.auth.SchoolLoginReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.auth.SchoolLoginRespVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StaffDO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.*;

@Service
public class SchoolAuthServiceImpl implements SchoolAuthService {

    @Resource
    private StaffService staffService;
    @Resource
    private StudentService studentService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String TOKEN_PREFIX = "school_token:";
    private static final long TOKEN_EXPIRE_HOURS = 24;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private String generateToken() {
        byte[] tokenBytes = new byte[32];
        SECURE_RANDOM.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    @Override
    public SchoolLoginRespVO staffLogin(SchoolLoginReqVO reqVO) {
        StaffDO staff = staffService.getStaffByUsername(reqVO.getUsername());
        if (staff == null || !passwordEncoder.matches(reqVO.getPassword(), staff.getPassword())) {
            throw exception(STAFF_BAD_CREDENTIALS);
        }
        if (CommonStatusEnum.isDisable(staff.getStatus())) {
            throw exception(STAFF_BAD_CREDENTIALS);
        }
        String token = generateToken();
        stringRedisTemplate.opsForValue().set(TOKEN_PREFIX + token, "staff:" + staff.getId(), TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
        SchoolLoginRespVO respVO = new SchoolLoginRespVO();
        respVO.setAccessToken(token);
        respVO.setUserId(staff.getId());
        respVO.setUserType("staff");
        respVO.setName(staff.getName());
        return respVO;
    }

    @Override
    public SchoolLoginRespVO studentLogin(SchoolLoginReqVO reqVO) {
        StudentDO student = studentService.getStudentByUsername(reqVO.getUsername());
        if (student == null || !passwordEncoder.matches(reqVO.getPassword(), student.getPassword())) {
            throw exception(STUDENT_BAD_CREDENTIALS);
        }
        if (CommonStatusEnum.isDisable(student.getStatus())) {
            throw exception(STUDENT_BAD_CREDENTIALS);
        }
        String token = generateToken();
        stringRedisTemplate.opsForValue().set(TOKEN_PREFIX + token, "student:" + student.getId(), TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
        SchoolLoginRespVO respVO = new SchoolLoginRespVO();
        respVO.setAccessToken(token);
        respVO.setUserId(student.getId());
        respVO.setUserType("student");
        respVO.setName(student.getName());
        return respVO;
    }

}
