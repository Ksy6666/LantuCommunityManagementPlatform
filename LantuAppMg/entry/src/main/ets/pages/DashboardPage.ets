import { AdminService } from '../common/api/Api';
import { DashboardData } from '../model/Dashboard';

interface StatCardParams {
  title: string;
  value: string;
  subtitle: string;
  gradientStart: string;
  gradientEnd: string;
  icon: string;
}

@Component
export struct DashboardPage {
  @Link currentTab: number;
  @State dashboard: DashboardData | null = null;
  @State isLoading: boolean = false;
  @State errorMessage: string = '';
  @State @Watch('onTabChange') prevTab: number = 0;

  aboutToAppear(): void {
    this.loadDashboard();
  }

  onTabChange(): void {
    if (this.currentTab === 0 && this.prevTab !== 0) {
      this.loadDashboard();
    }
    this.prevTab = this.currentTab;
  }

  build() {
    Scroll() {
      if (this.isLoading) {
        Column() {
          LoadingProgress()
            .width(48)
            .height(48)
            .color('#4361ee')

          Text('加载中...')
            .fontSize(14)
            .fontColor('#8c8c8c')
            .margin({ top: 16 })
        }
        .width('100%')
        .height('100%')
        .justifyContent(FlexAlign.Center)
      } else if (this.errorMessage) {
        Column() {
          Text('\u26A0')
            .fontSize(40)
            .fontColor('#e63946')
            .margin({ bottom: 16 })

          Text(this.errorMessage)
            .fontSize(16)
            .fontColor('#e63946')
            .margin({ bottom: 20 })

          Button('重新加载')
            .width(140)
            .height(40)
            .fontSize(14)
            .backgroundColor('#4361ee')
            .borderRadius(8)
            .onClick(() => {
              this.loadDashboard();
            })
        }
        .width('100%')
        .height('100%')
        .justifyContent(FlexAlign.Center)
      } else if (this.dashboard) {
        Column() {
          Row() {
            this.StatCard({
              title: '用户总数',
              value: this.dashboard.totalUsers.toString(),
              subtitle: '注册用户',
              gradientStart: '#4361ee',
              gradientEnd: '#7c8cf8',
              icon: '\u{1F465}'
            })

            this.StatCard({
              title: '今日签到',
              value: this.dashboard.todayCheckins.toString(),
              subtitle: '签到人数',
              gradientStart: '#2ec4b6',
              gradientEnd: '#6eddd2',
              icon: '\u2705'
            })

            this.StatCard({
              title: '本月签到',
              value: this.dashboard.monthCheckins.toString(),
              subtitle: '累计签到',
              gradientStart: '#e68a00',
              gradientEnd: '#f5b041',
              icon: '\u{1F4C5}'
            })
          }
          .width('100%')
          .justifyContent(FlexAlign.SpaceBetween)
          .margin({ bottom: 20 })

          Row() {
            Column() {
              Row() {
                Text('今日签到率')
                  .fontSize(16)
                  .fontWeight(FontWeight.Bold)
                  .fontColor('#1a1a2e')

                Blank()

                Text(this.getCheckinRate() + '%')
                  .fontSize(14)
                  .fontColor('#4361ee')
                  .fontWeight(FontWeight.Medium)
              }
              .width('100%')
              .margin({ bottom: 20 })

              Stack({ alignContent: Alignment.Center }) {
                Progress({
                  value: this.getCheckinRate(),
                  total: 100,
                  type: ProgressType.Ring
                })
                  .width(140)
                  .height(140)
                  .color('#4361ee')
                  .backgroundColor('#f0f2f5')
                  .style({ strokeWidth: 16 })

                Column() {
                  Text(this.getCheckinRate() + '%')
                    .fontSize(28)
                    .fontWeight(FontWeight.Bold)
                    .fontColor('#1a1a2e')

                  Text(this.dashboard.todayCheckins + '/' + this.dashboard.totalUsers)
                    .fontSize(12)
                    .fontColor('#8c8c8c')
                    .margin({ top: 2 })
                }
              }
              .width(140)
              .height(140)
            }
            .layoutWeight(1)
            .padding(20)
            .backgroundColor(Color.White)
            .borderRadius(12)
            .shadow({ radius: 6, color: '#0d000000', offsetX: 0, offsetY: 2 })

            Column() {
              Text('签到统计')
                .fontSize(16)
                .fontWeight(FontWeight.Bold)
                .fontColor('#1a1a2e')
                .width('100%')
                .margin({ bottom: 20 })

              Column() {
                Row() {
                  Column() {
                    Text(this.dashboard.todayCheckins.toString())
                      .fontSize(28)
                      .fontWeight(FontWeight.Bold)
                      .fontColor('#4361ee')

                    Text('今日')
                      .fontSize(12)
                      .fontColor('#8c8c8c')
                      .margin({ top: 4 })
                  }
                  .layoutWeight(1)
                  .alignItems(HorizontalAlign.Center)

                  Divider()
                    .vertical(true)
                    .height(50)
                    .color('#f0f0f0')

                  Column() {
                    Text(this.calculateDailyAvg())
                      .fontSize(28)
                      .fontWeight(FontWeight.Bold)
                      .fontColor('#2ec4b6')

                    Text('本月日均')
                      .fontSize(12)
                      .fontColor('#8c8c8c')
                      .margin({ top: 4 })
                  }
                  .layoutWeight(1)
                  .alignItems(HorizontalAlign.Center)
                }
                .width('100%')
              }
            }
            .layoutWeight(1)
            .padding(20)
            .backgroundColor(Color.White)
            .borderRadius(12)
            .shadow({ radius: 6, color: '#0d000000', offsetX: 0, offsetY: 2 })
            .margin({ left: 16 })
          }
          .width('100%')

          Column() {
            Row() {
              Text('数据概览')
                .fontSize(16)
                .fontWeight(FontWeight.Bold)
                .fontColor('#1a1a2e')

              Blank()

              Text('实时')
                .fontSize(12)
                .fontColor('#2ec4b6')
                .fontWeight(FontWeight.Medium)
            }
            .width('100%')
            .margin({ bottom: 16 })

            Row() {
              this.OverviewItem('总用户', this.dashboard.totalUsers.toString(), '#4361ee')
              this.OverviewItem('签到比例', this.getCheckinRate() + '%', '#2ec4b6')
              this.OverviewItem('月签到', this.dashboard.monthCheckins.toString(), '#e68a00')
              this.OverviewItem('日均', this.calculateDailyAvg(), '#8c8c8c')
            }
            .width('100%')
            .justifyContent(FlexAlign.SpaceBetween)
          }
          .width('100%')
          .padding(20)
          .backgroundColor(Color.White)
          .borderRadius(12)
          .shadow({ radius: 6, color: '#0d000000', offsetX: 0, offsetY: 2 })
          .margin({ top: 16 })
        }
        .width('100%')
        .padding(20)
      }
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#f0f2f5')
    .scrollBar(BarState.Off)
  }

  @Builder
  StatCard(params: StatCardParams): void {
    Column() {
      Row() {
        Stack({ alignContent: Alignment.Center }) {
          Text(params.icon)
            .fontSize(20)
        }
        .width(40)
        .height(40)
        .borderRadius(10)
        .linearGradient({
          direction: GradientDirection.Right,
          colors: [[params.gradientStart, 0.0], [params.gradientEnd, 1.0]]
        })

        Blank()
      }
      .width('100%')
      .margin({ bottom: 16 })

      Text(params.value)
        .fontSize(28)
        .fontWeight(FontWeight.Bold)
        .fontColor('#1a1a2e')
        .width('100%')

      Text(params.title)
        .fontSize(13)
        .fontColor('#8c8c8c')
        .margin({ top: 4 })
        .width('100%')
    }
    .width('32%')
    .padding(16)
    .backgroundColor(Color.White)
    .borderRadius(12)
    .shadow({ radius: 6, color: '#0d000000', offsetX: 0, offsetY: 2 })
  }

  @Builder
  OverviewItem(label: string, value: string, color: string): void {
    Column() {
      Text(value)
        .fontSize(20)
        .fontWeight(FontWeight.Bold)
        .fontColor(color)

      Text(label)
        .fontSize(12)
        .fontColor('#8c8c8c')
        .margin({ top: 4 })
    }
    .alignItems(HorizontalAlign.Center)
  }

  private getCheckinRate(): number {
    if (!this.dashboard || this.dashboard.totalUsers === 0) return 0;
    return Math.round(this.dashboard.todayCheckins / this.dashboard.totalUsers * 100);
  }

  private calculateDailyAvg(): string {
    if (!this.dashboard) return '0';
    const daysInMonth = new Date().getDate();
    const avg = daysInMonth > 0 ? (this.dashboard.monthCheckins / daysInMonth).toFixed(1) : '0';
    return avg;
  }

  private async loadDashboard(): Promise<void> {
    this.isLoading = true;
    this.errorMessage = '';

    try {
      const response = await AdminService.getDashboard();

      if (response.code === 200 && response.data) {
        this.dashboard = response.data;
      } else {
        this.errorMessage = response.message || '加载失败';
      }
    } catch (error) {
      this.errorMessage = '网络错误，请稍后重试';
    } finally {
      this.isLoading = false;
    }
  }
}
