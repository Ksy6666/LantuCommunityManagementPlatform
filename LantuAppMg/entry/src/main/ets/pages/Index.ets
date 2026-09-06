import { router } from '@kit.ArkUI';
import { PreferencesUtil } from '../common/utils/PreferencesUtil';
import { Constants } from '../common/constants/Constants';

@Entry
@Component
struct Index {
  aboutToAppear(): Promise<void> {
    return this.checkLogin();
  }

  build() {
    Column() {
      Image($r('app.media.log_lantu'))
        .width(72)
        .height(72)
        .borderRadius(14)
        .margin({ bottom: 20 })

      Text('蓝图管理')
        .fontSize(22)
        .fontWeight(FontWeight.Bold)
        .fontColor('#1a1a2e')
        .margin({ bottom: 16 })

      LoadingProgress()
        .width(36)
        .height(36)
        .color('#4361ee')
    }
    .width('100%')
    .height('100%')
    .justifyContent(FlexAlign.Center)
    .backgroundColor('#f0f4ff')
  }

  private async checkLogin(): Promise<void> {
    try {
      const token = await PreferencesUtil.get(Constants.TOKEN_KEY);

      if (token && token.length > 0) {
        router.replaceUrl({
          url: 'pages/MainPage'
        });
      } else {
        router.replaceUrl({
          url: 'pages/LoginPage'
        });
      }
    } catch (error) {
      router.replaceUrl({
        url: 'pages/LoginPage'
      });
    }
  }
}
