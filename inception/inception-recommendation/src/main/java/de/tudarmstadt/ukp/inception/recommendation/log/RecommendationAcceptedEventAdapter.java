/*
 * Copyright 2018
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
package de.tudarmstadt.ukp.inception.recommendation.log;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tudarmstadt.ukp.clarin.webanno.support.JSONUtil;
import de.tudarmstadt.ukp.inception.log.adapter.EventLoggingAdapter;
import de.tudarmstadt.ukp.inception.log.model.AnnotationDetails;
import de.tudarmstadt.ukp.inception.log.model.FeatureChangeDetails;
import de.tudarmstadt.ukp.inception.recommendation.config.RecommenderServiceAutoConfiguration;
import de.tudarmstadt.ukp.inception.recommendation.event.RecommendationAcceptedEvent;

/**
 * <p>
 * This class is exposed as a Spring Component via
 * {@link RecommenderServiceAutoConfiguration#recommendationAcceptedEventAdapter}.
 * </p>
 */
public class RecommendationAcceptedEventAdapter
    implements EventLoggingAdapter<RecommendationAcceptedEvent>
{
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public boolean accepts(Object aEvent)
    {
        return aEvent instanceof RecommendationAcceptedEvent;
    }
    
    @Override
    public long getDocument(RecommendationAcceptedEvent aEvent)
    {
        return aEvent.getDocument().getId();
    }
    
    @Override
    public long getProject(RecommendationAcceptedEvent aEvent)
    {
        return aEvent.getDocument().getProject().getId();
    }
    
    @Override
    public String getAnnotator(RecommendationAcceptedEvent aEvent)
    {
        return aEvent.getUser();
    }
    
    @Override
    public String getDetails(RecommendationAcceptedEvent aEvent)
    {
        try {
            AnnotationDetails annotation = new AnnotationDetails(aEvent.getFS());
            
            FeatureChangeDetails details = new FeatureChangeDetails();
            details.setAnnotation(annotation);
            details.setValue(aEvent.getRecommendedValue());

            return JSONUtil.toJsonString(details);
        }
        catch (IOException e) {
            log.error("Unable to log event [{}]", aEvent, e);
            return "<ERROR>";
        }
    }
}
