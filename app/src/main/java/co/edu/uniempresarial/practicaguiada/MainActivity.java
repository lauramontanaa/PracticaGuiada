package co.edu.uniempresarial.practicaguiada;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int INTDEFAULT = -100;
    private Context context;
    private Activity activity;
    //android version
    private TextView versionAnd;
    private int vSDK;
    //bateria
    private ProgressBar pbLevelBatt;
    private TextView tvLevelBatt;
    IntentFilter battFilter;
    //camera
    CameraManager cameraManager;
    String cameraId;
    private Button btnOn;
    private Button btnOff;
    //conexion
    private TextView tvConexion;
    ConnectivityManager conexion;
    //archivos
    private EditText nameFile;
    private FileStorage fileStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicio a enlace de componentes
        objectInit();
        //botones flash
        this.btnOn.setOnClickListener(this::onLight);
        this.btnOff.setOnClickListener(this::offLight);
        //bateria
        battFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadReceiver, battFilter);
    }

    //chequeo de conexion
    private void checkConexion(){
        conexion = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conexion.getActiveNetworkInfo();
        boolean stateNet = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if(stateNet){
            tvConexion.setText("State ON");
        }else{
            tvConexion.setText("State OFF");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadReceiver);
    }

    BroadcastReceiver broadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int levelBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, INTDEFAULT);
            pbLevelBatt.setProgress(levelBattery);
            tvLevelBatt.setText("Level battery: "+levelBattery+"%");
        }
    }; //OBJETO ; y sobreescritura de metodo
    private void offLight(View view) {
        try{
            cameraManager.setTorchMode(cameraId,false);
        }catch (Exception e){
            Log.i("FLASH", e.getMessage());
        }
    }

    private void onLight(View view) {
        try {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId,true);
        } catch (Exception e){
            Log.i("FLASH", e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String versionSO = Build.VERSION.RELEASE;
        vSDK = Build.VERSION.SDK_INT;
        versionAnd.setText("Version SO: "+versionSO+" / SDK: "+vSDK);
        checkConexion();
    }

    private void objectInit(){
        this.context = getApplicationContext();
        this.activity = this;
        this.versionAnd = findViewById(R.id.tvVersionAndroid);
        this.pbLevelBatt = findViewById(R.id.pbLevelBattery);
        this.tvLevelBatt = findViewById(R.id.tvLevelBatteryLB);
        this.tvConexion = findViewById(R.id.tvConnection);
        this.btnOn = findViewById(R.id.btnOn);
        this.btnOff = findViewById(R.id.btnOff);
    }
}