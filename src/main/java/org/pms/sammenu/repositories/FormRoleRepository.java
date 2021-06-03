package org.pms.sammenu.repositories;

import org.pms.sammenu.domain.FormRole;
import org.pms.sammenu.domain.UserRole;
import org.pms.sammenu.domain.form_views.FormList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FormRoleRepository extends JpaRepository<FormRole, Long> {

    Optional<FormRole> findByFormListAndUserRole(FormList formList, UserRole userRole);

    List<FormRole> findByFormListAndUserRoleIn(FormList formList, List<UserRole> userRoles);

    List<FormRole> findByUserRole(UserRole userRole);

    @Query(value = "select * from formrole inner join userrole on userroleid = userrole.id where projectid = :projectId",
            nativeQuery = true)
    List<FormRole> findByOrderId(@Param("projectId") Long projectId);

    @Transactional
    @Modifying
    @Query(value = "delete from formrole where userroleid = :userroleId",
            nativeQuery = true)
    void deleteFormRolesByUserRoleId(@Param("userroleId") Long userroleId);

    @Query(value = "select id from formrole where userroleid = :userroleId",
            nativeQuery = true)
    Set<Long> findFormRolesIdsByUserRoleId(@Param("userroleId") Long userroleId);
}
