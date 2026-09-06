import { router } from '@kit.ArkUI';
import { AuthService } from '../common/api/Api';
import { PreferencesUtil } from '../common/utils/PreferencesUtil';
import { Constants } from '../common/constants/Constants';
import { UserRole } from '../common/constants/Constants';
import { LoginResponse } from '../model/User';

@Entry
@Component
struct LoginPage {
  @State account: string = '';
  @State password: string = '';
  @State isLoading: boolean = false;
  @State errorMessage: string = '';
  @State showPassword: boolean = false;

  build() {
    Column() {
      Column() {
        Image($r('app.media.log_lantu'))
          .width(80)
          .height(80)
          .borderRadius(16)
          .margin({ bottom: 20 })

        Text('蓝图管理后台')
          .fontSize(28)
          .fontWeight(FontWeight.Bold)
          .fontColor('#1a1a2e')
          .margin({ bottom: 6 })

        Text('欢迎回来，请登录您的账户')
          .fontSize(14)
          .fontColor('#8c8c8c')
          .margin({ bottom: 40 })
      }
      .margin({ top: 60, bottom: 32 })

      Column() {
        Column() {
          Text('账号')
            .fontSize(13)
            .fontColor('#8c8c8c')
            .margin({ bottom: 6 })

          TextInput({ placeholder: '请输入账号', text: this.account })
            .width('100%')
            .height(44)
            .fontSize(15)
            .padding({ left: 14, right: 14 })
            .backgroundColor('#f7f8fa')
            .borderRadius(8)
            .onChange((value: string) => {
              this.account = value;
              this.errorMessage = '';
            })
        }
        .width('100%')
        .alignItems(HorizontalAlign.Start)
        .margin({ bottom: 20 })

        Column() {
          Text('密码')
            .fontSize(13)
            .fontColor('#8c8c8c')
            .margin({ bottom: 6 })

          Row() {
            TextInput({ placeholder: '请输入密码', text: this.password })
              .layoutWeight(1)
              .height(44)
              .fontSize(15)
              .padding({ left: 14, right: 44 })
              .backgroundColor('#f7f8fa')
              .borderRadius(8)
              .type(this.showPassword ? InputType.Normal : InputType.Password)
              .onChange((value: string) => {
                this.password = value;
                this.errorMessage = '';
              })

            Text(this.showPassword ? '隐藏' : '显示')
              .fontSize(12)
              .fontColor('#4361ee')
              .position({ x: '100%', y: '50%' })
              .translate({ x: -48, y: '-50%' })
              .onClick(() => {
                this.showPassword = !this.showPassword;
              })
          }
          .width('100%')
          .height(44)
        }
        .width('100%')
        .alignItems(HorizontalAlign.Start)

        if (this.errorMessage) {
          Row() {
            Text('\u26A0')
              .fontSize(14)
              .fontColor('#e63946')
              .margin({ right: 6 })

            Text(this.errorMessage)
              .fontSize(13)
              .fontColor('#e63946')
          }
          .width('100%')
          .margin({ top: 16, bottom: 4 })
        }

        Button(this.isLoading ? '登录中...' : '登  录')
          .width('100%')
          .height(48)
          .fontSize(16)
          .fontWeight(FontWeight.Medium)
          .backgroundColor(this.isLoading ? '#bfc8e6' : '#4361ee')
          .borderRadius(10)
          .margin({ top: 28 })
          .enabled(!this.isLoading && this.account.length > 0 && this.password.length > 0)
          .onClick(() => {
            this.handleLogin();
          })
      }
      .width('100%')
      .padding({ left: 28, right: 28 })
    }
    .width('100%')
    .height('100%')
    .linearGradient({
      direction: GradientDirection.Bottom,
      colors: [['#eef2ff', 0.0], ['#ffffff', 0.5], ['#f0f4ff', 1.0]]
    })
    .padding({ left: 24, right: 24 })
  }

  private async handleLogin(): Promise<void> {
    if (this.account.length === 0 || this.password.length === 0) {
      this.errorMessage = '请输入账号和密码';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    try {
      console.info('开始登录，账号:', this.account);
      const response = await AuthService.login(this.account, this.password);
      console.info('登录响应:', JSON.stringify(response));

      if (response.code === 200 && response.data) {
        const loginData: LoginResponse = response.data;

        if (loginData.roleId !== UserRole.ADMIN) {
          this.errorMessage = '权限不足，仅限管理员登录';
          this.isLoading = false;
          return;
        }

        await PreferencesUtil.put(Constants.TOKEN_KEY, loginData.token);
        await PreferencesUtil.put(Constants.USER_INFO_KEY, loginData);

        router.replaceUrl({
          url: 'pages/MainPage'
        });
      } else {
        this.errorMessage = response.message || '登录失败，请检查账号密码';
      }
    } catch (error) {
      console.error('登录异常:', error);
      if (typeof error === 'string') {
        this.errorMessage = error;
      } else if (typeof error === 'object' && error !== null) {
        const errObj = error as Record<string, Object>;
        this.errorMessage = (errObj.message as string) || '网络错误，请稍后重试';
      } else {
        this.errorMessage = '网络错误，请稍后重试';
      }
    } finally {
      this.isLoading = false;
    }
  }
}
