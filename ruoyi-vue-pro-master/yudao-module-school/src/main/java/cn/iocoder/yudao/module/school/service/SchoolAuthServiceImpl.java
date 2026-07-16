package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.school.controller.admin.vo.auth.SchoolLoginReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.auth.SchoolLoginRespVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StaffDO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    private OAuth2TokenCommonApi oauth2TokenApi;

    @Override
    public SchoolLoginRespVO staffLogin(SchoolLoginReqVO reqVO) {
        // 认证教职工身份
        StaffDO staff = staffService.getStaffByUsername(reqVO.getUsername());
        if (staff == null || !passwordEncoder.matches(reqVO.getPassword(), staff.getPassword())) {
            throw exception(STAFF_BAD_CREDENTIALS);
        }
        if (CommonStatusEnum.isDisable(staff.getStatus())) {
            throw exception(STAFF_BAD_CREDENTIALS);
        }
        // 创建 OAuth2 令牌（userType=ADMIN，允许访问 /admin-api/**）
        return createTokenAfterLoginSuccess(staff.getId(), UserTypeEnum.ADMIN, "staff", staff.getName());
    }

    @Override
    public SchoolLoginRespVO studentLogin(SchoolLoginReqVO reqVO) {
        // 认证学生身份
        StudentDO student = studentService.getStudentByUsername(reqVO.getUsername());
        if (student == null || !passwordEncoder.matches(reqVO.getPassword(), student.getPassword())) {
            throw exception(STUDENT_BAD_CREDENTIALS);
        }
        if (CommonStatusEnum.isDisable(student.getStatus())) {
            throw exception(STUDENT_BAD_CREDENTIALS);
        }
        // 创建 OAuth2 令牌（userType=MEMBER，仅允许访问 /app-api/**）
        return createTokenAfterLoginSuccess(student.getId(), UserTypeEnum.MEMBER, "student", student.getName());
    }

    @Override
    public void logout(String token) {
        oauth2TokenApi.removeAccessToken(token);
    }

    @Override
    public SchoolLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenRespDTO accessToken = oauth2TokenApi.refreshAccessToken(refreshToken,
                OAuth2ClientConstants.CLIENT_ID_DEFAULT);
        return buildRespVO(accessToken, null, null);
    }

    private SchoolLoginRespVO createTokenAfterLoginSuccess(Long userId, UserTypeEnum userType,
                                                            String userTypeName, String name) {
        OAuth2AccessTokenRespDTO accessToken = oauth2TokenApi.createAccessToken(
                new OAuth2AccessTokenCreateReqDTO()
                        .setUserId(userId)
                        .setUserType(userType.getValue())
                        .setClientId(OAuth2ClientConstants.CLIENT_ID_DEFAULT));
        return buildRespVO(accessToken, userTypeName, name);
    }

    private SchoolLoginRespVO buildRespVO(OAuth2AccessTokenRespDTO token, String userType, String name) {
        SchoolLoginRespVO respVO = new SchoolLoginRespVO();
        respVO.setAccessToken(token.getAccessToken());
        respVO.setRefreshToken(token.getRefreshToken());
        respVO.setExpiresTime(token.getExpiresTime());
        respVO.setUserId(token.getUserId());
        respVO.setUserType(userType);
        respVO.setName(name);
        return respVO;
    }

}
