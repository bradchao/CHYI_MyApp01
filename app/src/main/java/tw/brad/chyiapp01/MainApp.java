package tw.brad.chyiapp01;

import android.app.Application;
import android.content.SharedPreferences;

public class MainApp extends Application {
    static SharedPreferences sp;
    static SharedPreferences.Editor speditor;

    @Override
    public void onCreate() {
        super.onCreate();

        sp = getSharedPreferences("config", MODE_PRIVATE);  // read
        speditor = sp.edit();   // write

    }
}
