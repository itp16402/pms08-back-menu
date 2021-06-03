//package org.pms.sammenu.aspects;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.pms.sammenu.config.security.Authorization;
//import org.pms.sammenu.utils.AspectUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.security.Principal;
//
//@Aspect
//@Component
//@Slf4j
//public class ProjectAspect {
//
//    @Autowired
//    private AspectUtils aspectUtils;
//
//    @Before(value = "execution(* org.pms.sammenu.controllers.OrderController.*(..))")
//    public void ordersOfMemberInfo(JoinPoint joinPoint) {
//        log.info("Before method: {}", joinPoint.getSignature());
//        String username = (String) aspectUtils.getParameterByName((ProceedingJoinPoint) joinPoint, "username");
//        Principal principal = (Principal) aspectUtils.getParameterByName((ProceedingJoinPoint) joinPoint, "principal");
//        Authorization.authorizeRequest(username, principal);
//    }
//}
