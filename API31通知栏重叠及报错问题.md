#版本：Android API 31(Android S)
+ 问题1:通知栏重叠，显示不完整.  
解决：build.gradle文件
     compileSdk 31改为28
     targetSdk 31改为25
     
+ 问题2:报错语句 PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, playIntent, 0);     
分析：（1）编译版本28不报错
     （2）编译版本31
       上述语句修改为以下判断语句. 
        PendingIntent playPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            playPendingIntent = PendingIntent.getActivity(context, 123, playIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            playPendingIntent = PendingIntent.getActivity(context, 123, playIntent, PendingIntent.FLAG_ONE_SHOT);
        }
