import { HttpUtil } from '../utils/HttpUtil';
import { ApiResponse } from '../../model/ApiResponse';
import { User, LoginRequest, LoginResponse, UserListResponse } from '../../model/User';
import { DashboardData, CheckinStatus, RankingItem } from '../../model/Dashboard';

interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}

interface UpdateRoleRequest {
  roleId: number;
}

export class AuthService {
  static async login(account: string, password: string): Promise<ApiResponse<LoginResponse>> {
    const loginReq: LoginRequest = { account: account, password: password };
    return await HttpUtil.post<LoginResponse>('/auth/login', loginReq, false);
  }

  static async updateProfile(user: Partial<User>): Promise<ApiResponse<User>> {
    return await HttpUtil.put<User>('/auth/profile', user);
  }

  static async changePasswordByOld(oldPassword: string, newPassword: string): Promise<ApiResponse<string>> {
    const req: ChangePasswordRequest = { oldPassword: oldPassword, newPassword: newPassword };
    return await HttpUtil.put<string>('/auth/password/old', req);
  }
}

export class AdminService {
  static async getDashboard(): Promise<ApiResponse<DashboardData>> {
    return await HttpUtil.get<DashboardData>('/admin/dashboard');
  }

  static async getUsers(page: number = 1, size: number = 20, keyword: string = ''): Promise<ApiResponse<UserListResponse>> {
    const url = `/admin/users?page=${page}&size=${size}&keyword=${encodeURIComponent(keyword)}`;
    return await HttpUtil.get<UserListResponse>(url);
  }

  static async getUserById(userId: number): Promise<ApiResponse<User>> {
    return await HttpUtil.get<User>(`/admin/users/${userId}`);
  }

  static async updateUserRole(userId: number, roleId: number): Promise<ApiResponse<string>> {
    const req: UpdateRoleRequest = { roleId: roleId };
    return await HttpUtil.put<string>(`/admin/users/${userId}/role`, req);
  }

  static async deleteUser(userId: number): Promise<ApiResponse<string>> {
    return await HttpUtil.delete<string>(`/admin/users/${userId}`);
  }
}

export class CheckinService {
  static async checkin(): Promise<ApiResponse<string>> {
    return await HttpUtil.post<string>('/checkin');
  }

  static async getCheckinStatus(): Promise<ApiResponse<CheckinStatus>> {
    return await HttpUtil.get<CheckinStatus>('/checkin/status');
  }

  static async getMonthCheckins(yearMonth: string): Promise<ApiResponse<number[]>> {
    return await HttpUtil.get<number[]>(`/checkin/month?yearMonth=${yearMonth}`);
  }

  static async getRanking(topN: number = 20): Promise<ApiResponse<RankingItem[]>> {
    return await HttpUtil.get<RankingItem[]>(`/checkin/ranking?topN=${topN}`, false);
  }

  static async getMyRanking(): Promise<ApiResponse<RankingItem>> {
    return await HttpUtil.get<RankingItem>('/checkin/ranking/me');
  }
}
