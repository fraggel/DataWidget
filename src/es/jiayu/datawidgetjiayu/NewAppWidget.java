package es.jiayu.datawidgetjiayu;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    static double brilloMin=0.25;
    static double brilloMax=1.0;
    static boolean pulsado=false;
    static ImageButton imageButton;

    private static final String SYNC_CLICKED    = "DataJiayu";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        watchWidget = new ComponentName(context, NewAppWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.imageButton, getPendingSelfIntent(context, SYNC_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }
    @Override
    public void onReceive(Context context, Intent intent4) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent4);
        try {
            if (SYNC_CLICKED.equals(intent4.getAction())) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                RemoteViews remoteViews;
                ComponentName watchWidget;

                remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
                watchWidget = new ComponentName(context, NewAppWidget.class);

                Intent intent2 = new Intent(context, DummyDataActivity.class);
                //int brillo=Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                int brillo_actual=Settings.System.getInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS);
                SharedPreferences prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE);
                int max=Integer.parseInt(prefs.getString("maxBrillo","100"));
                int min=Integer.parseInt(prefs.getString("minBrillo","25"));

                brilloMax=Double.parseDouble(String.valueOf(max))/100;
                brilloMin=Double.parseDouble(String.valueOf(min))/100;
                Runtime r=Runtime.getRuntime();
                Process su = r.exec("su");
                OutputStream outputStream = su.getOutputStream();
                outputStream.write("am start -n \"com.mediatek.engineermode/com.mediatek.engineermode.networkselect.NetworkSelectActivity\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER".getBytes());
                outputStream.flush();
                outputStream.close();
                remoteViews.setOnClickPendingIntent(R.id.imageButton, getPendingSelfIntent(context, SYNC_CLICKED));
                appWidgetManager.updateAppWidget(watchWidget, remoteViews);

            }
        }catch(Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


	public void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {

		// Construct the RemoteViews object
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.new_app_widget);

       // Instruct the widget manager to update the widget
        views.setOnClickPendingIntent(R.id.imageButton, getPendingSelfIntent(context, SYNC_CLICKED));
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

}
