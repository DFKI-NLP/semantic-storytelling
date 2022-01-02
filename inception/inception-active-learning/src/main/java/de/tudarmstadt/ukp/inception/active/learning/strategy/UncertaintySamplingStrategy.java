/*
 * Copyright 2017
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
package de.tudarmstadt.ukp.inception.active.learning.strategy;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import de.tudarmstadt.ukp.inception.recommendation.api.model.SuggestionGroup;
import de.tudarmstadt.ukp.inception.recommendation.api.model.SuggestionGroup.Delta;

public class UncertaintySamplingStrategy
    implements Serializable, ActiveLearningStrategy
{
    private static final long serialVersionUID = 5664120040399862552L;

    @Override
    public Optional<Delta> generateNextSuggestion(List<SuggestionGroup> suggestions)
    {
        return suggestions.stream()
            // Fetch the top deltas per recommender
            .flatMap(group -> group.getTopDeltas().values().stream())
            // ... sort them in ascending order (smallest delta first)
            .sorted(Comparator.comparingDouble(Delta::getDelta))
            // ... and return the smallest delta (if there is one)
            .findFirst();
    }
}
