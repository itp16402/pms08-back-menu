package org.pms.sammenu.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.pms.sammenu.utils.AspectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class FormAspect {

    @Autowired
    private AspectUtils aspectUtils;

    @Before(value = "execution(* org.pms.sammenu.controllers.forms..*.*(..))")
    public void forms(JoinPoint joinPoint) {

        log.info("Authorization -- Before method: {}", joinPoint.getSignature());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        aspectUtils.fetchRoles(request);

        if (request.getMethod().equals(HttpMethod.POST.name()) ||
                request.getMethod().equals(HttpMethod.PUT.name()) ||
                request.getMethod().equals(HttpMethod.DELETE.name()))
            aspectUtils.checkIfAssignedToWork(request);
    }

    @Before(value = "execution(* org.pms.sammenu.controllers.form_views.FormListController.*(..))")
    public void formList(JoinPoint joinPoint) {

        log.info("Authorization -- Before method: {}", joinPoint.getSignature());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        if (request.getMethod().equals(HttpMethod.POST.name()) ||
                request.getMethod().equals(HttpMethod.PUT.name()) ||
                request.getMethod().equals(HttpMethod.DELETE.name())){

            aspectUtils.fetchRoles(request);
            aspectUtils.checkIfAssignedToWork(request);
        }
    }
}
