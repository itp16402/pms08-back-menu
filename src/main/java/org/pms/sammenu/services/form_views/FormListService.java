package org.pms.sammenu.services.form_views;

import org.pms.sammenu.dto.form_views.FormListDto;
import org.pms.sammenu.enums.FormStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface FormListService {

    List<FormListDto> findAll();

    FormListDto fetchByFormName(String formName);

    void saveStatus(Long orderId, FormStatus status, Long formListId, HttpServletRequest request);

    FormStatus getStatus(Long orderId, Long formListId);
}
