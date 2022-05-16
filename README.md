# Android-iMusic
安卓音乐播放器仿千千静听支持歌词同步

【V2.0】  
Cursor扫描本地音频文件，过滤mp3，flac格式
MusicControl实现基础音乐播放
最小化、切换Fragment后仍可播放
缺点:切换Fragment不能及时释放资源

【V3.0】  
#PagerView实现歌曲列表、歌词显示    
#LrcView：歌词视图 https://github.com/wangchenyan/lrcview
  同目录lrc文件歌词同步

【V4.0】   
#Service 音乐后台播放、控制  
#外来电话、声音，捕捉焦点并暂停播放  
#检测播放结束，自动下一曲

#MV ，通过WebView实现
#歌手列表， GridView实现


## 开发环境：Android Studio 64位
compileSdk 32
minSdk 25
targetSdk 32

Gradle版本:distributionUrl=https\://services.gradle.org/distributions/gradle-7.2-bin.zip


##【待开发功能】  
专辑旋转封面，仿网易云碟片  
    圆角图片处理
音乐控制改为后台方式  
    Service
音频焦点实现外接电话等自动停止播放  
   使用系统服务AudioManger
按歌手，按专辑分类
    Cursor过滤
歌词匹配  
    优先匹配本地歌词，找不到则搜索网络歌词
    歌词API:歌词迷，网易云
    http/json获取解析
MV显示  
   暂时通过
千千静听其他功能  
   打开指定音乐文件
   音效均衡DSP
   换肤
音乐文件信息修改:专辑封面、歌手、风格等信息
歌单自定义  
    创建歌单、增加歌曲到歌单
通知栏音乐控件
    可能不同安卓版本、手机厂家小米欧珀显示不同

高阶功能
在线音乐搜索、下载
   参考安卓MusicWorld无源码、洛雪音乐有源码
歌星指数排行
   爬虫
最新歌曲
   爬虫

##【难点问题记录】
Fragment跳转后原内容仍显示，只能改为PagerView实现左右滑动切换
Fragment通过底下Tab切换后闪退，问题是该Fragment的
销毁函数一些释放引起。


##  参考:
网易云音乐，PC版MusicTool，安卓MusicWorld
