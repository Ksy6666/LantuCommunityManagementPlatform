export class Constants {
  static readonly BASE_URL: string = 'http://10.0.2.2:8081/api';
  static readonly TOKEN_KEY: string = 'auth_token';
  static readonly USER_INFO_KEY: string = 'user_info';
  static readonly TIMEOUT: number = 30000;
}

export enum UserRole {
  ADMIN = 1,
  TEACHER = 2,
  STUDENT = 3,
  USER = 4
}

export class RoleNames {
  static readonly ADMIN: string = '管理员';
  static readonly TEACHER: string = '教师';
  static readonly STUDENT: string = '学生';
  static readonly USER: string = '普通用户';
  
  static getRoleName(roleId: number): string {
    if (roleId === UserRole.ADMIN) {
      return RoleNames.ADMIN;
    } else if (roleId === UserRole.TEACHER) {
      return RoleNames.TEACHER;
    } else if (roleId === UserRole.STUDENT) {
      return RoleNames.STUDENT;
    } else {
      return RoleNames.USER;
    }
  }
}
