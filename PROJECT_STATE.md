# Project State
# Last Updated: 2026-07-21

## Active Tasks
None

## Completed Tasks (2026-07-21)
- [x] T001: Fix CRITICAL - Staff default role is super admin | win-1 | done
  - Created `school-role-mapping.sql` with proper system roles (5001-5004)
  - Updated `StaffServiceImpl` to map school roles to system roles instead of hardcoding super admin (ID=1)
  - Teachers now get appropriate limited permissions instead of full admin access

- [x] T002: Fix CRITICAL - Course selection overselling race condition | win-1 | done
  - Added atomic `insertWithCapacityCheck()` method in `CourseSelectionMapper`
  - Updated `CourseServiceImpl.selectCourse()` to use atomic insert for elective courses
  - Prevents multiple concurrent requests from exceeding course capacity

- [x] T003: Fix CRITICAL - AppCourseController missing permission checks | win-1 | done
  - Added `@PreAuthorize("@ss.hasScope('member')")` to all AppCourseController endpoints
  - Updated `getCurrentStudentId()` to throw proper exceptions for unauthenticated users
  - Added parameter validation with `@NotNull` for courseId

- [x] T004: Fix CRITICAL - @Select bypasses tenant filter | win-1 | done
  - Replaced raw SQL `@Select("SELECT COUNT(DISTINCT student_id)...")` with `LambdaQueryWrapper` GROUP BY
  - MyBatis-Plus tenant interceptor now properly filters by tenant_id
  - Fixes cross-tenant data leak in statistics

- [x] T005: Fix CRITICAL - Data scope permissions incomplete | win-1 | done
  - Created `SchoolDataPermissionUtil` centralized permission checker
  - Updated `StudentController` to use utility for consistent permission logic
  - Fixed teachers (role=0) having unrestricted access - now they see empty lists
  - Added `DATA_PERMISSION_DENIED` error code
  - Added data permission checks to individual student operations (get/update/delete)

## Session Log
| TaskID | Owner | Status | Timestamp |
|--------|-------|--------|-----------|
| INIT   | setup | done   | 2026-07-15 |
| T001-T005 | win-1 | done   | 2026-07-21 |

## Next Steps
HIGH priority issues remaining (from Code Review):
- Fix Controller injecting Mapper directly (分层违规)
- Fix N+1 query in course list
- Add pagination to all list endpoints
- Fix ImportExcelVO zero validation
- Fix username uniqueness check incomplete
- Fix AppCourseController path conflict

MEDIUM and LOW priority issues can be addressed iteratively.