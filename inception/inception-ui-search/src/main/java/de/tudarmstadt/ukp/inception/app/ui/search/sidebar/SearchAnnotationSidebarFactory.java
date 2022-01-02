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
package de.tudarmstadt.ukp.inception.app.ui.search.sidebar;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import de.tudarmstadt.ukp.clarin.webanno.api.CasProvider;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.action.AnnotationActionHandler;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.model.AnnotatorState;
import de.tudarmstadt.ukp.clarin.webanno.ui.annotation.AnnotationPage;
import de.tudarmstadt.ukp.clarin.webanno.ui.annotation.sidebar.AnnotationSidebarFactory_ImplBase;
import de.tudarmstadt.ukp.clarin.webanno.ui.annotation.sidebar.AnnotationSidebar_ImplBase;
import de.tudarmstadt.ukp.inception.app.ui.search.config.SearchServiceUIAutoConfiguration;

/**
 * <p>
 * This class is exposed as a Spring Component via
 * {@link SearchServiceUIAutoConfiguration#searchAnnotationSidebarFactory}.
 * </p>
 */
public class SearchAnnotationSidebarFactory
    extends AnnotationSidebarFactory_ImplBase
{
    private static final ResourceReference ICON = new PackageResourceReference(
            SearchAnnotationSidebarFactory.class, "magnifier.png");
    
    @Override
    public String getDisplayName()
    {
        return "Search";
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
        return new SearchAnnotationSidebar(aId, aModel, aActionHandler, aCasProvider,
                aAnnotationPage);
    }
}
