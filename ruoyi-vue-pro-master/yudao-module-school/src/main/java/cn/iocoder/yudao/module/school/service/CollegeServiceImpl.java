package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.college.CollegeSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.CollegeDO;
import cn.iocoder.yudao.module.school.dal.dataobject.MajorDO;
import cn.iocoder.yudao.module.school.dal.mysql.CollegeMapper;
import cn.iocoder.yudao.module.school.dal.mysql.MajorMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.COLLEGE_HAS_MAJORS;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.COLLEGE_NOT_EXISTS;

@Service
@Validated
public class CollegeServiceImpl implements CollegeService {

    @Resource
    private CollegeMapper collegeMapper;
    @Resource
    private MajorMapper majorMapper;

    @Override
    public Long createCollege(CollegeSaveReqVO createReqVO) {
        CollegeDO college = BeanUtils.toBean(createReqVO, CollegeDO.class);
        collegeMapper.insert(college);
        return college.getId();
    }

    @Override
    public void updateCollege(CollegeSaveReqVO updateReqVO) {
        validateCollegeExists(updateReqVO.getId());
        CollegeDO updateObj = BeanUtils.toBean(updateReqVO, CollegeDO.class);
        collegeMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCollege(Long id) {
        validateCollegeExists(id);
        if (majorMapper.selectCount(MajorDO::getCollegeId, id) > 0) {
            throw exception(COLLEGE_HAS_MAJORS);
        }
        collegeMapper.deleteById(id);
    }

    @Override
    public CollegeDO getCollege(Long id) {
        return collegeMapper.selectById(id);
    }

    @Override
    public List<CollegeDO> getCollegeList(CollegeListReqVO reqVO) {
        return collegeMapper.selectList(reqVO);
    }

    @Override
    public List<CollegeDO> getCollegeListByStatus(Integer status) {
        CollegeListReqVO reqVO = new CollegeListReqVO();
        reqVO.setStatus(status);
        return collegeMapper.selectList(reqVO);
    }

    private void validateCollegeExists(Long id) {
        if (id == null || collegeMapper.selectById(id) == null) {
            throw exception(COLLEGE_NOT_EXISTS);
        }
    }

}
