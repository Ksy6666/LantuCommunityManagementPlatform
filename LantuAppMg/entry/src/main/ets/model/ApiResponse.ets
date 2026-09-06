export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

export class ApiResponseHelper {
  static isSuccess<T>(response: ApiResponse<T>): boolean {
    return response.code === 200;
  }
  
  static getMessage<T>(response: ApiResponse<T>): string {
    return response.message;
  }
  
  static getData<T>(response: ApiResponse<T>): T | null {
    if (ApiResponseHelper.isSuccess(response)) {
      return response.data;
    }
    return null;
  }
}
