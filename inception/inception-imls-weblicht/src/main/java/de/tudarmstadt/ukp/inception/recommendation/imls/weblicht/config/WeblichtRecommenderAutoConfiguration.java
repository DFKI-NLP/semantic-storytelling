/*
 * Copyright 2020
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
package de.tudarmstadt.ukp.inception.recommendation.imls.weblicht.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.tudarmstadt.ukp.clarin.webanno.api.RepositoryProperties;
import de.tudarmstadt.ukp.inception.recommendation.api.RecommendationService;
import de.tudarmstadt.ukp.inception.recommendation.imls.weblicht.WeblichtRecommenderFactory;
import de.tudarmstadt.ukp.inception.recommendation.imls.weblicht.WeblichtRecommenderFactoryImpl;
import de.tudarmstadt.ukp.inception.recommendation.imls.weblicht.chains.WeblichtChainService;
import de.tudarmstadt.ukp.inception.recommendation.imls.weblicht.chains.WeblichtChainServiceImpl;
import de.tudarmstadt.ukp.inception.recommendation.imls.weblicht.exporter.ChainExporter;

/**
 * Provides support for calling out to CLARIN Weblicht for recommendations.
 */
@Configuration
@ConditionalOnProperty(prefix = "recommender.weblicht", name = "enabled", havingValue = "true", 
        matchIfMissing = true)
public class WeblichtRecommenderAutoConfiguration
{
    private @PersistenceContext EntityManager entityManager;
    
    @Bean
    public WeblichtRecommenderFactory weblichtRecommenderFactory(WeblichtChainService aChainService)
    {
        return new WeblichtRecommenderFactoryImpl(aChainService);
    }
    
    @Bean
    public WeblichtChainService weblichtChainService(RepositoryProperties aRepoProperties)
    {
        return new WeblichtChainServiceImpl(aRepoProperties, entityManager);
    }
    
    public ChainExporter chainExporter(RecommendationService aRecommendationService,
            WeblichtChainService aChainService)
    {
        return new ChainExporter(aRecommendationService, aChainService);
    }
}
