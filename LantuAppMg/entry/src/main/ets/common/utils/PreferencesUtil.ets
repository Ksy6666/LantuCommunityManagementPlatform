import { preferences } from '@kit.ArkData';
import { Context } from '@kit.AbilityKit';

export class PreferencesUtil {
  private static preferencesStore: preferences.Preferences | null = null;
  private static readonly STORE_NAME: string = 'lantu_admin_store';

  static async init(context: Context): Promise<void> {
    try {
      PreferencesUtil.preferencesStore = await preferences.getPreferences(context, PreferencesUtil.STORE_NAME);
    } catch (error) {
      console.error('PreferencesUtil init failed:', error);
    }
  }

  static async put(key: string, value: string | number | boolean | object): Promise<void> {
    if (!PreferencesUtil.preferencesStore) {
      console.error('PreferencesStore not initialized');
      return;
    }
    
    try {
      if (typeof value === 'object') {
        await PreferencesUtil.preferencesStore.put(key, JSON.stringify(value));
      } else {
        await PreferencesUtil.preferencesStore.put(key, value);
      }
      await PreferencesUtil.preferencesStore.flush();
    } catch (error) {
      console.error('PreferencesUtil put failed:', error);
    }
  }

  static async get(key: string, defaultValue: string = ''): Promise<string> {
    if (!PreferencesUtil.preferencesStore) {
      console.error('PreferencesStore not initialized');
      return defaultValue;
    }
    
    try {
      const value = await PreferencesUtil.preferencesStore.get(key, defaultValue);
      return value as string;
    } catch (error) {
      console.error('PreferencesUtil get failed:', error);
      return defaultValue;
    }
  }

  static async getNumber(key: string, defaultValue: number = 0): Promise<number> {
    if (!PreferencesUtil.preferencesStore) {
      console.error('PreferencesStore not initialized');
      return defaultValue;
    }
    
    try {
      const value = await PreferencesUtil.preferencesStore.get(key, defaultValue);
      return value as number;
    } catch (error) {
      console.error('PreferencesUtil getNumber failed:', error);
      return defaultValue;
    }
  }

  static async getBoolean(key: string, defaultValue: boolean = false): Promise<boolean> {
    if (!PreferencesUtil.preferencesStore) {
      console.error('PreferencesStore not initialized');
      return defaultValue;
    }
    
    try {
      const value = await PreferencesUtil.preferencesStore.get(key, defaultValue);
      return value as boolean;
    } catch (error) {
      console.error('PreferencesUtil getBoolean failed:', error);
      return defaultValue;
    }
  }

  static async getObject<T>(key: string): Promise<T | null> {
    if (!PreferencesUtil.preferencesStore) {
      console.error('PreferencesStore not initialized');
      return null;
    }
    
    try {
      const value = await PreferencesUtil.preferencesStore.get(key, '');
      if (value && typeof value === 'string') {
        return JSON.parse(value) as T;
      }
      return null;
    } catch (error) {
      console.error('PreferencesUtil getObject failed:', error);
      return null;
    }
  }

  static async delete(key: string): Promise<void> {
    if (!PreferencesUtil.preferencesStore) {
      console.error('PreferencesStore not initialized');
      return;
    }
    
    try {
      await PreferencesUtil.preferencesStore.delete(key);
      await PreferencesUtil.preferencesStore.flush();
    } catch (error) {
      console.error('PreferencesUtil delete failed:', error);
    }
  }

  static async clear(): Promise<void> {
    if (!PreferencesUtil.preferencesStore) {
      console.error('PreferencesStore not initialized');
      return;
    }
    
    try {
      await PreferencesUtil.preferencesStore.clear();
      await PreferencesUtil.preferencesStore.flush();
    } catch (error) {
      console.error('PreferencesUtil clear failed:', error);
    }
  }
}
