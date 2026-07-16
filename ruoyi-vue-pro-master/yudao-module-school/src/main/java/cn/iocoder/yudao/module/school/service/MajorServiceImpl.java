package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.major.MajorSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.ClassDO;
import cn.iocoder.yudao.module.school.dal.dataobject.MajorDO;
import cn.iocoder.yudao.module.school.dal.mysql.ClassMapper;
import cn.iocoder.yudao.module.school.dal.mysql.CollegeMapper;
import cn.iocoder.yudao.module.school.dal.mysql.MajorMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.*;

@Service
@Validated
public class MajorServiceImpl implements MajorService {

    @Resource
    private MajorMapper majorMapper;
    @Resource
    private CollegeMapper collegeMapper;
    @Resource
    private ClassMapper classMapper;

    @Override
    public Long createMajor(MajorSaveReqVO createReqVO) {
        if (collegeMapper.selectById(createReqVO.getCollegeId()) == null) {
            throw exception(COLLEGE_NOT_EXISTS);
        }
        MajorDO major = BeanUtils.toBean(createReqVO, MajorDO.class);
        majorMapper.insert(major);
        return major.getId();
    }

    @Override
    public void updateMajor(MajorSaveReqVO updateReqVO) {
        validateMajorExists(updateReqVO.getId());
        if (collegeMapper.selectById(updateReqVO.getCollegeId()) == null) {
            throw exception(COLLEGE_NOT_EXISTS);
        }
        MajorDO updateObj = BeanUtils.toBean(updateReqVO, MajorDO.class);
        majorMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMajor(Long id) {
        validateMajorExists(id);
        if (classMapper.selectCount(ClassDO::getMajorId, id) > 0) {
            throw exception(MAJOR_HAS_CLASSES);
        }
        majorMapper.deleteById(id);
    }

    @Override
    public MajorDO getMajor(Long id) {
        return majorMapper.selectById(id);
    }

    @Override
    public List<MajorDO> getMajorList(MajorListReqVO reqVO) {
        return majorMapper.selectList(reqVO);
    }

    private void validateMajorExists(Long id) {
        if (id == null || majorMapper.selectById(id) == null) {
            throw exception(MAJOR_NOT_EXISTS);
        }
    }

}
