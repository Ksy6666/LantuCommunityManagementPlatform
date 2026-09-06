export interface User {
  id: number;
  account: string;
  name: string;
  nickname: string;
  phone: string;
  email: string;
  birthday: string;
  roleId: number;
  roleName: string;
  createdAt: string;
  updatedAt: string;
}

export interface LoginRequest {
  account: string;
  password: string;
}

export interface LoginResponse {
  id: number;
  account: string;
  name: string;
  nickname: string;
  phone: string;
  email: string;
  birthday: string;
  avatarUrl: string;
  roleId: number;
  roleName: string;
  token: string;
}

export interface UserListResponse {
  list: User[];
  total: number;
  page: number;
  size: number;
}
