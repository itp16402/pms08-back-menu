package org.pms.sammenu.services.forms;

import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountDto;
import org.pms.sammenu.dto.forms.important_accounts.ImportantAccountRequestDto;
import org.pms.sammenu.enums.Locale;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface ImportantAccountService {

    ImportantAccountDto fetchImportantAccountByProjectId(Long projectId, Long formListId);

    void save(Long projectId, ImportantAccountRequestDto importantAccountRequestDto, Long formListId, HttpServletRequest request);
}
