export interface DashboardData {
  totalUsers: number;
  todayCheckins: number;
  monthCheckins: number;
}

export interface CheckinStatus {
  totalPoints: number;
  consecutiveDays: number;
  todayChecked: boolean;
}

export interface RankingItem {
  userId: number;
  nickname: string;
  avatar: string;
  totalPoints: number;
  rank: number;
  roleName: string;
}
