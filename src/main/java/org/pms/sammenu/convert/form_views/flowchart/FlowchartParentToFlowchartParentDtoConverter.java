package org.pms.sammenu.convert.form_views.flowchart;

import org.pms.sammenu.domain.form_views.Flowchart;
import org.pms.sammenu.domain.form_views.FlowchartChild;
import org.pms.sammenu.domain.form_views.FlowchartParent;
import org.pms.sammenu.dto.form_views.FlowchartChildDto;
import org.pms.sammenu.dto.form_views.FlowchartDto;
import org.pms.sammenu.dto.form_views.FlowchartParentDto;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlowchartParentToFlowchartParentDtoConverter implements Converter<FlowchartParent, FlowchartParentDto> {

    @Override
    public FlowchartParentDto convert(FlowchartParent flowchartParent) {
        return FlowchartParentDto.builder()
                .id(flowchartParent.getId())
                .phase(flowchartParent.getPhase())
                .name(flowchartParent.getName())
                .formName(flowchartParent.getFormName())
                .language(Locale.fromValue(flowchartParent.getLanguage()))
                .formType(!ObjectUtils.isEmpty(flowchartParent.getFormType()) ?
                        FormType.fromValue(flowchartParent.getFormType()) : null)
                .sOrder(flowchartParent.getSeqOrder())
                .type(flowchartParent.getType())
                .icon(flowchartParent.getIcon())
                .css(flowchartParent.getCss())
                .flowcharts(buildFlowcharts(flowchartParent.getFlowcharts()))
                .build();
    }

    private List<FlowchartDto> buildFlowcharts(List<Flowchart> flowcharts){

        return flowcharts
                .stream()
                .map(flowchart -> FlowchartDto.builder()
                        .id(flowchart.getId())
                        .phase(flowchart.getPhase())
                        .name(flowchart.getName())
                        .formName(flowchart.getFormName())
                        .language(Locale.fromValue(flowchart.getLanguage()))
                        .formType(FormType.fromValue(flowchart.getFormType()))
                        .sOrder(flowchart.getSeqOrder())
                        .type(flowchart.getType())
                        .icon(flowchart.getIcon())
                        .css(flowchart.getCss())
                        .state(flowchart.getState())
                        .children(!ObjectUtils.isEmpty
                                (flowchart.getFlowchartChildren()) ?
                                buildChildren(flowchart.getFlowchartChildren()) : new ArrayList<>())
                        .build())
                .sorted(Comparator.comparing(FlowchartDto::getSOrder))
                .collect(Collectors.toList());
    }

    private List<FlowchartChildDto> buildChildren(List<FlowchartChild> flowchartChildren){

        return flowchartChildren
                .stream()
                .map(flowchartChild -> FlowchartChildDto.builder()
                        .id(flowchartChild.getId())
                        .phase(flowchartChild.getPhase())
                        .name(flowchartChild.getName())
                        .formName(flowchartChild.getFormName())
                        .language(Locale.fromValue(flowchartChild.getLanguage()))
                        .formType(FormType.fromValue(flowchartChild.getFormType()))
                        .sOrder(flowchartChild.getSeqOrder())
                        .type(flowchartChild.getType())
                        .icon(flowchartChild.getIcon())
                        .css(flowchartChild.getCss())
                        .state(flowchartChild.getState())
                        .build())
                .sorted(Comparator.comparing(FlowchartChildDto::getSOrder))
                .collect(Collectors.toList());
    }
}
