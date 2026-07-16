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

}
