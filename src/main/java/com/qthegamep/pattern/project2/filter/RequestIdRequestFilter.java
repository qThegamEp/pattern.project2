package com.qthegamep.pattern.project2.filter;

import com.qthegamep.pattern.project2.binder.property.Property;
import com.qthegamep.pattern.project2.service.GenerationService;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(10)
public class RequestIdRequestFilter implements ContainerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestIdRequestFilter.class);

    private static final String X_REQUEST_ID_HEADER = "x-request-id";

    private Long defaultRequestIdLength;
    private GenerationService generationService;

    @Inject
    public RequestIdRequestFilter(@Property(value = "filter.default.request.id.length") Long defaultRequestIdLength,
                                  GenerationService generationService) {
        this.defaultRequestIdLength = defaultRequestIdLength;
        this.generationService = generationService;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String requestIdHeader = containerRequestContext.getHeaderString(Constants.REQUEST_ID_HEADER);
        if (requestIdHeader == null || requestIdHeader.isEmpty()) {
            String xRequestIdHeader = containerRequestContext.getHeaderString(X_REQUEST_ID_HEADER);
            String requestId = xRequestIdHeader == null || xRequestIdHeader.isEmpty()
                    ? generationService.generateUniqueId(defaultRequestIdLength)
                    : xRequestIdHeader;
            LOG.debug("RequestId: {}", requestId);
            MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
            headers.add(Constants.REQUEST_ID_HEADER, requestId);
        } else {
            LOG.debug("RequestId header: {}", requestIdHeader);
        }
    }
}
