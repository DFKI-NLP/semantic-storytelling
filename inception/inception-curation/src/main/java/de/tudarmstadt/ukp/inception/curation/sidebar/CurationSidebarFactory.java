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
package de.tudarmstadt.ukp.inception.curation.sidebar;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import de.tudarmstadt.ukp.clarin.webanno.api.CasProvider;
import de.tudarmstadt.ukp.clarin.webanno.api.ProjectService;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.action.AnnotationActionHandler;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.model.AnnotatorState;
import de.tudarmstadt.ukp.clarin.webanno.ui.annotation.AnnotationPage;
import de.tudarmstadt.ukp.clarin.webanno.ui.annotation.sidebar.AnnotationSidebarFactory_ImplBase;
import de.tudarmstadt.ukp.clarin.webanno.ui.annotation.sidebar.AnnotationSidebar_ImplBase;

@Component("curationSidebar")
@ConditionalOnProperty(prefix = "curation.sidebar", name = "enabled", havingValue = "true")
public class CurationSidebarFactory
    extends AnnotationSidebarFactory_ImplBase
{

    private static final ResourceReference ICON = new PackageResourceReference(
            CurationSidebarFactory.class, "data_table.png");
    
    private @Autowired ProjectService projectService;
    
    @Override
    public String getDisplayName()
    {
        return "Curation";
    }

    @Override
    public ResourceReference getIcon()
    {
        return ICON;
    }

    @Override
    public AnnotationSidebar_ImplBase create(String aId, IModel<AnnotatorState> aModel,
            AnnotationActionHandler aActionHandler, CasProvider aCasProvider,
            AnnotationPage aAnnotationPage)
    {
        return new CurationSidebar(aId, aModel, aActionHandler, aCasProvider,
                aAnnotationPage);
    }

    @Override
    public boolean applies(AnnotatorState aState)
    {
        boolean isCurator = projectService.isCurator(aState.getProject(), aState.getUser()); 
        return isCurator;
    }

}
