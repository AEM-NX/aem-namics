
package com.namics.aem.commons.wcm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Util class used for request related topics.
 *
 * @author sbaur, Namics AG
 */
@Slf4j
public final class RequestUtil {


    private RequestUtil() {
    }

    /**
     * Gets a parameter from the request, return empty string if it does not exist.
     *
     * @param name    the parameter name
     * @param request the request
     * @return the request parameter, empty string if parameter does not exist
     */
    public static String getRequestParameter(
            final String name,
            final SlingHttpServletRequest request) {
        return getRequestParameter(name, StringUtils.EMPTY, request);
    }

    /**
     * Gets a parameter from the request, return defaultValue if it does not exist.
     *
     * @param name         the parameter name
     * @param request      the request
     * @param defaultValue the defaultValue
     * @return the request parameter or defaultValue if parameter does not exist
     */
    public static String getRequestParameter(final String name,
                                             final String defaultValue,
                                             final SlingHttpServletRequest request) {

        if (request.getRequestParameterMap() != null) {
            final RequestParameter requestParameter = request.getRequestParameterMap().getValue(name);
            if (requestParameter != null && StringUtils.isNotBlank(requestParameter.toString())) {
                return requestParameter.toString();
            }
        }
        return defaultValue;
    }

    /**
     * Gets a parameter as integer from the request, return a default value if it does not exist.
     *
     * @param name         the parameter name
     * @param request      the request
     * @param defaultValue the default value
     * @return the request parameter as integer, default value if parameter does not exist
     */
    public static int getRequestParameterAsInteger(
            final String name,
            final SlingHttpServletRequest request,
            final int defaultValue) {
        try {
            return Integer.parseInt(getRequestParameter(name, request));
        } catch (final NumberFormatException e) {
            log.debug("getRequestParameterAsInteger(): unable to parse int from request parameter: {}", name);
        }
        return defaultValue;
    }

    /**
     * Gets the value after the given selector prefix. Empty String otherwise.
     *
     * @param request {@link SlingHttpServletRequest}
     * @param prefix  String
     * @return value after the given selector prefix. Empty String otherwise.
     */
    public static String getSelectorByPrefix(
            final SlingHttpServletRequest request,
            final String prefix) {
        final List<String> selectors = getSelectors(request);
        for (final String selector : selectors) {
            if (StringUtils.startsWith(selector, prefix)) {
                return StringUtils.substringAfter(selector, prefix);
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Returns, if the given Request has the searched selector.
     *
     * @param request          {@link SlingHttpServletRequest}
     * @param searchedSelector String with Selector to search for.
     * @return true, if given parameter is a selector of the request
     */
    public static boolean hasSelector(
            final SlingHttpServletRequest request,
            final String searchedSelector) {
        final List<String> selectors = getSelectors(request);
        for (final String givenSelector : selectors) {
            if (StringUtils.equalsIgnoreCase(givenSelector, searchedSelector)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getSelectors(final SlingHttpServletRequest request) {
        return new ArrayList<>(Arrays.asList(request.getRequestPathInfo().getSelectors()));
    }

    /**
     * Looks for a selector by index.
     *
     * @param request      the request to look at
     * @param defaultValue the value that is returned if no valid selector is found (not selectors, or not enough selectors)
     * @return the selector with the given index, or defaultValue if not found
     */
    public static String getFirstSelector(@Nonnull SlingHttpServletRequest request, String defaultValue) {
        return getSelector(request, 0, defaultValue);
    }

    /**
     * Check if X-SSI-Enabled is set.
     *
     * @param request {@link SlingHttpServletRequest}
     * @return true, if X-SSI-Enabled is true, otherwise false
     */
    public static boolean isSSIEnabled(final SlingHttpServletRequest request) {
        return Boolean.parseBoolean(request.getHeader("X-SSI-Enabled"));
    }

    /**
     * Looks for a selector by index.
     *
     * @param request       the request to look at
     * @param selectorIndex the index of the selector to be returned
     * @param defaultValue  the value that is returned if no valid selector is found (not selectors, or not enough selectors)
     * @return the selector with the given index, or defaultValue if not found
     */
    public static String getSelector(@Nonnull SlingHttpServletRequest request, int selectorIndex, String defaultValue) {
        final List<String> selectors = getSelectors(request);
        if (selectors.isEmpty() || selectorIndex >= selectors.size()) {
            return defaultValue;
        } else {
            return selectors.get(selectorIndex);
        }
    }

    public static String getBaseUrl(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestURL = request.getRequestURL().toString();
        String baseUrl = StringUtils.substringBefore(requestURL, requestURI);
        baseUrl = StringUtils.removeEnd(baseUrl, "/");
        return baseUrl;
    }

    public static String getFullRequestUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String result;
        if (StringUtils.isNotBlank(queryString)) {
            result = url + "?" + queryString;
        } else {
            result = url;
        }
        return result;
    }

}
