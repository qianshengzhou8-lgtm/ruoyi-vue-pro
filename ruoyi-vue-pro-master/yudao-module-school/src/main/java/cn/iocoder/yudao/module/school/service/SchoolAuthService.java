package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.module.school.controller.admin.vo.auth.SchoolLoginReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.auth.SchoolLoginRespVO;

/**
 * 学校认证 Service 接口
 */
public interface SchoolAuthService {

    /**
     * 教职工登录
     */
    SchoolLoginRespVO staffLogin(SchoolLoginReqVO reqVO);

    /**
     * 学生登录
     */
    SchoolLoginRespVO studentLogin(SchoolLoginReqVO reqVO);

    /**
     * 登出
     *
     * @param token 访问令牌
     */
    void logout(String token);

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 登录结果
     */
    SchoolLoginRespVO refreshToken(String refreshToken);

}
