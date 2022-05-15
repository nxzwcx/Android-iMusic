package cn.edu.seu.cose.imusic.util;

//功能说明
//获取音乐文件的封面信息

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import cn.edu.seu.cose.imusic.R;

public class MusicUtil {

    //获取专辑封面的UI
    private static final String TAG="MusicUtil";
    private static final Uri albumArtUri=Uri.parse("content://media/external/_iMusic/album_img");

    //获取raw目录下音乐文件
    public static List<Music> getMediaFilesFromRaw(Fragment currentFragment)
    {
        List<Music> musicList=new ArrayList<>();
        //List<AssetFileDescriptor> _rawList=new ArrayList<>();;//raw中文件
        AssetFileDescriptor afd1 = currentFragment.getResources().openRawResourceFd(R.raw.doujiangyoutiao);
        AssetFileDescriptor afd2= currentFragment.getResources().openRawResourceFd(R.raw.ziyoufeixiang);
        AssetFileDescriptor afd3=currentFragment.getResources().openRawResourceFd(R.raw.hetangyese);
        AssetFileDescriptor afd4=currentFragment.getResources().openRawResourceFd(R.raw.xiaoqingge);
        AssetFileDescriptor afd5=currentFragment.getResources().openRawResourceFd(R.raw.guyongzhe);
        AssetFileDescriptor afd6=currentFragment.getResources().openRawResourceFd(R.raw.cuoweishikong);
        musicList.add(new Music("豆浆油条", "林俊杰", afd1,252*1000));
        musicList.add(new Music("自由飞翔", "凤凰传奇", afd2,256*1000));
        musicList.add(new Music("荷塘月色", "凤凰传奇", afd3,249*1000));
        musicList.add(new Music("小情歌", "苏打绿", afd4,273*1000));
        musicList.add(new Music("孤勇者", "陈奕迅", afd5,256*1000));
        musicList.add(new Music("错位时空", "NA", afd6,204*1000));
        return musicList;
    }

    public static List<Music> getLocalMedias(Fragment curentFragment){
        Context context=curentFragment.getContext();
        Cursor cursor = context.getContentResolver().query(
               MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
               // , null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        List<Music> musicList=new ArrayList<>();
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToNext();
            Music music=new Music();
            @SuppressLint("Range") String name = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
            @SuppressLint("Range") String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
            @SuppressLint("Range") long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            @SuppressLint("Range") String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
            @SuppressLint("Range") int album_id=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            @SuppressLint("Range") Long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));//艺人
            if(MusicFilter.isMusic(url)&&duration>=30*60) {
                music.setName(name);
                music.setUrl(url);
                music.setDuration(duration);
                music.setSinger(artist);
                music.setAlbumID(album_id);
                music.setFileSize(fileSize);
                Log.i("文件大小：" , fileSize.toString());
                musicList.add(music);
            }
        }
        return musicList;
    }

    //媒体准备音乐
    public static boolean initMediaPlayer(MediaPlayer mediaPlayer, Music music)
    {
        mediaPlayer.reset();

        switch(music.getFileType())
        {
            case localFile:
                try {
                    mediaPlayer.setDataSource(music.getUrl());
                    //mediaPlayer.prepare();
                    //异步方式
                    mediaPlayer.prepareAsync();
                    //mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                break;
            case rawFile:
                AssetFileDescriptor afd= music.getAfd();
                try {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    //mediaPlayer.prepare();
                    //异步方式
                    mediaPlayer.prepareAsync();
                    //调用音频的监听方法，音频准备完毕后响应该方法进行音乐播放
                    //mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                break;
            case networkFile:
                break;
        }

        //异步处理方式
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        return true;
    }

    //获取专辑图片，目前是只能获取手机自带歌曲的专辑图片，如果手机有酷狗，qq音乐之类的，可能无法获取专辑图片
    //因为他们的uri不知道。
    public Bitmap getArtwork(Context context, long song_id, long album_id, boolean allowdefalut, boolean small){
        if(album_id < 0) {
            if(song_id < 0) {
                Bitmap bm = getArtworkFromFile(context, song_id, -1);
                if(bm != null) {
                    return bm;
                }
            }
            if(allowdefalut) {
                return getDefaultArtwork(context, small);
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(albumArtUri, album_id);
        if(uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                //先制定原始大小
                options.inSampleSize = 1;
                //只进行大小判断
                options.inJustDecodeBounds = true;
                //调用此方法得到options得到图片的大小
                BitmapFactory.decodeStream(in, null, options);
                /** 我们的目标是在你N pixel的画面上显示。 所以需要调用computeSampleSize得到图片缩放的比例 **/
                /** 这里的target为800是根据默认专辑图片大小决定的，800只是测试数字但是试验后发现完美的结合 **/
                if(small){
                    options.inSampleSize = computeSampleSize(options, 40);
                } else{
                    options.inSampleSize = computeSampleSize(options, 600);
                }
                // 我们得到了缩放比例，现在开始正式读入Bitmap数据
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, options);
            } catch (FileNotFoundException e) {
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);
                if(bm != null) {
                    if(bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if(bm == null && allowdefalut) {
                            return getDefaultArtwork(context, small);
                        }
                    }
                } else if(allowdefalut) {
                    bm = getDefaultArtwork(context, small);
                }
                return bm;
            } finally {
                try {
                    if(in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 从文件当中获取专辑封面位图
     * @param context
     * @param songid
     * @param albumid
     * @return
     */
    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid){
        Bitmap bm = null;
        if(albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("---------------------"+TAG+"Must specify an album or a song id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if(albumid < 0){
                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(albumArtUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if(pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }
            options.inSampleSize = 1;
            // 只进行大小判断
            options.inJustDecodeBounds = true;
            // 调用此方法得到options得到图片大小
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            // 我们的目标是在800pixel的画面上显示
            // 所以需要调用computeSampleSize得到图片缩放的比例
            options.inSampleSize = 100;
            // 我们得到了缩放的比例，现在开始正式读入Bitmap数据
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            //根据options参数，减少所需要的内存
            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 获取默认专辑图片
     * @param context
     * @return
     */
    @SuppressLint("ResourceType")
    public static Bitmap getDefaultArtwork(Context context, boolean small) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        if(small){    //返回小图片
            //return
            BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.music), null, opts);
        }
        return BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.music), null, opts);
    }

    /**
     * 对图片进行合适的缩放
     * @param options
     * @param target
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int target) {
        int w = options.outWidth;
        int h = options.outHeight;
        int candidateW = w / target;
        int candidateH = h / target;
        int candidate = Math.max(candidateW, candidateH);
        if(candidate == 0) {
            return 1;
        }
        if(candidate > 1) {
            if((w > target) && (w / candidate) < target) {
                candidate -= 1;
            }
        }
        if(candidate > 1) {
            if((h > target) && (h / candidate) < target) {
                candidate -= 1;
            }
        }
        return candidate;
    }
}
