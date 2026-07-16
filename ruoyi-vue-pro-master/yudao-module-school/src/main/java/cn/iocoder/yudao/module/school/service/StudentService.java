package cn.iocoder.yudao.module.school.service;

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

    StudentDO getStudentByUsername(String username);

}
