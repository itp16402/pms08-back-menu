package org.pms.sammenu.utils;

import org.pms.sammenu.domain.forms.essential_size.EssentialSizePerformance;
import org.pms.sammenu.enums.Audit;
import org.pms.sammenu.exceptions.UnacceptableActionException;
import org.pms.sammenu.repositories.essential_size.EssentialSizePerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EssentialSizeUtils {

    @Autowired
    private EssentialSizePerformanceRepository essentialSizePerformanceRepository;

    public Double getPerformanceMateriality(Long projectId){

        List<EssentialSizePerformance> essentialSizePerformanceList = essentialSizePerformanceRepository
                .findByEssentialSize_Project_Id(projectId);

        if (essentialSizePerformanceList.isEmpty())
            throw new UnacceptableActionException("Essential size form has not completed yet.");

        return essentialSizePerformanceList.get(0).getPerAmount();
    }
}
