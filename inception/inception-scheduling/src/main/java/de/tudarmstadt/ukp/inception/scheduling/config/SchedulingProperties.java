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
package de.tudarmstadt.ukp.inception.scheduling.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("inception.scheduling")
public class SchedulingProperties
{
    private int numberOfThreads = 4;
    private int queueSize = 100;

    public int getNumberOfThreads()
    {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int aNumberOfThreads)
    {
        numberOfThreads = aNumberOfThreads;
    }

    public int getQueueSize()
    {
        return queueSize;
    }

    public void setQueueSize(int aQueueSize)
    {
        queueSize = queueSize;
    }
}
