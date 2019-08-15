#  轻量级网站动态构建平台（不知道算不算是FaaS）
* 用javascript 和 freemarker 语法做动态构建

* 插件化扩展（不同的实例之间可互为中心；这个很有意思的...）

* 支持热更新 和 热扩展，还有热开发

## 示例与交流
* QQ交流群：870505482

* Demo（先在上面玩玩看），每天会还原数据
   > http://tkt.noear.org/.admin/?_L0n5=1CE24B1CF36B0C5B94AACE6263DBD947FFA53531


##  部署指南（用/bin/下的文件）
0. 准备好 mysql（5.6+） 和 java（jdk8）运行环境
1. 在服务器上下载 /bin/tk.jar 到 /data/sss/tk.jar (目录可定义)
2. 使用脚本运行服务：java -jar /data/sss/tk.jar -server.port=8081 (端口可定义)
3. 打开网站：http://{网站域名}/
4. 根据提示进行安装设置 
5. 然后是安装一些扩展或者在上面开发（到此已完成部署）
* 建议把脚本转为.service文件后通过systemctl操控；或其它更好友的控制脚本

## 后续说明
1. 管理后台地址：http://{网站域名}/.admin/?_L0n5=1CE24B1CF36B0C5B94AACE6263DBD947FFA53531 
   > 后台地址 = http://{网站域名}/.admin/?_L0n5={token}
   
   > {token} = sha1({_frm_admin_user}+#+{_frm_admin_pwd})
2. 进入框架/配置界面，修改管理密码（需安装：框架插件）
   > _core/_frm_admin_pwd =管理密码 
3. 想把开发好的功能打包并分享，进框架/配置界面，开始开发者模式
   > _core/_frm_enable_dev = 1

## 附：安装界面
![](setup.png)
