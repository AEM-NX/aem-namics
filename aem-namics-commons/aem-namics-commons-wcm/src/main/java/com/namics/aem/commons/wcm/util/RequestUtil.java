
package com.namics.aem.commons.wcm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Util class used for request related topics.
 *
 * @author sbaur
 * @author mkitanovski
 */
@Slf4j
public final class RequestUtil {

    public static final String UTF_8 = "UTF-8";

    private RequestUtil() {
    }

    /**
     * @param request the request
     * @param name    name of the parameter to look for
     * @return if the request has a parameter with the given name
     */
    public static boolean hasParameter(SlingHttpServletRequest request, String name) {
        return request.getParameter(name) != null;
    }

    /**
     * Gets a parameter from the request, return empty string if it does not exist.
     *
     * @param request the request
     * @param name    the parameter name
     * @return the request parameter, empty string if parameter does not exist
     */
    public static String getStringParameter(final SlingHttpServletRequest request, final String name) {
        return getStringParameter(request, name, StringUtils.EMPTY);
    }

    /**
     * Gets a parameter from the request, return defaultValue if it does not exist.
     *
     * @param request      the request
     * @param name         the parameter name
     * @param defaultValue the defaultValue
     * @return the request parameter or defaultValue if parameter does not exist
     */
    public static String getStringParameter(final SlingHttpServletRequest request, final String name, final String defaultValue) {
        if (hasParameter(request, name)) {
            return request.getParameter(name);
        } else {
            return defaultValue;
        }
    }

    /**
     * Gets a parameter as integer from the request, return a default value if it does not exist.
     *
     * @param request the request
     * @param name    the parameter name
     * @return the request parameter as integer, default value if parameter does not exist
     */
    public static int getIntParameter(final SlingHttpServletRequest request, final String name) {
        return getIntParameter(request, name, 0);
    }

    /**
     * Gets a parameter as integer from the request, return a default value if it does not exist.
     *
     * @param request      the request
     * @param name         the parameter name
     * @param defaultValue the default value
     * @return the request parameter as integer, default value if parameter does not exist
     */
    public static int getIntParameter(final SlingHttpServletRequest request, final String name, final int defaultValue) {
        try {
            return Integer.parseInt(getStringParameter(request, name, String.valueOf(defaultValue)));
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Gets a parameter as boolean from the request
     *
     * @param request the request
     * @param name    the parameter name
     * @return the request parameter as integer, false if parameter does not exist
     */
    public static boolean getBooleanParameter(final SlingHttpServletRequest request, final String name) {
        return getBooleanParameter(request, name, false);
    }

    /**
     * Gets a parameter as boolean from the request
     *
     * @param request      the request
     * @param name         the parameter name
     * @param defaultValue the default value
     * @return the request parameter as integer, default value if parameter does not exist
     */
    public static boolean getBooleanParameter(final SlingHttpServletRequest request, final String name, final boolean defaultValue) {
        if (hasParameter(request, name)) {
            return Boolean.parseBoolean(getStringParameter(request, name));
        }
        return defaultValue;
    }

    /**
     * Returns, if the given Request has the searched selector.
     *
     * @param request          {@link SlingHttpServletRequest}
     * @param searchedSelector String with Selector to search for.
     * @return true, if given parameter is a selector of the request
     */
    public static boolean hasSelector(final SlingHttpServletRequest request, final String searchedSelector) {
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
     * @param request the request to analyze
     * @return get the base url before the request path
     */
    public static String getBaseUrl(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestURL = request.getRequestURL().toString();
        String baseUrl = StringUtils.substringBefore(requestURL, requestURI);
        baseUrl = StringUtils.removeEnd(baseUrl, "/");
        return baseUrl;
    }

    /**
     * @param request the request to analyze
     * @return the full request url including the query string
     */
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


    /**
     * NullSafe way to get a request parameter value. (returns an empty string if parameter is null)
     *
     * @param parameterName request parameter name
     * @param request       the request
     * @return the value of the given parameter name or an empty string if it does not exits
     */
    public static String getNullSafeUtf8ParameterValue(String parameterName, SlingHttpServletRequest request) {
        if (parameterName == null || request == null) {
            return StringUtils.EMPTY;
        }

        final RequestParameter reqParameter = request.getRequestParameter(parameterName);
        return getNullSafeUtf8ParameterValue(reqParameter);
    }

    /**
     * NullSafe way to get a request parameter value. (returns an empty string if parameter is null)
     *
     * @param requestParameter the request parameter
     * @return the value of the given parameter or an empty string if it does not exits
     */
    public static String getNullSafeUtf8ParameterValue(RequestParameter requestParameter) {
        String value = "";

        try {
            if (requestParameter != null) {
                value = requestParameter.getString(UTF_8);
            }
        } catch (UnsupportedEncodingException ignored) {
        }

        return value;
    }

    /**
     * Returns the resource from the request path.
     *
     * @param request {@link SlingHttpServletRequest} request.
     * @return Resource from the request.
     */
    public static Resource getResourceFromRequestPath(final SlingHttpServletRequest request) {
        final String pathInfo = request.getPathInfo();
        return request.getResource().getResourceResolver().resolve(pathInfo);
    }

    /**
     * Returns true, if the request has the given parameter. False otherwise. Returns false if either parameter is null or parameterName is blank.
     *
     * @param parameterName parameter to lookup
     * @param request       request to check
     * @return true, if the request has the given parameter. False otherwise.
     */
    public static boolean hasParameter(String parameterName, SlingHttpServletRequest request) {
        if (request == null || StringUtils.isBlank(parameterName)) {
            return false;
        }
        return request.getRequestParameter(parameterName) != null;
    }

    /**
     * Returns a selection list of an element.
     *
     * @param request     Form request as SlingHttpServletRequest
     * @param name Form element name
     * @return list of requests
     */
    public static List<String> getParameterListFromRequest(final SlingHttpServletRequest request, final String name) {
        final List<String> requestList = new ArrayList<>();
        final String[] paramValues = request.getParameterValues(name);
        if (null != paramValues && paramValues.length > 0) {
            Collections.addAll(requestList, paramValues);
        }
        return requestList;
    }

}
