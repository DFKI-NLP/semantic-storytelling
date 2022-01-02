/*
 * Copyright 2020
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

package de.tudarmstadt.ukp.inception.workload.dynamic.support;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import de.tudarmstadt.ukp.clarin.webanno.model.SourceDocument;


public class WorkloadMetadataDialog extends Panel
{

    private static final long serialVersionUID = 2797336810690526392L;

    public WorkloadMetadataDialog(String aID, SourceDocument aDocument,
                                  List<String> finishedUsers, List<String> inProgressUsers)
    {
        super(aID);

        //TODO more content, e.g. created date
        Label documentName = new Label("documentName", aDocument.getName());
        Label userInProgress = new Label("userInProgress",
            inProgressUsers.isEmpty() ? "-" : inProgressUsers.stream().collect(Collectors.joining(", ")));
        Label userFinished = new Label("userFinished",
            finishedUsers.isEmpty() ? "-" : finishedUsers.stream().collect(Collectors.joining(", ")));

        add(documentName);
        add(userInProgress);
        add(userFinished);
    }
}
