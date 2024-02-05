package com.city.city_collector.freemark.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.city.city_collector.freemark.directivemodel.IncludeNewDirectiveModel;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

@Component
public class IncludeNewConfig {

    @Autowired
    private Configuration configuration;

    @Autowired
    private IncludeNewDirectiveModel includeNewDirectiveModel;

    @PostConstruct
    public void setSharedVariable()
            throws TemplateModelException {
        this.configuration.setSharedVariable("includeNew", this.includeNewDirectiveModel);
    }
}
