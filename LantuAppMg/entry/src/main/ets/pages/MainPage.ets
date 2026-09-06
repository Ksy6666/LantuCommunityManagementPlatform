import { router } from '@kit.ArkUI';
import { PreferencesUtil } from '../common/utils/PreferencesUtil';
import { Constants } from '../common/constants/Constants';
import { User, LoginResponse } from '../model/User';
import { DashboardPage } from './DashboardPage';
import { UserManagePage } from './UserManagePage';
import { promptAction } from '@kit.ArkUI';

interface MenuItem {
  id: number;
  label: string;
  icon: string;
}

@Entry
@Component
struct MainPage {
  @State currentTab: number = 0;
  @State userInfo: LoginResponse | null = null;
  @State showLogoutDialog: boolean = false;
  @State menuExpanded: boolean = true;

  private menuItems: MenuItem[] = [
    { id: 0, label: '仪表盘', icon: '\u{1F4CA}' },
    { id: 1, label: '用户管理', icon: '\u{1F465}' }
  ];

  aboutToAppear(): Promise<void> {
    return this.loadUserInfo();
  }

  build() {
    Stack() {
      Row() {
        Column() {
          Row() {
            Image($r('app.media.log_lantu'))
              .width(32)
              .height(32)
              .borderRadius(8)

            if (this.menuExpanded) {
              Text('蓝图管理')
                .fontSize(16)
                .fontWeight(FontWeight.Bold)
                .fontColor(Color.White)
                .margin({ left: 10 })
            }
          }
          .width('100%')
          .height(56)
          .padding({ left: 16, right: 12 })
          .alignItems(VerticalAlign.Center)

          Divider()
            .color('#3d3d6b')
            .margin({ left: 12, right: 12, bottom: 8 })

          Column() {
            ForEach(this.menuItems, (item: MenuItem) => {
              Row() {
                Text(item.icon)
                  .fontSize(18)
                  .margin({ right: this.menuExpanded ? 8 : 0 })

                if (this.menuExpanded) {
                  Text(item.label)
                    .fontSize(14)
                    .fontColor(this.currentTab === item.id ? Color.White : '#a0a0c0')
                    .layoutWeight(1)
                }
              }
              .width('100%')
              .height(44)
              .padding({ left: 16, right: 12 })
              .borderRadius(8)
              .alignItems(VerticalAlign.Center)
              .backgroundColor(this.currentTab === item.id ? '#4361ee' : Color.Transparent)
              .margin({ bottom: 4 })
              .onClick(() => {
                this.currentTab = item.id;
              })
            }, (item: MenuItem) => item.id.toString())
          }
          .width('100%')
          .padding({ left: 8, right: 8 })

          Blank()

          Column() {
            Row() {
              Stack({ alignContent: Alignment.Center }) {
                Text((this.userInfo?.nickname || this.userInfo?.name || '管').charAt(0))
                  .fontSize(14)
                  .fontWeight(FontWeight.Bold)
                  .fontColor('#4361ee')
              }
              .width(32)
              .height(32)
              .borderRadius(16)
              .backgroundColor('#e0e4ff')

              if (this.menuExpanded) {
                Column() {
                  Text(this.userInfo?.nickname || this.userInfo?.name || '管理员')
                    .fontSize(13)
                    .fontWeight(FontWeight.Medium)
                    .fontColor(Color.White)
                    .maxLines(1)

                  Text('管理员')
                    .fontSize(11)
                    .fontColor('#a0a0c0')
                }
                .margin({ left: 10 })
                .layoutWeight(1)
                .alignItems(HorizontalAlign.Start)
              }
            }
            .width('100%')
            .padding({ left: 16, right: 12 })
            .margin({ bottom: 12 })

            Row() {
              Text('\u{1F6AA}')
                .fontSize(18)
                .margin({ right: this.menuExpanded ? 8 : 0 })

              if (this.menuExpanded) {
                Text('退出登录')
                  .fontSize(13)
                  .fontColor('#a0a0c0')
                  .margin({ left: 4 })
              }
            }
            .width('100%')
            .height(40)
            .padding({ left: 16, right: 12 })
            .borderRadius(8)
            .alignItems(VerticalAlign.Center)
            .onClick(() => {
              this.showLogoutDialog = true;
            })
          }
          .width('100%')
          .padding({ left: 8, right: 8, bottom: 16 })
        }
        .width(this.menuExpanded ? 200 : 64)
        .height('100%')
        .backgroundColor('#1a1a2e')

        Column() {
          Row() {
            Row() {
              Text('\u{2630}')
                .fontSize(18)
                .fontColor('#595959')
            }
            .width(36)
            .height(36)
            .borderRadius(8)
            .backgroundColor('#f0f2f5')
            .justifyContent(FlexAlign.Center)
            .alignItems(VerticalAlign.Center)
            .margin({ right: 16 })
            .onClick(() => {
              this.menuExpanded = !this.menuExpanded;
            })

            Text(this.menuItems[this.currentTab].label)
              .fontSize(18)
              .fontWeight(FontWeight.Bold)
              .fontColor('#1a1a2e')

            Blank()

            Row() {
              Stack({ alignContent: Alignment.Center }) {
                Text((this.userInfo?.nickname || this.userInfo?.name || '管').charAt(0))
                  .fontSize(13)
                  .fontWeight(FontWeight.Bold)
                  .fontColor('#4361ee')
              }
              .width(32)
              .height(32)
              .borderRadius(16)
              .backgroundColor('#e8ecff')

              Text(this.userInfo?.nickname || this.userInfo?.name || '管理员')
                .fontSize(14)
                .fontColor('#434343')
                .margin({ left: 8 })
            }
            .alignItems(VerticalAlign.Center)
          }
          .width('100%')
          .height(56)
          .padding({ left: 20, right: 20 })
          .backgroundColor(Color.White)
          .shadow({ radius: 2, color: '#0d000000', offsetX: 0, offsetY: 1 })

          if (this.currentTab === 0) {
            DashboardPage({ currentTab: $currentTab })
          } else {
            UserManagePage({ currentTab: $currentTab })
          }
        }
        .layoutWeight(1)
        .height('100%')
      }
      .width('100%')
      .height('100%')

      if (this.showLogoutDialog) {
        this.LogoutDialog()
      }
    }
    .width('100%')
    .height('100%')
  }

  @Builder
  LogoutDialog(): void {
    Column() {
      Column() {
        Text('确认退出')
          .fontSize(18)
          .fontWeight(FontWeight.Bold)
          .fontColor('#1a1a2e')
          .margin({ bottom: 12 })

        Text('确定要退出登录吗？退出后需重新登录才能使用。')
          .fontSize(14)
          .fontColor('#8c8c8c')
          .margin({ bottom: 24 })
          .textAlign(TextAlign.Center)

        Row() {
          Button('取消')
            .layoutWeight(1)
            .height(40)
            .fontSize(14)
            .backgroundColor('#f0f2f5')
            .fontColor('#595959')
            .borderRadius(8)
            .onClick(() => {
              this.showLogoutDialog = false;
            })

          Button('退出登录')
            .layoutWeight(1)
            .height(40)
            .fontSize(14)
            .backgroundColor('#e63946')
            .borderRadius(8)
            .margin({ left: 12 })
            .onClick(() => {
              this.handleLogout();
            })
        }
        .width('100%')
      }
      .width(300)
      .padding(24)
      .backgroundColor(Color.White)
      .borderRadius(16)
    }
    .width('100%')
    .height('100%')
    .backgroundColor('rgba(0, 0, 0, 0.45)')
    .justifyContent(FlexAlign.Center)
    .onClick(() => {
      this.showLogoutDialog = false;
    })
  }

  private async loadUserInfo(): Promise<void> {
    this.userInfo = await PreferencesUtil.getObject<LoginResponse>(Constants.USER_INFO_KEY);
  }

  private async handleLogout(): Promise<void> {
    try {
      await PreferencesUtil.delete(Constants.TOKEN_KEY);
      await PreferencesUtil.delete(Constants.USER_INFO_KEY);

      this.showLogoutDialog = false;

      router.replaceUrl({
        url: 'pages/LoginPage'
      });
    } catch (error) {
      promptAction.showToast({ message: '退出失败' });
    }
  }
}
