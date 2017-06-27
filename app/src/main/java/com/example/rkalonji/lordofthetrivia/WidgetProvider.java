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
        active.putExtra("target", ACTION_LAUNCH_CATEGORIES_FRAGMENT);
        active.setAction(ACTION_LAUNCH_CATEGORIES_FRAGMENT);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, active, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_launch_categories_grid, actionPendingIntent);

        active = new Intent(context, MainActivity.class);
        active.putExtra("target", ACTION_LAUNCH_SETS_FRAGMENT);
        active.setAction(ACTION_LAUNCH_SETS_FRAGMENT);
        actionPendingIntent = PendingIntent.getActivity(context, 0, active, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_launch_all_sets_grid, actionPendingIntent);

        active = new Intent(context, MainActivity.class);
        active.putExtra("target", ACTION_SCORES_FRAGMENT);
        active.setAction(ACTION_SCORES_FRAGMENT);
        actionPendingIntent = PendingIntent.getActivity(context, 0, active, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_launch_scores_grid, actionPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }
}
