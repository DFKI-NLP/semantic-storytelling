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
package de.tudarmstadt.ukp.inception.active.learning.event;

import org.springframework.context.ApplicationEvent;

import de.tudarmstadt.ukp.clarin.webanno.model.Project;

public class ActiveLearningSessionCompletedEvent
    extends ApplicationEvent
{
    private static final long serialVersionUID = -4736560772442881663L;

    private final Project project;
    private final String user;

    public ActiveLearningSessionCompletedEvent(Object source, Project aProject, String aUser)
    {
        super(source);
        project = aProject;
        user = aUser;
    }

    public Project getProject()
    {
        return project;
    }

    public String getUser()
    {
        return user;
    }
}
