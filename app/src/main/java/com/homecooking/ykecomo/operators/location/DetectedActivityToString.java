package com.homecooking.ykecomo.operators.location;

import com.google.android.gms.location.DetectedActivity;

import rx.functions.Func1;

public class DetectedActivityToString implements Func1<DetectedActivity, String> {
    @Override
    public String call(DetectedActivity detectedActivity) {
        return getNameFromType(detectedActivity.getType()) + " with confidence " + detectedActivity.getConfidence();
    }

    private String getNameFromType(int activityType) {
        switch (activityType) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.TILTING:
                return "tilting";
        }
        return "unknown";
    }
}
