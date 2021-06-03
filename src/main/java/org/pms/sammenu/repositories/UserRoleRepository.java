package org.pms.sammenu.repositories;

import org.pms.sammenu.domain.Authority;
import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Transactional
    @Modifying
    @Query(value = "delete from userrole where userid = :userId and projectid = :projectId",
            nativeQuery = true)
    void deleteUserRoleByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);

    @Transactional
    @Modifying
    @Query(value = "delete from userrole where projectid = :projectId",
            nativeQuery = true)
    void deleteUserRoleByProjectId(@Param("projectId") Long projectId);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Set<UserRole> findByUser_IdAndProject(Long userId, Project project);

    @Query(value = "select distinct(userid) from userrole where projectid = :projectId",
            nativeQuery = true)
    Set<Long> findUserIds(@Param("projectId") Long projectId);

    @Query(value = "select distinct(projectid) from userrole where userid = :userId",
            nativeQuery = true)
    Set<Long> findProjectIdsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM public.userrole where userid = :userId and projectid = :projectId " +
            "and roleid not in(1, 5) and projectid " +
            "in (select projectid from userrole where userid = :userId  group by projectid having count(roleid) = 1)",
            nativeQuery = true)
    Set<UserRole> findRolesAsMember(@Param("userId") Long userId, @Param("projectId") Long projectId);

    Optional<UserRole> findByProjectAndAuthority(Project project, Authority authority);

    Optional<UserRole> findByUser_IdAndProjectAndAuthority(Long userId, Project project, Authority authority);

    @Query(value = "select m.* from memberrole m " +
            "inner join formrole f on f.memberroleid = m.id " +
            "inner join formlist l on f.formlistid = l.id " +
            "where l.formname = :formName and m.roleid = 3 and m.projectid = :projectId",
            nativeQuery = true)
    Set<UserRole> findAssignedUsersByForm(@Param("formName") String formName, @Param("projectId") Long projectId);
}
