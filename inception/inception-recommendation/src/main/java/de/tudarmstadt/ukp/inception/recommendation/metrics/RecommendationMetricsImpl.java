/*
 * Copyright 2020
 * Ubiquitous Knowledge Processing (UKP) Lab and FG Language Technology
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
package de.tudarmstadt.ukp.inception.recommendation.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import de.tudarmstadt.ukp.inception.recommendation.api.RecommendationService;

@ManagedResource
/**
 * <p>
 * This class is exposed as a Spring Component via
 * {@link RecommenderServiceAutoConfiguration#recommendationMetricsImpl}.
 * </p>
 */
public class RecommendationMetricsImpl
    implements RecommendationMetrics
{
    private final RecommendationService recService;
    
    @Autowired
    public RecommendationMetricsImpl(RecommendationService aRecService)
    {
        recService = aRecService;
    }

    @Override
    @ManagedAttribute
    public long getEnabledRecommendersTotal()
    {
        return recService.countEnabledRecommenders();
    }
    
}
