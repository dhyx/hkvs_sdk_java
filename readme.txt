sprintboot
1 在海康官网下载对应的sdk   https://www.hikvision.com/Cn/download_61.html
   在这一步一定要下载和jdk一样的位数， 并不是和电脑位数一致的  比如我的jdk为32位 则下载windows 32位的sdk

2 在D盘新建文件夹比如D:\hk\hkv\
  把下载的sdk中的 【库文件】里的HCNetSDK.dll、HCCore.dll、HCNetSDKCom文件夹、PlayCtrl.dll、SuperRender.dll、AudioRender.dll、ssleay32.dll、libeay32.dll等文件均拷贝到这个文件夹下。

其中pom.xml中的examples包需要自行编译， 具体步骤可百度

运行util下的main方法即可

此项目为32位sdk, 可直接使用




64位sdk
 1 2 步一致
然后把sdk(你下载的sdk目录)\Demo示例\4- Java 开发示例\1-ClientDemo\ClientDemo-NetBeansPro\src\ClientDemo目录拷贝到项目目录下，可直接替换我的文件夹
修改ClientDemo\HCNetSDK.java36行  把路径改为D:\hk\hkv\HCNetSDK
64位没有具体试验过 只是理论 有问题可以随时沟通~