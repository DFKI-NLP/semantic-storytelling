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
package de.tudarmstadt.ukp.inception.log;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import de.tudarmstadt.ukp.inception.log.exporter.LoggedEventExporter;

@SpringBootConfiguration
@ComponentScan(
        basePackages = {
            "de.tudarmstadt.ukp.clarin.webanno.webapp",
            "de.tudarmstadt.ukp.inception"
        },
        excludeFilters = {
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { 
                        LoggedEventExporter.class
                })
        })
@EntityScan(
        basePackages = {
            "de.tudarmstadt.ukp.inception.log.model",
            "de.tudarmstadt.ukp.clarin.webanno.security.model",
            "de.tudarmstadt.ukp.clarin.webanno.model"
})
@EnableAutoConfiguration
public class SpringConfig
{
    // No content
}


