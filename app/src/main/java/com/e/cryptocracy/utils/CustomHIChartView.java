package com.e.cryptocracy.utils;

import android.content.Context;
import android.app.Activity;
import android.content.ContextWrapper;
import android.util.AttributeSet;

import com.highsoft.highcharts.core.HIChartView;

public class CustomHIChartView extends HIChartView {

    public CustomHIChartView(Context context) {
        super(unwrap(context));
    }

    public CustomHIChartView(Context context, AttributeSet attributeSet) {
        super(unwrap(context), attributeSet);
    }

    // unwrap the context until I get the original passed-in Activity context
    private static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (Activity) context;
    }
}
