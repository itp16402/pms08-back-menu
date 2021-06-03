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
//public class UserRoleAspect {
//
//    @Autowired
//    private AspectUtils aspectUtils;
//
//    @Before(value = "execution(* org.pms.sammenu.controllers.UserRoleController.fetchUserRolesByProjectId(..))")
//    public void fetchUserRolesByProjectId(JoinPoint joinPoint) {
//
//        log.info("Before method: {}", joinPoint.getSignature());
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                .currentRequestAttributes()).getRequest();
//
//        aspectUtils.fetchRoles(request);
//    }
//
//    @Before(value = "execution(* org.pms.sammenu.sam.controllers.UserRoleController.saveUserRoles(..))")
//    public void saveUserRoles(JoinPoint joinPoint) {
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
//    @Before(value = "execution(* org.pms.sammenu.controllers.UserRoleController.removeUser(..))")
//    public void removeUser(JoinPoint joinPoint) {
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
//    @Before(value = "execution(* org.pms.sammenu.controllers.UserRoleController.assignAdmin(..))")
//    public void assignAdmin(JoinPoint joinPoint) {
//
//        log.info("Before method: {}", joinPoint.getSignature());
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                .currentRequestAttributes()).getRequest();
//
//        Set<Authority> roles = aspectUtils.fetchRoles(request);
//
//        aspectUtils.checkIfRoleIsAuthorized(roles,
//                Authority.builder().id(AuthorityType.PARTNER.code()).build(),
//                Authority.builder().id(AuthorityType.OWNER.code()).build());
//    }
//
//    @Before(value = "execution(* org.pms.sammenu.controllers.UserRoleController.removeAdmin(..))")
//    public void removeAdmin(JoinPoint joinPoint) {
//
//        log.info("Before method: {}", joinPoint.getSignature());
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                .currentRequestAttributes()).getRequest();
//
//        Set<Authority> roles = aspectUtils.fetchRoles(request);
//
//        aspectUtils.checkIfRoleIsAuthorized(roles,
//                Authority.builder().id(AuthorityType.PARTNER.code()).build(),
//                Authority.builder().id(AuthorityType.OWNER.code()).build());
//    }
//
//    @Before(value = "execution(* org.pms.sammenu.controllers.UserRoleController.saveStatus(..))")
//    public void saveStatus(JoinPoint joinPoint) {
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                .currentRequestAttributes()).getRequest();
//
//        aspectUtils.fetchRoles(request);
//
//        aspectUtils.checkIfAssignedToWork(request);
//    }
//}
