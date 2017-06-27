package com.example.rkalonji.lordofthetrivia;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by rkalonji on 06/27/2017.
 */

public class WidgetProvider extends AppWidgetProvider {

    // our actions for our buttons
    public static String ACTION_LAUNCH_CATEGORIES_FRAGMENT = "ActionLaunchCategoriesFragment";
    public static String ACTION_LAUNCH_SETS_FRAGMENT = "ActionLaunchSetsFragment";
    public static String ACTION_SCORES_FRAGMENT = "ActionLaunchScoresFragment";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_lottr);

        Intent active = new Intent(context, MainActivity.class);
        active.setAction(ACTION_LAUNCH_CATEGORIES_FRAGMENT);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_launch_categories_grid, actionPendingIntent);

        active = new Intent(context, WidgetProvider.class);
        active.setAction(ACTION_LAUNCH_SETS_FRAGMENT);
        actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_launch_all_sets_grid, actionPendingIntent);

        active = new Intent(context, WidgetProvider.class);
        active.setAction(ACTION_SCORES_FRAGMENT);
        actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_launch_scores_grid, actionPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
/*        Intent configIntent = new Intent(context, MainActivity.class);

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_root,
                PendingIntent.readPendingIntentOrNullFromParcel(context,0, configIntent,0));
        // remoteViews.setOnClickPendingIntent(R.id.widget_root, configPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);*/
/*        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_lottr);
            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }*/
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_LAUNCH_CATEGORIES_FRAGMENT)) {
            intent.putExtra("target", ACTION_LAUNCH_CATEGORIES_FRAGMENT);
            context.startActivity(intent);
        } else if (intent.getAction().equals(ACTION_LAUNCH_SETS_FRAGMENT)) {
            intent.putExtra("target", ACTION_LAUNCH_SETS_FRAGMENT);
            context.startActivity(intent);
        } else if (intent.getAction().equals(ACTION_SCORES_FRAGMENT)) {
            intent.putExtra("target", ACTION_SCORES_FRAGMENT);
            context.startActivity(intent);
        } else {
            super.onReceive(context, intent);
        }
    }
}
