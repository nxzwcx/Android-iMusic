package cn.edu.seu.cose.imusic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import cn.edu.seu.cose.imusic.databinding.ActivityMainBinding;
import cn.edu.seu.cose.imusic.ui.home.HomeFragment;
import cn.edu.seu.cose.imusic.ui.music.MusicFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Visualizer visualizer;
    private HomeFragment homeFragment;
    private MusicFragment musicFragment;

    /**
     * 请求读文件权限
     */
    private static final int REQUEST_READ_STORAGE = 0x01;
    /**
     * 请求读取文件
     */
    private static final int REQUEST_RECORD_AUDIO = 0X01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //运行时权限处理，动态申请WRITE_EXTERNAL_STORAGE权限
        //PackageManager.PERMISSION_GRANTED 表示有权限， PackageManager.PERMISSION_DENIED 表示无权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            //这里会调用后面的onRequestPermissionResult
        }else{
            //initMediaPlayer(0);
            Toast.makeText(this, "文件访问已授权！", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //initMediaPlayer(0);
                    Toast.makeText(this, "文件访问已授权！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
              default:
                break;
        }

        //cx add
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "RECORD_AUDIO已授权！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "RECORD_AUDIO未授权！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


}