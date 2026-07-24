package cn.iocoder.yudao.module.school.util;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.school.dal.dataobject.StaffDO;
import cn.iocoder.yudao.module.school.dal.dataobject.StudentDO;
import cn.iocoder.yudao.module.school.dal.dataobject.ClassDO;
import cn.iocoder.yudao.module.school.dal.dataobject.MajorDO;
import cn.iocoder.yudao.module.school.dal.mysql.ClassMapper;
import cn.iocoder.yudao.module.school.dal.mysql.MajorMapper;
import cn.iocoder.yudao.module.school.dal.mysql.StaffMapper;
import cn.iocoder.yudao.module.school.dal.mysql.StudentMapper;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 学校数据权限工具类
 * 集中管理学校模块的数据权限过滤逻辑
 *
 * 使用 ThreadLocal 缓存教职工上下文，同一请求内多次调用只查一次数据库
 */
@Component
public class SchoolDataPermissionUtil {

    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private ClassMapper classMapper;

    @Getter
    @AllArgsConstructor
    private static class StaffContext {
        private final Integer role;
        private final Long collegeId;
        private final Long classId;
    }

    private final ThreadLocal<StaffContext> contextCache = new ThreadLocal<>();

    private StaffContext loadStaffContext() {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        if (loginUserId == null) {
            return new StaffContext(0, null, null);
        }
        AdminUserDO adminUser = adminUserMapper.selectById(loginUserId);
        if (adminUser == null) {
            return new StaffContext(0, null, null);
        }
        StaffDO staff = staffMapper.selectByUsername(adminUser.getUsername());
        if (staff == null || staff.getRole() == null) {
            // 非学校教职工（如系统管理员 admin），授予全部数据访问权限
            // 菜单权限由 @PreAuthorize 单独控制
            return new StaffContext(3, null, null);
        }
        return new StaffContext(staff.getRole(), staff.getCollegeId(), staff.getClassId());
    }

    private StaffContext getStaffContext() {
        StaffContext ctx = contextCache.get();
        if (ctx == null) {
            ctx = loadStaffContext();
            contextCache.set(ctx);
        }
        return ctx;
    }

    /**
     * 获取当前登录教职工的角色
     * @return 0=教师(无数据权限), 1=班主任(本班), 2=学院院长(本院), 3=校长(全校)
     *         非教职工（如系统管理员）返回 3，拥有全部数据权限
     */
    public Integer getCurrentStaffRole() {
        return getStaffContext().getRole();
    }

    /**
     * 获取当前登录教职工的学院ID
     */
    public Long getCurrentStaffCollegeId() {
        return getStaffContext().getCollegeId();
    }

    /**
     * 获取当前登录班主任的班级ID
     */
    public Long getCurrentStaffClassId() {
        return getStaffContext().getClassId();
    }

    /**
     * 检查是否有权限访问指定学院的数据
     */
    public boolean hasAccessToCollege(Long collegeId) {
        Integer role = getCurrentStaffRole();
        if (role == null || role == 0) return false;
        if (role == 3) return true;
        if (role == 2) {
            Long currentCollegeId = getCurrentStaffCollegeId();
            return currentCollegeId != null && currentCollegeId.equals(collegeId);
        }
        return false;
    }

    /**
     * 检查是否有权限访问指定班级的数据
     */
    public boolean hasAccessToClass(Long classId) {
        Integer role = getCurrentStaffRole();
        if (role == null || role == 0) return false;
        if (role == 3) return true;
        if (role == 2) {
            Long currentCollegeId = getCurrentStaffCollegeId();
            if (currentCollegeId == null) return false;
            Long classCollegeId = getCollegeIdByClassId(classId);
            return classCollegeId != null && classCollegeId.equals(currentCollegeId);
        }
        if (role == 1) {
            Long currentClassId = getCurrentStaffClassId();
            return currentClassId != null && currentClassId.equals(classId);
        }
        return false;
    }

    /**
     * 根据班级ID查询所属学院ID
     * @param classId 班级ID
     * @return 学院ID，如果找不到返回 null
     */
    public Long getCollegeIdByClassId(Long classId) {
        if (classId == null) return null;
        ClassDO classDO = classMapper.selectById(classId);
        if (classDO == null) return null;
        MajorDO majorDO = majorMapper.selectById(classDO.getMajorId());
        if (majorDO == null) return null;
        return majorDO.getCollegeId();
    }

    /**
     * 检查当前用户是否为超级管理员或校长（拥有全部权限）
     */
    public boolean hasFullAccess() {
        Integer role = getCurrentStaffRole();
        return role != null && role == 3;
    }

    /**
     * 检查当前用户是否有权限访问指定学生
     * @param studentId 学生ID
     * @return 是否有权限
     */
    public boolean hasAccessToStudent(Long studentId) {
        if (studentId == null) {
            return false;
        }

        Integer role = getCurrentStaffRole();
        if (role == null || role == 0) {
            return false;
        }
        if (role == 3) {
            return true;
        }

        StudentDO student = studentMapper.selectById(studentId);
        if (student == null) {
            return false;
        }

        if (role == 2) {
            Long currentCollegeId = getCurrentStaffCollegeId();
            if (currentCollegeId == null) {
                return false;
            }
            Long studentCollegeId = getCollegeIdByClassId(student.getClassId());
            return studentCollegeId != null && studentCollegeId.equals(currentCollegeId);
        }

        if (role == 1) {
            Long currentClassId = getCurrentStaffClassId();
            return currentClassId != null && currentClassId.equals(student.getClassId());
        }

        return false;
    }

}
