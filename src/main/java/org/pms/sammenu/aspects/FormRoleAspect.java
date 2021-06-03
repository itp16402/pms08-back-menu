//package org.pms.sammenu.aspects;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.pms.sammenu.domain.Authority;
//import org.pms.sammenu.enums.AuthorityType;
//import org.pms.sammenu.utils.AspectUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Set;
//
//@Aspect
//@Component
//@Slf4j
//public class FormRoleAspect {
//
//    @Autowired
//    private AspectUtils aspectUtils;
//
//    @Before(value = "execution(* org.pms.sammenu.controllers.FormRoleController.saveFormRoles(..))")
//    public void saveFormRoles(JoinPoint joinPoint) {
//
//        log.info("Before method: {}", joinPoint.getSignature());
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                .currentRequestAttributes()).getRequest();
//
//        Set<Authority> roles = aspectUtils.fetchRoles(request);
//
//        aspectUtils.checkIfRoleIsAuthorized(roles, Authority.builder().id(AuthorityType.MANAGER.code()).build());
//    }
//
//    @Before(value = "execution(* org.pms.sammenu.controllers.FormRoleController.removeFormRoles(..))")
//    public void removeFormRoles(JoinPoint joinPoint) {
//
//        log.info("Before method: {}", joinPoint.getSignature());
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                .currentRequestAttributes()).getRequest();
//
//        Set<Authority> roles = aspectUtils.fetchRoles(request);
//
//        aspectUtils.checkIfRoleIsAuthorized(roles, Authority.builder().id(AuthorityType.MANAGER.code()).build());
//    }
//
//    @Before(value = "execution(* org.pms.sammenu.controllers.FormRoleController.changeFormStatus(..))")
//    public void changeFormStatus(JoinPoint joinPoint) {
//
//        log.info("Before method: {}", joinPoint.getSignature());
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                .currentRequestAttributes()).getRequest();
//
//        aspectUtils.checkIfAssignedToWork(request);
//    }
//}
