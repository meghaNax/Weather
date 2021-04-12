package com.app.weatherlive.ui.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import com.app.weatherlive.R
import com.app.weatherlive.ui.activity.WeatherInfoActivity


class LiveWeatherWidget : AppWidgetProvider() {

    private var mPendingIntent: PendingIntent? = null


    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWeatherWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWeatherWidget(context: Context,
                                    widgetManager: AppWidgetManager,
                                    widgetId: Int) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (mPendingIntent == null) {
            mPendingIntent = PendingIntent.getService(context, REQUEST_CODE, Intent(context, LiveWeatherService::class.java), PendingIntent.FLAG_CANCEL_CURRENT)
        }

        val views = RemoteViews(context.packageName, R.layout.widget_live_weather)
        val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, Intent(context, WeatherInfoActivity::class.java), 0)
        views.setOnClickPendingIntent(R.id.txtLiveWeather, pendingIntent)
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), INTERVAL, mPendingIntent)
        widgetManager.updateAppWidget(widgetId, views)
    }

    companion object{
        const val INTERVAL:Long = 60000
        const val REQUEST_CODE:Int = 0
    }
}