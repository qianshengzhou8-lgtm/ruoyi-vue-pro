package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.class_.ClassSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.ClassDO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import cn.iocoder.yudao.module.school.dal.mysql.ClassMapper;
import cn.iocoder.yudao.module.school.dal.mysql.MajorMapper;
import cn.iocoder.yudao.module.school.dal.mysql.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.school.enums.ErrorCodeConstants.*;

@Service
@Validated
@Slf4j
public class ClassServiceImpl implements ClassService {

    @Resource
    private ClassMapper classMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private StudentMapper studentMapper;

    @Override
    public Long createClass(ClassSaveReqVO createReqVO) {
        if (majorMapper.selectById(createReqVO.getMajorId()) == null) {
            throw exception(MAJOR_NOT_EXISTS);
        }
        ClassDO classDO = BeanUtils.toBean(createReqVO, ClassDO.class);
        classMapper.insert(classDO);
        return classDO.getId();
    }

    @Override
    public void updateClass(ClassSaveReqVO updateReqVO) {
        validateClassExists(updateReqVO.getId());
        if (majorMapper.selectById(updateReqVO.getMajorId()) == null) {
            throw exception(MAJOR_NOT_EXISTS);
        }
        ClassDO updateObj = BeanUtils.toBean(updateReqVO, ClassDO.class);
        classMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClass(Long id) {
        validateClassExists(id);
        if (studentMapper.selectCount(StudentDO::getClassId, id) > 0) {
            throw exception(CLASS_HAS_STUDENTS);
        }
        classMapper.deleteById(id);
    }

    @Override
    public ClassDO getClass(Long id) {
        return classMapper.selectById(id);
    }

    @Override
    public List<ClassDO> getClassList(ClassListReqVO reqVO) {
        return classMapper.selectList(reqVO);
    }

    @Override
    public List<ClassDO> getClassListByMajorId(Long majorId) {
        return classMapper.selectListByMajorId(majorId);
    }

    @Override
    public List<ClassDO> getClassListByCollegeId(ClassListReqVO reqVO, Long collegeId) {
        return classMapper.selectListByCollegeId(reqVO, collegeId);
    }

    @Override
    public ImportRespVO importClassList(List<ClassImportExcelVO> list) {
        ImportRespVO result = new ImportRespVO();
        for (ClassImportExcelVO vo : list) {
            try {
                ClassSaveReqVO saveVO = BeanUtils.toBean(vo, ClassSaveReqVO.class);
                createClass(saveVO);
                result.setCreateCount(result.getCreateCount() + 1);
            } catch (Exception e) {
                log.warn("[importClassList] 导入班级失败: name={}", vo.getName(), e);
                result.setFailureCount(result.getFailureCount() + 1);
                result.getFailureReasons().add(vo.getName() + ": " + e.getMessage());
            }
        }
        return result;
    }

    private void validateClassExists(Long id) {
        if (id == null || classMapper.selectById(id) == null) {
            throw exception(CLASS_NOT_EXISTS);
        }
    }

}
