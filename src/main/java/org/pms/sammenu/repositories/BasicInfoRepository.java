package org.pms.sammenu.repositories;

import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.forms.BasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasicInfoRepository extends JpaRepository<BasicInfo, Long> {

    Optional<BasicInfo> findBasicInfoByProject(Project project);

    Optional<BasicInfo> findBasicInfoByProject_Id(Long projectId);
}
