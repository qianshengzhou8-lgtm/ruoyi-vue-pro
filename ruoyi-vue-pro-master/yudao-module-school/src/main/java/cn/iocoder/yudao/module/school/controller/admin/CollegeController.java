package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.CollegeDO;
import cn.iocoder.yudao.module.school.service.CollegeService;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
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
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.COLLEGE_NOT_EXISTS;

@Tag(name = "管理后台 - 学院管理")
@RestController
@RequestMapping("/school/college")
@Validated
public class CollegeController {

    @Resource
    private CollegeService collegeService;

    @PostMapping("/create")
    @Operation(summary = "创建学院")
    @PreAuthorize("@ss.hasPermission('school:college:create')")
    public CommonResult<Long> createCollege(@Valid @RequestBody CollegeSaveReqVO createReqVO) {
        return success(collegeService.createCollege(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新学院")
    @PreAuthorize("@ss.hasPermission('school:college:update')")
    public CommonResult<Boolean> updateCollege(@Valid @RequestBody CollegeSaveReqVO updateReqVO) {
        collegeService.updateCollege(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除学院")
    @PreAuthorize("@ss.hasPermission('school:college:delete')")
    public CommonResult<Boolean> deleteCollege(@RequestParam("id") Long id) {
        collegeService.deleteCollege(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得学院信息")
    @PreAuthorize("@ss.hasPermission('school:college:query')")
    public CommonResult<CollegeRespVO> getCollege(@RequestParam("id") Long id) {
        CollegeDO college = collegeService.getCollege(id);
        if (college == null) { throw exception(COLLEGE_NOT_EXISTS); }
        return success(BeanUtils.toBean(college, CollegeRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得学院列表")
    @PreAuthorize("@ss.hasPermission('school:college:query')")
    public CommonResult<List<CollegeRespVO>> getCollegeList(@Valid CollegeListReqVO reqVO) {
        List<CollegeDO> list = collegeService.getCollegeList(reqVO);
        return success(BeanUtils.toBean(list, CollegeRespVO.class));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取学院精简列表", description = "只包含开启的学院，用于下拉框")
    public CommonResult<List<CollegeRespVO>> getSimpleCollegeList() {
        List<CollegeDO> list = collegeService.getCollegeListByStatus(CommonStatusEnum.ENABLE.getStatus());
        return success(BeanUtils.toBean(list, CollegeRespVO.class));
    }

}
