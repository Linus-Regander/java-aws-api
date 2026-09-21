package com.imagerecognitioner.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraceIdFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private final TraceIdFilter filter = new TraceIdFilter();

    @AfterEach
    void clearTraceId() {
        MDC.clear();
    }

    @Test
    void doFilterInternal_propagatesOrGeneratesTraceIdAndAlwaysCleansUp() throws Exception {
        Map<String, FilterTestCase> testCases = Map.of(
            "existing trace id", () -> runScenario("trace-123", false),
            "blank trace id", () -> runScenario("   ", false),
            "missing trace id", () -> runScenario(null, false),
            "filter chain failure", () -> runScenario("trace-failure", true));

        for (Map.Entry<String, FilterTestCase> testCase : testCases.entrySet()) {
            reset(request, response, filterChain);
            try {
                testCase.getValue().run();
            } catch (AssertionError assertionError) {
                throw new AssertionError("Test case failed: " + testCase.getKey(), assertionError);
            } catch (Exception exception) {
                throw new AssertionError("Test case failed: " + testCase.getKey(), exception);
            }
        }
    }

    private void runScenario(String header, boolean chainFails) throws Exception {
        when(request.getHeader(TraceIdFilter.TRACE_ID_HEADER)).thenReturn(header);
        if (chainFails) {
            doThrow(new ServletException("chain failed")).when(filterChain).doFilter(request, response);
        }

        Exception thrown = null;
        try {
            filter.doFilterInternal(request, response, filterChain);
        } catch (Exception exception) {
            thrown = exception;
        }

        ArgumentCaptor<String> traceId = ArgumentCaptor.forClass(String.class);
        verify(response).setHeader(org.mockito.ArgumentMatchers.eq(TraceIdFilter.TRACE_ID_HEADER), traceId.capture());
        verify(filterChain).doFilter(request, response);
        assertNotNull(traceId.getValue());
        assertEquals(header == null || header.isBlank() ? traceId.getValue() : header, traceId.getValue());
        assertNull(MDC.get(TraceIdFilter.TRACE_ID_MDC_KEY));
        if (chainFails) {
            assertNotNull(thrown);
            assertEquals("chain failed", thrown.getMessage());
        } else {
            assertNull(thrown);
        }
    }

    @FunctionalInterface
    private interface FilterTestCase {
        void run() throws Exception;
    }
}