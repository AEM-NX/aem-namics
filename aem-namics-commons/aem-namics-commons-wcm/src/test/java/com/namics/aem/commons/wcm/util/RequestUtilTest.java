package com.namics.aem.commons.wcm.util;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author mkitanovski
 * @author sbaur
 */
public class RequestUtilTest {

    @Test
    public void testGetNullSafeRequestParameterHappyPath() throws UnsupportedEncodingException {
        final SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
        final RequestParameter requestParameter = mock(RequestParameter.class);

        when(requestParameter.getString(RequestUtil.UTF_8)).thenReturn("MyNodeName");

        when(request.getRequestParameter("tabNodeName")).thenReturn(requestParameter);

        assertThat(RequestUtil.getNullSafeUtf8ParameterValue("tabNodeName", request), equalTo("MyNodeName"));
    }

    @Test
    public void testGetNullSafeRequestParameterParamNotExists() throws UnsupportedEncodingException {
        final SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);

        when(request.getRequestParameter("tabNodeName")).thenReturn(null);

        assertThat(RequestUtil.getNullSafeUtf8ParameterValue("tabNodeName", request), equalTo(""));
    }

    @Test
    public void testGetNullSafeRequestParameterWithNullParam() {
        final SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
        assertThat(RequestUtil.getNullSafeUtf8ParameterValue(null, request), equalTo(""));
    }

    @Test
    public void testGetNullSafeRequestParameterWithNullRequest() {
        assertThat(RequestUtil.getNullSafeUtf8ParameterValue("tabNodeName", null), equalTo(""));
    }

    @Test
    public void testGetNullSafeUtf8ParameterValueWith() throws UnsupportedEncodingException {
        final RequestParameter requestParameter = mock(RequestParameter.class);

        when(requestParameter.getString(RequestUtil.UTF_8)).thenReturn("MyNodeName");

        assertThat(RequestUtil.getNullSafeUtf8ParameterValue(requestParameter), equalTo("MyNodeName"));
    }

    @Test
    public void testGetNullSafeUtf8ParameterValueWithNullParam() {
        assertThat(RequestUtil.getNullSafeUtf8ParameterValue(null), equalTo(""));
    }

    @Test
    public void testHasParameter() throws Exception {
        final SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
        final RequestParameter requestParameter = mock(RequestParameter.class);

        when(request.getRequestParameter("valid")).thenReturn(requestParameter);

        assertThat(RequestUtil.hasParameter("valid", request), is(true));
        assertThat(RequestUtil.hasParameter("invalid", request), is(false));
        assertThat(RequestUtil.hasParameter("valid", null), is(false));
        assertThat(RequestUtil.hasParameter(null, request), is(false));
        assertThat(RequestUtil.hasParameter("", request), is(false));

    }
}
