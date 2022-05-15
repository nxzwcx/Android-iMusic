//https://blog.csdn.net/csdn_lg_one/article/details/115705860
//通过Visualizer 函数对接了MediaPlayer，实现频谱特效
package cn.edu.seu.cose.imusic.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import cn.edu.seu.cose.imusic.R;

/**
 * author by LiuGuo
 * on 2021/4/13
 * 自定义组件：音乐频谱显示组件
 * API-> setMediaPlayer()  外层设置路径 播放之后 自动显示频谱效果
 */
public class MusicFrequencyView extends View {
    private int widthsize;
    private int heightsize;
    private Paint paint;
    int count = -1;
    float mCount = 0;
    int count1 = -1;
    float mCount1 = 0;
    int count2 = -1;
    float mCount2 = 0;
    int count3 = -1;
    float mCount3 = 0;
    int count4 = -1;
    float mCount4 = 0;
    int count5 = -1;
    float mCount5 =0;
    boolean isUp = false;
    boolean isUp1 = false;
    boolean isUp2 = false;
    boolean isUp3 = false;
    boolean isUp4 = false;
    boolean isUp5 = false;
    int[] voiceData = new int[6];
    private Visualizer visualizer;
    private int currentFrequency;
    private int mCurrentFrequency;
    private int currentVolume;
    private final int itemColor;
    private Paint paint1;
    private float mi;

    public MusicFrequencyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.musicView);
        itemColor = ta.getColor(R.styleable.musicView_itemColor, Color.parseColor("#000000"));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthsize = MeasureSpec.getSize(widthMeasureSpec);
        heightsize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthsize, heightsize);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Log.e("onDrawonDraw", "" + heightsize + "==" + widthsize);

        mCount = heightsize / 1.04895f - count;
        RectF rectF = new RectF(widthsize / 5.8f, mCount, widthsize / 4.46154f, heightsize / 1.04895f);

        mCount1 = heightsize / 1.04895f - count1;
        RectF rectF1 = new RectF(widthsize / 3.41176f, mCount1, widthsize / 2.9f, heightsize / 1.04895f);

        mCount2 = heightsize / 1.04895f - count2;
        RectF rectF2 = new RectF(widthsize / 2.4166667f, mCount2, widthsize / 2.148148f, heightsize / 1.04895f);

        mCount3 = heightsize / 1.04895f - count3;
        RectF rectF3 = new RectF(widthsize / 1.870968f, mCount3, widthsize / 1.705882f, heightsize / 1.04895f);

        mCount4 = heightsize / 1.04895f - count4;
        RectF rectF4 = new RectF(widthsize / 1.526316f, mCount4, widthsize / 1.414634f, heightsize / 1.04895f);

        mCount5 = heightsize / 1.04895f - count5;
        RectF rectF5 = new RectF(widthsize / 1.28889f, mCount5, widthsize / 1.20833f, heightsize / 1.04895f);

        canvas.drawRect(rectF, paint);
        canvas.drawRect(rectF1, paint);
        canvas.drawRect(rectF2, paint);
        canvas.drawRect(rectF3, paint);
        canvas.drawRect(rectF4, paint);
        canvas.drawRect(rectF5, paint);
        canvas.drawLine(widthsize / 11.6f, heightsize / 1.04895f, widthsize / 1.09434f, heightsize / 1.04895f, paint);


        int[] ints = todoCount(count, voiceData[0], isUp);
        count = ints[0];
        isUp = ints[1] == 1 ? true : false;

        int[] ints1 = todoCount(count1, voiceData[1], isUp1);
        count1 = ints1[0];
        isUp1 = ints1[1] == 1 ? true : false;

        int[] ints2 = todoCount(count2, voiceData[2], isUp2);
        count2 = ints2[0];
        isUp2 = ints2[1] == 1 ? true : false;

        int[] ints3 = todoCount(count3, voiceData[3], isUp3);
        count3 = ints3[0];
        isUp3 = ints3[1] == 1 ? true : false;

        int[] ints4 = todoCount(count4, voiceData[4], isUp4);
        count4 = ints4[0];
        isUp4 = ints4[1] == 1 ? true : false;

        int[] ints5 = todoCount(count5, voiceData[5], isUp5);
        count5 = ints5[0];
        isUp5 = ints5[1] == 1 ? true : false;


    }


    /**
     * 判断count
     *
     * @param count
     * @param voiceDatum
     * @param isUp
     */
    private int[] todoCount(int count, int voiceDatum, boolean isUp) {

        if (count >= 0) {
            if (count < voiceDatum) {
                if (isUp) {
                    count -= 10;
                } else {
                    count += 50;
                    isUp = false;
                }
            }
            if (count == voiceDatum) {
                isUp = true;
                count -= 1;
            }
            invalidate();
        }

        int[] ints = new int[2];
        ints[0] = count;
        ints[1] = isUp ? 1 : 0;
        return ints;
    }


    /**
     * 只需传入 mediaPlayer 即可
     *
     * @param mediaPlayer
     */
    public void setMediaPlayer(final MediaPlayer mediaPlayer) {
        //Error code -3 when initializing Visualizer.
        //if(mediaPlayer.isPlaying()==false) return;
        visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                //Log.i("xiaozhu", "waveform" + waveform.length);
                long v = 0;
                for (int i = 0; i < waveform.length; i++) {
                    v += Math.pow(waveform[i], 2);
                }
                double volume = 10 * Math.log10(v / (double) waveform.length);
                currentVolume = (int) volume;
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                if (mediaPlayer == null || !mediaPlayer.isPlaying()) return;
                mi++;
                if (mi % 1 != 0) { //延缓频率
                    return;
                }

                float[] magnitudes = new float[fft.length / 2];
                int max = 0;
                for (int i = 0; i < magnitudes.length; i++) {
                    magnitudes[i] = (float) Math.hypot(fft[2 * i], fft[2 * i + 1]);
                    if (magnitudes[max] < magnitudes[i]) {
                        max = i;
                    }
                }
                currentFrequency = max * samplingRate / fft.length;
                if (currentFrequency / 28000 > 28) {
                    currentFrequency = 28;
                } else {
                    currentFrequency = currentFrequency / 28000;
                }

                int[] bv = new int[6];
                for (int j = 0; j < 6; j++) {
                    if (j == 0 || j == 1) {
                        bv[j] = currentFrequency += 1;
                    }

                    if (j == 2 || j == 3) {
                        bv[j] = currentFrequency -= 3;
                    }

                    if (j == 4 || j == 5) {
                        bv[j] = currentFrequency -= 1;
                    }

                    if (currentFrequency < 3)
                        currentFrequency = 0;
                    bv[j] = currentFrequency;
                }
                setMusicData(bv);
                //Log.i("xiaozhu", "currentFrequency=" + currentFrequency + "=====" + currentFrequency);

            }
        }, Visualizer.getMaxCaptureRate() / 2, true, true);

        visualizer.setEnabled(true);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

    }


    /**
     * 设置频率数据
     *
     * @param data
     */
    public void setMusicData(int[] data) {

        if (count < 0) {
            voiceData[0] = data[0] * 10;
            if (voiceData[0] > 0) {
                voiceData[0] = voiceData[0] + 50 - voiceData[0] % 50; //补全50的倍数 向上取整50
            } else {
                voiceData[0] = 0; //补全50的倍数 向上取整50
            }
            count = 0;
            isUp = false;
            invalidate();
        }

        if (count1 < 0) {
            voiceData[1] = data[1] * 10;
            if (voiceData[1] > 0) {
                voiceData[1] = voiceData[1] + 50 - voiceData[1] % 50; //补全50的倍数 向上取整50
            } else {
                voiceData[1] = 0; //补全50的倍数 向上取整50
            }
            count1 = 0;
            isUp1 = false;
            invalidate();
        }

        if (count2 < 0) {
            voiceData[2] = data[2] * 10;
            if (voiceData[2] > 0) {
                voiceData[2] = voiceData[2] + 50 - voiceData[2] % 50; //补全50的倍数 向上取整50
            } else {
                voiceData[2] = 0;
            }
            count2 = 0;
            isUp2 = false;
            invalidate();
        }

        if (count3 < 0) {
            voiceData[3] = data[3] * 10;
            if (voiceData[3] > 0) {
                voiceData[3] = voiceData[3] + 50 - voiceData[3] % 50; //补全50的倍数 向上取整50
            } else {
                voiceData[3] = 0;
            }
            count3 = 0;
            isUp3 = false;
            invalidate();
        }

        if (count4 < 0) {
            voiceData[4] = data[4] * 10;
            if (voiceData[4] > 0) {
                voiceData[4] = voiceData[4] + 50 - voiceData[4] % 50; //补全50的倍数 向上取整50
            } else {
                voiceData[4] = 0;
            }
            count4 = 0;
            isUp4 = false;
            invalidate();
        }

        if (count5 < 0) {
            voiceData[5] = data[5] * 10;
            if (voiceData[5] > 0) {
                voiceData[5] = voiceData[5] + 50 - voiceData[5] % 50; //补全50的倍数 向上取整50
            } else {
                voiceData[5] = 0;
            }
            count5 = 0;
            isUp5 = false;
            invalidate();
        }
    }

    /**
     * 初始化画笔
     */
    void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null); //部分手机不显示阴影效果 配合setShadowLayer
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(itemColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setFilterBitmap(true);
        paint.setShadowLayer(2, 9, 5, Color.parseColor("#55000000"));
        paint.setStrokeWidth(heightsize / 100f);


        paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setColor(itemColor);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(heightsize / 100f);


    }
}
