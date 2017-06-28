package com.example.rkalonji.lordofthetrivia;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by rkalonji on 06/28/2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        WidgetScoreViewsFactory dataProvider = new WidgetScoreViewsFactory(
                getApplicationContext(), intent);
        return dataProvider;
    }
}
