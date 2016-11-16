package quickjournal.bhupendrashekhawat.me.android.quickjournal.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Bhupendra Shekhawat on 17/11/16.
 */

public class WidgetService extends RemoteViewsService {
    public static final String LOG_TAG = WidgetService.class.getSimpleName();



    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //Return RemoteViewsFactory
        return new WidgetFactory(this, intent);
    }
}