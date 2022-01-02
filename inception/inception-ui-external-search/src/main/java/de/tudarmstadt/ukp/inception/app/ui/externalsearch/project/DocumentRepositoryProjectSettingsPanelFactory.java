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
package de.tudarmstadt.ukp.inception.app.ui.externalsearch.project;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.springframework.core.annotation.Order;

import de.tudarmstadt.ukp.clarin.webanno.model.Project;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.settings.ProjectSettingsPanelFactory;
import de.tudarmstadt.ukp.inception.app.ui.externalsearch.config.ExternalSearchUIAutoConfiguration;

/**
 * Project settings panel to configure document repositories for the external search.
 * <p>
 * This class is exposed as a Spring Component via
 * {@link ExternalSearchUIAutoConfiguration#documentRepositoryProjectSettingsPanelFactory()}.
 * </p>
 */
@Order(450)
public class DocumentRepositoryProjectSettingsPanelFactory
    implements ProjectSettingsPanelFactory
{
    @Override
    public String getPath()
    {
        return "/search";
    }

    @Override
    public String getLabel()
    {
        return "Document Repositories";
    }

    @Override
    public Panel createSettingsPanel(String aID, final IModel<Project> aProjectModel)
    {
        return new ProjectDocumentRepositoriesPanel(aID, aProjectModel);
    }
}
