package org.pms.sammenu.services.forms;

import org.pms.sammenu.dto.forms.essential_size.base.BaseDto;
import org.pms.sammenu.dto.forms.essential_size.base.BaseResponseDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizeOverallRequestDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizePerformanceRequestDto;
import org.pms.sammenu.dto.forms.essential_size.requests.EssentialSizeRequestDto;
import org.pms.sammenu.dto.forms.essential_size.responses.EssentialSizeDto;
import org.pms.sammenu.dto.forms.essential_size.responses.EssentialSizePerformanceDto;
import org.pms.sammenu.enums.Locale;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface EssentialSizeService {

    EssentialSizeDto fetchEssentialSizeByProjectId(Long projectId, Long formListId, Locale locale);

    List<BaseResponseDto> fetchBaseList(Long projectId, Locale locale);

    BaseDto fetchBase(Integer baseId, Long projectId, Locale locale);

    void save(Long projectId, EssentialSizeRequestDto essentialSizeDto, Long formListId, HttpServletRequest request);

    void save(Long projectId, EssentialSizeOverallRequestDto essentialSizeOverallRequestDto);

    void save(Long projectId, EssentialSizePerformanceRequestDto essentialSizePerformanceRequestDto);

    List<EssentialSizePerformanceDto> fetchPerformanceMateriality(Long projectId);
}
