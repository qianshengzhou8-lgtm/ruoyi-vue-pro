package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.ClassDO;
import cn.iocoder.yudao.module.school.service.ClassService;
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
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.CLASS_NOT_EXISTS;

@Tag(name = "管理后台 - 班级管理")
@RestController
@RequestMapping("/school/class")
@Validated
public class ClassController {

    @Resource
    private ClassService classService;

    @PostMapping("/create")
    @Operation(summary = "创建班级")
    @PreAuthorize("@ss.hasPermission('school:class:create')")
    public CommonResult<Long> createClass(@Valid @RequestBody ClassSaveReqVO createReqVO) {
        return success(classService.createClass(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新班级")
    @PreAuthorize("@ss.hasPermission('school:class:update')")
    public CommonResult<Boolean> updateClass(@Valid @RequestBody ClassSaveReqVO updateReqVO) {
        classService.updateClass(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除班级")
    @PreAuthorize("@ss.hasPermission('school:class:delete')")
    public CommonResult<Boolean> deleteClass(@RequestParam("id") Long id) {
        classService.deleteClass(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得班级信息")
    @PreAuthorize("@ss.hasPermission('school:class:query')")
    public CommonResult<ClassRespVO> getClass(@RequestParam("id") Long id) {
        ClassDO c = classService.getClass(id);
        if (c == null) { throw exception(CLASS_NOT_EXISTS); }
        return success(BeanUtils.toBean(c, ClassRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得班级列表")
    @PreAuthorize("@ss.hasPermission('school:class:query')")
    public CommonResult<List<ClassRespVO>> getClassList(@Valid ClassListReqVO reqVO) {
        List<ClassDO> list = classService.getClassList(reqVO);
        return success(BeanUtils.toBean(list, ClassRespVO.class));
    }

}
