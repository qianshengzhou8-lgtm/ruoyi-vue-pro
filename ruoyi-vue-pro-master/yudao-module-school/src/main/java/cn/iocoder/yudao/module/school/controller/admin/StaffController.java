package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.staff.StaffSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StaffDO;
import cn.iocoder.yudao.module.school.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.STAFF_NOT_EXISTS;

@Tag(name = "管理后台 - 教职工管理")
@RestController
@RequestMapping("/school/staff")
@Validated
public class StaffController {

    @Resource
    private StaffService staffService;

    @PostMapping("/create")
    @Operation(summary = "创建教职工")
    @PreAuthorize("@ss.hasPermission('school:staff:create')")
    public CommonResult<Long> createStaff(@Valid @RequestBody StaffSaveReqVO createReqVO) {
        return success(staffService.createStaff(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新教职工")
    @PreAuthorize("@ss.hasPermission('school:staff:update')")
    public CommonResult<Boolean> updateStaff(@Valid @RequestBody StaffSaveReqVO updateReqVO) {
        staffService.updateStaff(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除教职工")
    @PreAuthorize("@ss.hasPermission('school:staff:delete')")
    public CommonResult<Boolean> deleteStaff(@RequestParam("id") Long id) {
        staffService.deleteStaff(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得教职工信息")
    @PreAuthorize("@ss.hasPermission('school:staff:query')")
    public CommonResult<StaffRespVO> getStaff(@RequestParam("id") Long id) {
        StaffDO s = staffService.getStaff(id);
        if (s == null) { throw exception(STAFF_NOT_EXISTS); }
        return success(BeanUtils.toBean(s, StaffRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得教职工列表")
    @PreAuthorize("@ss.hasPermission('school:staff:query')")
    public CommonResult<List<StaffRespVO>> getStaffList(@Valid StaffListReqVO reqVO) {
        List<StaffDO> list = staffService.getStaffList(reqVO);
        return success(BeanUtils.toBean(list, StaffRespVO.class));
    }

}
