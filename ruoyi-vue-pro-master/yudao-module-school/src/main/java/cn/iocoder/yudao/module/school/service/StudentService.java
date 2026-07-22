package cn.iocoder.yudao.module.school.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.school.controller.admin.vo.ImportRespVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentImportExcelVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentListReqVO;
import cn.iocoder.yudao.module.school.controller.admin.vo.student.StudentSaveReqVO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;

import java.util.List;

public interface StudentService {

    Long createStudent(StudentSaveReqVO createReqVO);

    void updateStudent(StudentSaveReqVO updateReqVO);

    void deleteStudent(Long id);

    StudentDO getStudent(Long id);

    List<StudentDO> getStudentList(StudentListReqVO reqVO);

    List<StudentDO> getStudentListByCollegeId(StudentListReqVO reqVO, Long collegeId);

    PageResult<StudentDO> getStudentPage(StudentListReqVO reqVO);

    PageResult<StudentDO> getStudentPageByCollegeId(StudentListReqVO reqVO, Long collegeId);

    StudentDO getStudentByUsername(String username);

    /** 检查学生是否属于指定学院 */
    boolean hasStudentInCollege(Long studentId, Long collegeId);

    /** 导入学生列表 */
    ImportRespVO importStudentList(List<StudentImportExcelVO> list);

}
