import { http } from '@kit.NetworkKit';
import { Constants } from '../constants/Constants';
import { ApiResponse } from '../../model/ApiResponse';
import { PreferencesUtil } from './PreferencesUtil';

export class HttpUtil {
  static async get<T>(url: string, needAuth: boolean = true): Promise<ApiResponse<T>> {
    return HttpUtil.request<T>(http.RequestMethod.GET, url, null, needAuth);
  }

  static async post<T>(url: string, data: object | null = null, needAuth: boolean = true): Promise<ApiResponse<T>> {
    return HttpUtil.request<T>(http.RequestMethod.POST, url, data, needAuth);
  }

  static async put<T>(url: string, data: object | null = null, needAuth: boolean = true): Promise<ApiResponse<T>> {
    return HttpUtil.request<T>(http.RequestMethod.PUT, url, data, needAuth);
  }

  static async delete<T>(url: string, needAuth: boolean = true): Promise<ApiResponse<T>> {
    return HttpUtil.request<T>(http.RequestMethod.DELETE, url, null, needAuth);
  }

  private static async request<T>(
    method: http.RequestMethod,
    url: string,
    data: object | null,
    needAuth: boolean
  ): Promise<ApiResponse<T>> {
    const fullUrl = Constants.BASE_URL + url;
    const token = await PreferencesUtil.get(Constants.TOKEN_KEY);

    const headers: Record<string, string> = {
      'Content-Type': 'application/json'
    };
    
    if (needAuth && token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const options: http.HttpRequestOptions = {
      method: method,
      header: headers,
      connectTimeout: Constants.TIMEOUT,
      readTimeout: Constants.TIMEOUT,
      expectDataType: http.HttpDataType.OBJECT
    };

    if (data && (method === http.RequestMethod.POST || method === http.RequestMethod.PUT)) {
      options.extraData = data;
    }

    const httpRequest = http.createHttp();
    try {
      const response = await httpRequest.request(fullUrl, options);
      
      if (response.responseCode === 401) {
        await PreferencesUtil.delete(Constants.TOKEN_KEY);
        await PreferencesUtil.delete(Constants.USER_INFO_KEY);
        const errorResponse: ApiResponse<T> = {
          code: 401,
          message: '登录已过期，请重新登录',
          data: null as T
        };
        return errorResponse;
      } else if (response.responseCode === 200 || response.responseCode === 201) {
        const result = response.result as ApiResponse<T>;
        return result;
      } else {
        const errorResponse: ApiResponse<T> = {
          code: response.responseCode,
          message: `请求失败: ${response.responseCode}`,
          data: null as T
        };
        return errorResponse;
      }
    } catch (error) {
      console.error('HTTP请求错误:', error);
      let errorMsg = '网络错误';
      
      if (error) {
        if (typeof error === 'string') {
          errorMsg = error;
        } else if (typeof error === 'object') {
          const errObj = error as Record<string, Object>;
          if (errObj.message) {
            errorMsg = errObj.message as string;
          } else if (errObj.code) {
            errorMsg = `错误码: ${errObj.code}`;
          } else {
            errorMsg = JSON.stringify(error);
          }
        }
      }
      
      const errorResponse: ApiResponse<T> = {
        code: -1,
        message: errorMsg,
        data: null as T
      };
      return errorResponse;
    } finally {
      httpRequest.destroy();
    }
  }
}
