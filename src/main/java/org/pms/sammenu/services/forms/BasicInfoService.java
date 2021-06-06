package org.pms.sammenu.services.forms;

import org.pms.sammenu.dto.forms.BasicInfoDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface BasicInfoService {

    BasicInfoDto fetchBasicInfoByProjectId(Long id, Long formListId);

    void save(Long projectId, BasicInfoDto basicInfoDto, Long formListId, HttpServletRequest request);
}
