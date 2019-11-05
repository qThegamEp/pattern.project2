package com.qthegamep.pattern.project2.metric;

import com.qthegamep.pattern.project2.util.Constants;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import sun.management.Util;

import java.lang.management.PlatformManagedObject;

public class ResponseStatusMetric implements MeterBinder {

    private static final String RESPONSE_STATUS = "response.status";
    private static final String RESPONSE_CODE_TAG = "response.code";

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        PlatformManagedObject errorTypesPlatformManagedObject = () -> Util.newObjectName(RESPONSE_STATUS);
        Metrics.RESPONSE_STATUS_METRIC.forEach((key, value) -> Gauge.builder(RESPONSE_STATUS, errorTypesPlatformManagedObject, platformManagedObject -> value.get())
                .description("The response status")
                .baseUnit(Constants.GRIZZLY.getValue())
                .tags(Tags.of(RESPONSE_CODE_TAG, key))
                .register(meterRegistry));
    }
}
