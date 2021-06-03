package org.pms.sammenu.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageProcessor { // Any name you like
    // List of HttpMessageConverter
    private List<HttpMessageConverter<?>> messageConverters;
    // under org.springframework.web.servlet.mvc.method.annotation
    /**
     * Below class name are copied from the framework.
     * (And yes, they are hard-coded, too)
     */
    private static final boolean jaxb2Present =
            ClassUtils.isPresent("javax.xml.bind.Binder", MessageProcessor.class.getClassLoader());

    private static final boolean jackson2Present =
            ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", MessageProcessor.class.getClassLoader()) &&
                    ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", MessageProcessor.class.getClassLoader());

    private static final boolean gsonPresent =
            ClassUtils.isPresent("com.google.gson.Gson", MessageProcessor.class.getClassLoader());

    public void handle(Object returnValue, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {

        this.messageConverters = new ArrayList<>();

        this.messageConverters.add(new ByteArrayHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new ResourceHttpMessageConverter());
        this.messageConverters.add(new SourceHttpMessageConverter<Source>());
        this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());

        if (jaxb2Present) {
            this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
        }
        if (jackson2Present) {
            this.messageConverters.add(new MappingJackson2HttpMessageConverter());
        }
        else if (gsonPresent) {
            this.messageConverters.add(new GsonHttpMessageConverter());
        }

        RequestResponseBodyMethodProcessor processor = new RequestResponseBodyMethodProcessor(this.messageConverters);

        ServletWebRequest nativeRequest = new ServletWebRequest(request, response);
        processor.handleReturnValue(returnValue,
                new MethodParameter(getClass().getMethod("handle", Object.class,
                        HttpServletRequest.class,
                        HttpServletResponse.class),
                        -1),
                new ModelAndViewContainer(),
                nativeRequest);
    }

    /**
     * @return list of message converters
     */
    public List<HttpMessageConverter<?>> getMessageConverters() {
        return messageConverters;
    }
}
