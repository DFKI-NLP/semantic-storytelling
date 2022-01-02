/*
 * Copyright 2018
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
package de.tudarmstadt.ukp.inception.recommendation.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

import de.tudarmstadt.ukp.clarin.webanno.api.annotation.model.AnnotatorState;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.model.VID;

public class AjaxRecommendationAcceptedEvent {
    protected AjaxRequestTarget target;
    private AnnotatorState annotatorState;
    private VID vid;

    public AjaxRecommendationAcceptedEvent(AjaxRequestTarget aTarget,
                                           AnnotatorState aAnnotatorState,
                                           VID aVid) {
        this.target = aTarget;
        this.annotatorState = aAnnotatorState;
        this.vid = aVid;
    }

    public AjaxRequestTarget getTarget() {
        return target;
    }

    public AnnotatorState getAnnotatorState() {
        return annotatorState;
    }

    public VID getVid() { return vid; }
}

