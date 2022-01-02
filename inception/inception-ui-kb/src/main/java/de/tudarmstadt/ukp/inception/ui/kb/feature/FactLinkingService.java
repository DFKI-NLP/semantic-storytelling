/*
 * Copyright 2018
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tudarmstadt.ukp.inception.ui.kb.feature;

import java.util.List;

import org.apache.uima.cas.CAS;

import de.tudarmstadt.ukp.clarin.webanno.model.Project;
import de.tudarmstadt.ukp.inception.kb.ConceptFeatureTraits;
import de.tudarmstadt.ukp.inception.kb.graph.KBHandle;
import de.tudarmstadt.ukp.inception.kb.graph.KBProperty;
import de.tudarmstadt.ukp.inception.kb.model.KnowledgeBase;

public interface FactLinkingService
{
    List<KBProperty> listProperties(Project aProject, ConceptFeatureTraits traits);

    KBHandle getKBHandleFromCasByAddr(CAS aCas, int targetAddr, Project aProject,
        ConceptFeatureTraits traits);

    KnowledgeBase findKnowledgeBaseContainingProperty(KBProperty aProperty, Project aProject,
        ConceptFeatureTraits traits);

    ConceptFeatureTraits getFeatureTraits(Project aProject);

    KBHandle getKBInstancesByIdentifierAndTraits(String kbHandleIdentifier, Project aProject,
        ConceptFeatureTraits traits);
}
