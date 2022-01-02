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
package de.tudarmstadt.ukp.inception.curation.merge;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.uima.cas.CAS;
import org.springframework.stereotype.Component;

import de.tudarmstadt.ukp.clarin.webanno.api.annotation.model.AnnotatorState;

@Component(ManualMergeStrategy.BEAN_NAME)
public class ManualMergeStrategy implements MergeStrategy
{
    public static final String BEAN_NAME = "manualStrategy";
    
    private String uiName = "Manual";

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof ManualMergeStrategy)) {
            return false;
        }
        ManualMergeStrategy castOther = (ManualMergeStrategy) other;
        return new EqualsBuilder().append(uiName, castOther.uiName).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(uiName).toHashCode();
    }

    @Override
    public void merge(AnnotatorState aState, CAS aCas, Map<String, CAS> aUserCases,
            boolean aMergeIncomplete)
    {
        // Do nothing
    }
    
    @Override
    public String getUiName()
    {
        return uiName;
    }

    public void setUiName(String aUiName)
    {
        uiName = aUiName;
    }
    

}
