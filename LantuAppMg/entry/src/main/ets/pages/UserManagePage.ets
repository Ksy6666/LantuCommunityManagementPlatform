import { AdminService } from '../common/api/Api';
import { User } from '../model/User';
import { RoleNames, UserRole } from '../common/constants/Constants';
import { promptAction } from '@kit.ArkUI';

interface RoleItem {
  roleId: number;
  name: string;
}

@Component
export struct UserManagePage {
  @Link currentTab: number;
  @State users: User[] = [];
  @State isLoading: boolean = false;
  @State errorMessage: string = '';
  @State searchKeyword: string = '';
  @State currentPage: number = 1;
  @State pageSize: number = 20;
  @State totalUsers: number = 0;
  @State showDeleteDialog: boolean = false;
  @State selectedUser: User | null = null;
  @State showRoleDialog: boolean = false;
  @State @Watch('onTabChange') prevTab: number = 0;
  @State roleList: RoleItem[] = [
    { roleId: UserRole.ADMIN, name: RoleNames.ADMIN },
    { roleId: UserRole.TEACHER, name: RoleNames.TEACHER },
    { roleId: UserRole.STUDENT, name: RoleNames.STUDENT },
    { roleId: UserRole.USER, name: RoleNames.USER }
  ];

  aboutToAppear(): void {
    this.loadUsers();
  }

  onTabChange(): void {
    if (this.currentTab === 1 && this.prevTab !== 1) {
      this.loadUsers();
    }
    this.prevTab = this.currentTab;
  }

  build() {
    Column() {
      Row() {
        Row() {
          Text('\u{1F50D}')
            .fontSize(16)
            .margin({ left: 12, right: 8 })

          TextInput({ placeholder: '搜索用户名或账号', text: this.searchKeyword })
            .layoutWeight(1)
            .height(36)
            .fontSize(14)
            .backgroundColor(Color.Transparent)
            .onChange((value: string) => {
              this.searchKeyword = value;
            })
            .onSubmit(() => {
              this.currentPage = 1;
              this.loadUsers();
            })
        }
        .layoutWeight(1)
        .height(40)
        .backgroundColor(Color.White)
        .borderRadius(8)
        .alignItems(VerticalAlign.Center)

        Button('搜索')
          .width(72)
          .height(40)
          .fontSize(14)
          .backgroundColor('#4361ee')
          .borderRadius(8)
          .margin({ left: 12 })
          .onClick(() => {
            this.currentPage = 1;
            this.loadUsers();
          })
      }
      .width('100%')
      .padding({ left: 20, right: 20, top: 16, bottom: 12 })

      Row() {
        Text('共 ' + this.totalUsers + ' 位用户')
          .fontSize(13)
          .fontColor('#8c8c8c')

        Blank()

        Text('第 ' + this.currentPage + ' 页')
          .fontSize(13)
          .fontColor('#8c8c8c')
      }
      .width('100%')
      .padding({ left: 20, right: 20, bottom: 8 })

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
        .layoutWeight(1)
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
              this.loadUsers();
            })
        }
        .width('100%')
        .layoutWeight(1)
        .justifyContent(FlexAlign.Center)
      } else {
        List() {
          ForEach(this.users, (user: User) => {
            ListItem() {
              this.UserItem(user)
            }
          }, (user: User) => user.id.toString())
        }
        .width('100%')
        .layoutWeight(1)
        .padding({ left: 20, right: 20, top: 4, bottom: 4 })
        .scrollBar(BarState.Auto)

        Row() {
          Button('上一页')
            .height(36)
            .fontSize(13)
            .backgroundColor(Color.Transparent)
            .fontColor(this.currentPage > 1 ? '#4361ee' : '#d9d9d9')
            .borderWidth(1)
            .borderColor(this.currentPage > 1 ? '#4361ee' : '#d9d9d9')
            .borderRadius(6)
            .padding({ left: 16, right: 16 })
            .enabled(this.currentPage > 1)
            .onClick(() => {
              if (this.currentPage > 1) {
                this.currentPage--;
                this.loadUsers();
              }
            })

          Text('' + this.currentPage)
            .fontSize(14)
            .fontWeight(FontWeight.Medium)
            .fontColor('#1a1a2e')
            .margin({ left: 16, right: 16 })

          Button('下一页')
            .height(36)
            .fontSize(13)
            .backgroundColor(Color.Transparent)
            .fontColor(this.hasMorePages() ? '#4361ee' : '#d9d9d9')
            .borderWidth(1)
            .borderColor(this.hasMorePages() ? '#4361ee' : '#d9d9d9')
            .borderRadius(6)
            .padding({ left: 16, right: 16 })
            .enabled(this.hasMorePages())
            .onClick(() => {
              if (this.hasMorePages()) {
                this.currentPage++;
                this.loadUsers();
              }
            })
        }
        .width('100%')
        .justifyContent(FlexAlign.Center)
        .padding({ top: 12, bottom: 16 })
        .backgroundColor('#f0f2f5')
      }

      if (this.showDeleteDialog) {
        this.DeleteDialog()
      }

      if (this.showRoleDialog) {
        this.RoleDialog()
      }
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#f0f2f5')
  }

  @Builder
  UserItem(user: User): void {
    Row() {
      Stack() {
        Text((user.nickname || user.name).charAt(0))
          .fontSize(16)
          .fontWeight(FontWeight.Bold)
          .fontColor(Color.White)
      }
      .width(40)
      .height(40)
      .borderRadius(20)
      .backgroundColor(this.getAvatarColor(user.roleId))
      .margin({ right: 14 })

      Column() {
        Row() {
          Text(user.nickname || user.name)
            .fontSize(15)
            .fontWeight(FontWeight.Medium)
            .fontColor('#1a1a2e')

          Text(RoleNames.getRoleName(user.roleId))
            .fontSize(11)
            .fontColor(Color.White)
            .backgroundColor(this.getRoleColor(user.roleId))
            .padding({ left: 8, right: 8, top: 2, bottom: 2 })
            .borderRadius(4)
            .margin({ left: 8 })
        }
        .margin({ bottom: 4 })

        Row() {
          Text(user.account)
            .fontSize(12)
            .fontColor('#8c8c8c')
            .margin({ right: 16 })

          if (user.phone) {
            Text(user.phone)
              .fontSize(12)
              .fontColor('#8c8c8c')
          }
        }
      }
      .alignItems(HorizontalAlign.Start)
      .layoutWeight(1)

      Row() {
        Button('角色')
          .height(30)
          .fontSize(12)
          .backgroundColor('#e8ecff')
          .fontColor('#4361ee')
          .borderRadius(6)
          .padding({ left: 12, right: 12 })
          .onClick(() => {
            this.selectedUser = user;
            this.showRoleDialog = true;
          })

        Button('删除')
          .height(30)
          .fontSize(12)
          .backgroundColor('#ffe8ea')
          .fontColor('#e63946')
          .borderRadius(6)
          .padding({ left: 12, right: 12 })
          .margin({ left: 8 })
          .onClick(() => {
            this.selectedUser = user;
            this.showDeleteDialog = true;
          })
      }
    }
    .width('100%')
    .padding({ left: 16, right: 16, top: 14, bottom: 14 })
    .backgroundColor(Color.White)
    .borderRadius(10)
    .margin({ top: 6, bottom: 6 })
    .alignItems(VerticalAlign.Center)
  }

  @Builder
  DeleteDialog(): void {
    Column() {
      Column() {
        Stack({ alignContent: Alignment.Center }) {
          Text('\u{1F5D1}')
            .fontSize(22)
            .fontColor('#e63946')
        }
        .width(48)
        .height(48)
        .borderRadius(24)
        .backgroundColor('#ffe8ea')
        .margin({ bottom: 16 })

        Text('确认删除')
          .fontSize(18)
          .fontWeight(FontWeight.Bold)
          .fontColor('#1a1a2e')
          .margin({ bottom: 8 })

        Text('确定要删除该用户吗？此操作不可撤销。')
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
              this.showDeleteDialog = false;
              this.selectedUser = null;
            })

          Button('确认删除')
            .layoutWeight(1)
            .height(40)
            .fontSize(14)
            .backgroundColor('#e63946')
            .borderRadius(8)
            .margin({ left: 12 })
            .onClick(() => {
              this.handleDeleteUser();
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
      this.showDeleteDialog = false;
      this.selectedUser = null;
    })
  }

  @Builder
  RoleDialog(): void {
    Column() {
      Column() {
        Stack({ alignContent: Alignment.Center }) {
          Text('\u{1F464}')
            .fontSize(24)
            .fontColor('#4361ee')
        }
        .width(48)
        .height(48)
        .borderRadius(24)
        .backgroundColor('#e8ecff')
        .margin({ bottom: 16 })

        Text('修改角色')
          .fontSize(18)
          .fontWeight(FontWeight.Bold)
          .fontColor('#1a1a2e')
          .margin({ bottom: 8 })

        Text('为该用户分配新角色')
          .fontSize(14)
          .fontColor('#8c8c8c')
          .margin({ bottom: 20 })

        Column() {
          ForEach(this.roleList, (role: RoleItem) => {
            Row() {
              Stack({ alignContent: Alignment.Center }) {
                if (this.selectedUser?.roleId === role.roleId) {
                  Text('\u2713')
                    .fontSize(12)
                    .fontColor(Color.White)
                    .fontWeight(FontWeight.Bold)
                }
              }
              .width(20)
              .height(20)
              .borderRadius(10)
              .backgroundColor(this.selectedUser?.roleId === role.roleId ? '#4361ee' : '#f0f2f5')
              .margin({ right: 10 })

              Text(role.name)
                .fontSize(14)
                .fontColor(this.selectedUser?.roleId === role.roleId ? '#1a1a2e' : '#595959')
                .layoutWeight(1)
            }
            .width('100%')
            .height(44)
            .padding({ left: 12, right: 12 })
            .borderRadius(8)
            .backgroundColor(this.selectedUser?.roleId === role.roleId ? '#e8ecff' : Color.Transparent)
            .alignItems(VerticalAlign.Center)
            .margin({ bottom: 4 })
            .onClick(() => {
              this.handleUpdateRole(role.roleId);
            })
          }, (role: RoleItem) => role.roleId.toString())
        }
        .width('100%')
        .margin({ bottom: 16 })

        Button('关闭')
          .width('100%')
          .height(40)
          .fontSize(14)
          .backgroundColor('#f0f2f5')
          .fontColor('#595959')
          .borderRadius(8)
          .onClick(() => {
            this.showRoleDialog = false;
            this.selectedUser = null;
          })
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
      this.showRoleDialog = false;
      this.selectedUser = null;
    })
  }

  private getAvatarColor(roleId: number): string {
    switch (roleId) {
      case UserRole.ADMIN:
        return '#4361ee';
      case UserRole.TEACHER:
        return '#2ec4b6';
      case UserRole.STUDENT:
        return '#e68a00';
      default:
        return '#8c8c8c';
    }
  }

  private getRoleColor(roleId: number): string {
    switch (roleId) {
      case UserRole.ADMIN:
        return '#4361ee';
      case UserRole.TEACHER:
        return '#2ec4b6';
      case UserRole.STUDENT:
        return '#e68a00';
      default:
        return '#8c8c8c';
    }
  }

  private hasMorePages(): boolean {
    return this.currentPage * this.pageSize < this.totalUsers;
  }

  private async loadUsers(): Promise<void> {
    this.isLoading = true;
    this.errorMessage = '';

    try {
      const response = await AdminService.getUsers(this.currentPage, this.pageSize, this.searchKeyword);
      
      if (response.code === 200 && response.data) {
        this.users = response.data.list;
        this.totalUsers = response.data.total;
      } else {
        this.errorMessage = response.message || '加载失败';
      }
    } catch (error) {
      this.errorMessage = '网络错误，请稍后重试';
    } finally {
      this.isLoading = false;
    }
  }

  private async handleDeleteUser(): Promise<void> {
    if (!this.selectedUser) return;

    try {
      const response = await AdminService.deleteUser(this.selectedUser.id);
      
      if (response.code === 200) {
        promptAction.showToast({ message: '删除成功' });
        this.showDeleteDialog = false;
        this.selectedUser = null;
        this.loadUsers();
      } else {
        promptAction.showToast({ message: response.message || '删除失败' });
      }
    } catch (error) {
      promptAction.showToast({ message: '网络错误' });
    }
  }

  private async handleUpdateRole(roleId: number): Promise<void> {
    if (!this.selectedUser) return;

    try {
      const response = await AdminService.updateUserRole(this.selectedUser.id, roleId);
      
      if (response.code === 200) {
        promptAction.showToast({ message: '角色更新成功' });
        this.showRoleDialog = false;
        this.selectedUser = null;
        this.loadUsers();
      } else {
        promptAction.showToast({ message: response.message || '更新失败' });
      }
    } catch (error) {
      promptAction.showToast({ message: '网络错误' });
    }
  }
}
