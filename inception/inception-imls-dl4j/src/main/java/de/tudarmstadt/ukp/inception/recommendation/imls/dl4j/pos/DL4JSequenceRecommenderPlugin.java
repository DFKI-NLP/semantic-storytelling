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
package de.tudarmstadt.ukp.inception.recommendation.imls.dl4j.pos;

import static java.util.Collections.singleton;

import java.util.Set;

import org.pf4j.PluginWrapper;

import de.tudarmstadt.ukp.clarin.webanno.plugin.api.Plugin;

public class DL4JSequenceRecommenderPlugin
    extends Plugin
{
    public DL4JSequenceRecommenderPlugin(PluginWrapper aWrapper)
    {
        super(aWrapper);
    }
    
    @Override
    public Set<Class<?>> getSources()
    {
        return singleton(DL4JSequenceRecommenderFactory.class);
    }
}
