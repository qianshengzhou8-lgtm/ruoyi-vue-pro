package cn.iocoder.yudao.module.school.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassRespVO;
import cn.iocoder.yudao.module.school.dal.dataobject.ClassDO;
import cn.iocoder.yudao.module.school.dal.dataobject.MajorDO;
import cn.iocoder.yudao.module.school.dal.dataobject.CollegeDO;
import cn.iocoder.yudao.module.school.service.CollegeService;
import cn.iocoder.yudao.module.school.service.MajorService;
import cn.iocoder.yudao.module.school.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 学校级联数据")
@RestController
@RequestMapping("/school/cascade")
@Validated
public class SchoolCascadeController {

    @Resource
    private CollegeService collegeService;
    @Resource
    private MajorService majorService;
    @Resource
    private ClassService classService;

    @GetMapping("/colleges")
    @Operation(summary = "获取所有学院（用于下拉选择）")
    @PreAuthorize("@ss.hasPermission('school:college:query')")
    public CommonResult<List<CollegeRespVO>> getCollegeList() {
        List<CollegeDO> list = collegeService.getCollegeList(null);
        return success(BeanUtils.toBean(list, CollegeRespVO.class));
    }

    @GetMapping("/majors")
    @Operation(summary = "根据学院ID获取专业列表（用于级联选择）")
    @PreAuthorize("@ss.hasPermission('school:major:query')")
    public CommonResult<List<MajorRespVO>> getMajorList(@RequestParam("collegeId") Long collegeId) {
        List<MajorDO> list = majorService.getMajorListByCollegeId(collegeId);
        return success(BeanUtils.toBean(list, MajorRespVO.class));
    }

    @GetMapping("/classes")
    @Operation(summary = "根据专业ID获取班级列表（用于级联选择）")
    @PreAuthorize("@ss.hasPermission('school:class:query')")
    public CommonResult<List<ClassRespVO>> getClassList(@RequestParam("majorId") Long majorId) {
        List<ClassDO> list = classService.getClassListByMajorId(majorId);
        return success(BeanUtils.toBean(list, ClassRespVO.class));
    }

}