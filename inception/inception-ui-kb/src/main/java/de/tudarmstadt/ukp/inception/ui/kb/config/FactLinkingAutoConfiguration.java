/*
 * Copyright 2019
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tudarmstadt.ukp.inception.ui.kb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.tudarmstadt.ukp.clarin.webanno.api.AnnotationSchemaService;
import de.tudarmstadt.ukp.inception.kb.KnowledgeBaseService;
import de.tudarmstadt.ukp.inception.kb.config.KnowledgeBaseServiceAutoConfiguration;
import de.tudarmstadt.ukp.inception.ui.kb.feature.FactLinkingService;
import de.tudarmstadt.ukp.inception.ui.kb.feature.FactLinkingServiceImpl;
import de.tudarmstadt.ukp.inception.ui.kb.feature.PropertyFeatureSupport;
import de.tudarmstadt.ukp.inception.ui.kb.feature.SubjectObjectFeatureSupport;
import de.tudarmstadt.ukp.inception.ui.kb.initializers.FactLayerInitializer;

@Configuration
@AutoConfigureAfter(KnowledgeBaseServiceAutoConfiguration.class)
@ConditionalOnBean(KnowledgeBaseService.class)
@ConditionalOnProperty(prefix = "knowledge-base.fact-linking", name = "enabled", 
        havingValue = "true", matchIfMissing = false)
public class FactLinkingAutoConfiguration
{
    @Bean
    @Autowired
    public PropertyFeatureSupport propertyFeatureSupport(KnowledgeBaseService aKbService)
    {
        return new PropertyFeatureSupport(aKbService);
    }
    
    @Bean
    public SubjectObjectFeatureSupport subjectObjectFeatureSupport()
    {
        return new SubjectObjectFeatureSupport();
    }
    
    @Bean
    public FactLinkingService factLinkingService()
    {
        return new FactLinkingServiceImpl();
    }
    
    @Bean
    @Autowired
    public FactLayerInitializer factLayerInitializer(
            AnnotationSchemaService aAnnotationSchemaService)
    {
        return new FactLayerInitializer(aAnnotationSchemaService);
    }
}
