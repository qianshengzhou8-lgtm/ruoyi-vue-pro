package cn.iocoder.yudao.module.school.controller.admin;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.config.SecurityProperties;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.auth.SchoolLoginReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.auth.SchoolLoginRespVO;
import cn.iocoder.yudao.module.school.service.SchoolAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "学校管理后台 - 认证")
@RestController
@RequestMapping("/school/auth")
@Validated
public class SchoolAuthController {

    @Resource
    private SchoolAuthService schoolAuthService;
    @Resource
    private SecurityProperties securityProperties;

    @PostMapping("/staff-login")
    @PermitAll
    @Operation(summary = "教职工登录")
    public CommonResult<SchoolLoginRespVO> staffLogin(@RequestBody @Valid SchoolLoginReqVO reqVO) {
        return success(schoolAuthService.staffLogin(reqVO));
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "登出系统")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            schoolAuthService.logout(token);
        }
        return success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    public CommonResult<SchoolLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(schoolAuthService.refreshToken(refreshToken));
    }

}
