package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.school.controller.admin.vo.auth.SchoolLoginReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.auth.SchoolLoginRespVO;
import cn.iocoder.yudao.module.school.service.SchoolAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "学校管理后台 - 认证")
@RestController
@RequestMapping("/school/auth")
@Validated
public class SchoolAuthController {

    @Resource
    private SchoolAuthService schoolAuthService;

    @PostMapping("/staff-login")
    @PermitAll
    @Operation(summary = "教职工登录")
    public CommonResult<SchoolLoginRespVO> staffLogin(@RequestBody @Valid SchoolLoginReqVO reqVO) {
        return success(schoolAuthService.staffLogin(reqVO));
    }

    @PostMapping("/student-login")
    @PermitAll
    @Operation(summary = "学生登录（移动端）")
    public CommonResult<SchoolLoginRespVO> studentLogin(@RequestBody @Valid SchoolLoginReqVO reqVO) {
        return success(schoolAuthService.studentLogin(reqVO));
    }

}
