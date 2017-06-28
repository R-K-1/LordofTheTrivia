package com.example.rkalonji.lordofthetrivia;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    public static final String ACTION_VIEW_SCORE = "ActionViewScore";
    public static final String EXTRA_SELECTED_SCORE = "ExtraSelectedScore";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_lottr);

        Intent active = new Intent(context, MainActivity.class);
        active.setAction(ACTION_LAUNCH_CATEGORIES_FRAGMENT);
        active.putExtra("target", ACTION_LAUNCH_CATEGORIES_FRAGMENT);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, active, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_launch_categories_grid, actionPendingIntent);

        active = new Intent(context, MainActivity.class);
        active.setAction(ACTION_LAUNCH_SETS_FRAGMENT);
        active.putExtra("target", ACTION_LAUNCH_SETS_FRAGMENT);
        actionPendingIntent = PendingIntent.getActivity(context, 0, active, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_launch_all_sets_grid, actionPendingIntent);

        active = new Intent(context, MainActivity.class);
        active.setAction(ACTION_SCORES_FRAGMENT);
        active.putExtra("target", ACTION_SCORES_FRAGMENT);
        actionPendingIntent = PendingIntent.getActivity(context, 0, active, PendingIntent.FLAG_UPDATE_CURRENT);
        // remoteViews.setOnClickPendingIntent(R.id.widget_launch_scores_grid, actionPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);

            // Adding collection list item handler
            final Intent onItemClick = new Intent(context, WidgetProvider.class);
            // onItemClick.setAction(ACTION_TOAST);
/*            onItemClick.setAction(ACTION_VIEW_STOCK_HISTORY);
            onItemClick.setData(Uri.parse(onItemClick.toUri(Intent.URI_INTENT_SCHEME)));*/
            final PendingIntent onClickPendingIntent = PendingIntent
                    .getBroadcast(context, 0, onItemClick, PendingIntent.FLAG_UPDATE_CURRENT);
            mView.setPendingIntentTemplate(R.id.widgetCollectionList, onClickPendingIntent);

            appWidgetManager.updateAppWidget(widgetId, mView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews initViews(Context context, AppWidgetManager widgetManager, int widgetId) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.widget_lottr);

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter(widgetId, R.id.widgetCollectionList, intent);

        return mView;
    }
}
