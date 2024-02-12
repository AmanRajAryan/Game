package aman.three;

import android.os.Bundle;

import android.view.WindowManager;
import android.widget.Toast;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import aman.three.MyGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new CaptureCrash(getApplicationContext()));
		super.onCreate(savedInstanceState);
        getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGame(), config);
        
	}
    
    public void nakeToast(String messafe) {
    	Toast.makeText(getApplicationContext() , messafe , 0).show();
    }
}
