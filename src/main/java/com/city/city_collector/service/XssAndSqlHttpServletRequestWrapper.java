
package com.city.city_collector.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

/**
 * @author nb
 * @Description:
 */
public class XssAndSqlHttpServletRequestWrapper extends HttpServletRequestWrapper {


    public XssAndSqlHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);

    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (!StringUtils.isEmpty(value)) {
            value = StringEscapeUtils.escapeHtml4(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null) {
            return null;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            String value = parameterValues[i];
            parameterValues[i] = StringEscapeUtils.escapeHtml4(value);
        }
        return parameterValues;
    }  
    
/*    @Override  
    public String getHeader(String name) {  
        return StringEscapeUtils.escapeHtml4(super.getHeader(name));  
    }  
  
    @Override  
    public String getQueryString() {  
        return StringEscapeUtils.escapeHtml4(super.getQueryString());  
    }  
  
    @Override  
    public String getParameter(String name) {  
        return StringEscapeUtils.escapeHtml4(super.getParameter(name));  
    }  
  
    @Override  
    public String[] getParameterValues(String name) {  
        String[] values = super.getParameterValues(name);  
        if(values != null) {  
            int length = values.length;  
            String[] escapseValues = new String[length];  
            for(int i = 0; i < length; i++){  
                escapseValues[i] = StringEscapeUtils.escapeHtml4(values[i]);  
            }  
            return escapseValues;  
        }  
        return super.getParameterValues(name);  
    }  
    */
   
    

}
