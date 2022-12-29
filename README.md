# Android-iMusic
安卓音乐播放器仿千千静听支持歌词同步

## 【V2.0】  
Cursor扫描本地音频文件，过滤mp3，flac格式
MusicControl实现基础音乐播放
最小化、切换Fragment后仍可播放
缺点:切换Fragment不能及时释放资源

## 【V3.0】  
#PagerView实现歌曲列表、歌词显示 
  传统方案是Fragmentlayout+ FragmentManager实现
帧视图的隐藏、切换。但比较麻烦。几乎现在主流都采用PagerView方式实现页面视图切换，优势是支持手势滑动切换
#LrcView：歌词视图 https://github.com/wangchenyan/lrcview
  同目录lrc文件歌词同步

## 【V3.5】稳定版   
#MV，通过WebView实现  
#歌手列表， GridView实现  

## 【V4.0】 
#通知栏显示
   在MusicService调用NotificationUtil  
#通知栏内容更新
   广播方式更新通知，实现上一首，暂停/播放，下一首，退出  

#歌曲专辑封面显示   
   获取albumID，获取封面图片信息   
#fragment页面:歌手/专辑分类    
   Recyclerview+adapter实现    
【BUG】1、频谱不显示，暂停、播放时闪退
  
## 开发环境：Android Studio 64位  
- jdk:11
- compileSdk 32
- minSdk 25
- targetSdk 32
- Gradle版本: gradle-7.2-bin.zip


## 【待开发功能】  
#Service 音乐后台播放、控制  
#外来电话、声音，捕捉焦点并暂停播放   
#检测播放结束，自动下一曲  
+ 专辑旋转封面，仿网易云碟片  
    圆角图片处理
+ 音乐控制改为后台方式  
    ✓Service
+ 音频焦点实现外接电话等自动停止播放  
    ✓使用系统服务AudioManger
+ 耳机线控  
+ 按歌手，按专辑分类
    Cursor过滤
    HashSet遍历list存储专辑、歌手信息实现去重
+ 歌词匹配  
     优先匹配本地歌词，找不到则搜索网络歌词
      歌词API:歌词迷，网易云
       http/json获取解析
+ MV显示  
   暂时通过  
+ 千千静听其他功能  
   + 打开指定音乐文件
   + 音效均衡DSP
   + 一键换肤
   + ✓频谱
+ 音乐文件信息修改:专辑封面、歌手、风格等信息
+ 歌单自定义  
    创建歌单、增加歌曲到歌单
+ 通知栏音乐控件
    可能不同安卓版本、手机厂家小米欧珀显示不同
+ 锁屏插件
     提供锁屏时的播放控制
+ 歌单列表优化
  分享音乐、设为铃声、删除歌曲
  A-Z快捷显示，精确定位列表的位置
+ 歌单智能分类，根据每首音乐的风格标签，
   蓝调，爵士，古典，轻音乐，摇滚等自动分类。
+ 歌单自定义，讲想听的到到同一列表
+ 网易云、QQ歌单列表根据账号id获取
## 【高阶功能】
+ 局域网聊天模式
    同一wlan下识别在线人数，发送/接收聊天信息
    显示每个人正在听的歌曲，如xx在听:后海 
+ 在线音乐搜索、下载
   参考安卓MusicWorld无源码、洛雪音乐有源码
+ 歌星指数排行
   爬虫
+ 最新歌曲
   爬虫
+ 演唱会模式，虚拟人物演唱表现形式，洛天依等

## 【难点问题记录】
Fragment跳转后原内容仍显示，只能改为PagerView实现左右滑动切换
Fragment通过底下Tab切换后闪退，问题是该Fragment的
销毁函数一些释放引起。
（难点）### Fragment绑定Service未实现，百度查了一下应该是不可以。改为Activity与Service交互。这个问题折腾了好久。
利用static静态变量实现Activity与Fragment之间传递参数（继承各种监听事件，从而传递List<Listeners>)实现控制上一曲、下一曲。
      首先activity绑定service,初始化service的静态mediaplayer
      然后fragment修改mediaplayer上一曲、下一曲状态
参考： https://blog.csdn.net/weixin_34220834/article/details/88030672

###  通知栏不显示问题:Notifications
  还没找到原因，怀疑是安卓8以上参数问题
### 参考:
网易云音乐，PC版MusicTool，安卓MusicWorld   
Service教程：
https://www.cnblogs.com/yanglh6-jyx/p/Android_Service_MediaPlayer.html

### 广播接收传递service或者音乐对象，为空  
##  实现原理
+ 通知栏取消:manage.cancel(id)
+ 通知栏更新内容:安卓广播机制
注册广播Broadcast，设置action
方式一 静态注册 全局生效
方式二 动态注册 与activity生命周期一致
接听信息Receiver，intent通过action取数据
action名称设置问题，广播与接收得一致。还得检查是用的静态注册还是动态注册


###  频谱不显示问题

###  根据专辑id获取封面图片报错
+ 清单文件中声明audio_setting权限
+ 文件通过inputstream，参数uri生成
+ uri的目录只能放在特定audio目录下，否则会报错
