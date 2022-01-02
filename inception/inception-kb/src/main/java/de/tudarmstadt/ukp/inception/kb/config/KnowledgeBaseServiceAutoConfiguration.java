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
package de.tudarmstadt.ukp.inception.kb.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.tudarmstadt.ukp.clarin.webanno.api.AnnotationSchemaService;
import de.tudarmstadt.ukp.clarin.webanno.api.RepositoryProperties;
import de.tudarmstadt.ukp.inception.kb.KnowledgeBaseService;
import de.tudarmstadt.ukp.inception.kb.KnowledgeBaseServiceImpl;
import de.tudarmstadt.ukp.inception.kb.exporter.KnowledgeBaseExporter;

@Configuration
@ConditionalOnProperty(prefix = "knowledge-base", name = "enabled", 
        havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(KnowledgeBasePropertiesImpl.class)
public class KnowledgeBaseServiceAutoConfiguration
{
    private @PersistenceContext EntityManager entityManager;
    
    @Bean
    @Autowired
    public KnowledgeBaseExporter knowledgeBaseExporter(KnowledgeBaseService aKbService,
            KnowledgeBaseProperties aKbProperties, AnnotationSchemaService aSchemaService)
    {
        return new KnowledgeBaseExporter(aKbService, aKbProperties, aSchemaService);
    }
    
    @Bean
    @Autowired
    public KnowledgeBaseService knowledgeBaseService(RepositoryProperties aRepoProperties,
            KnowledgeBaseProperties aKbProperties)
    {
        return new KnowledgeBaseServiceImpl(aRepoProperties, aKbProperties, entityManager);
    }
}
