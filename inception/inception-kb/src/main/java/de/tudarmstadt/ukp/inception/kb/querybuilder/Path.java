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
package de.tudarmstadt.ukp.inception.kb.querybuilder;

import static java.util.stream.Collectors.joining;

import java.util.Arrays;

import org.eclipse.rdf4j.sparqlbuilder.core.QueryElement;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.rdf.RdfPredicate;

/**
 * Construct property paths for use with the {@link SparqlBuilder}
 */
public class Path
{
    public static RdfPredicate of(QueryElement... aElements)
    {
        return () -> Arrays.stream(aElements)
                .map(QueryElement::getQueryString)
                .collect(joining("/"));
    }

    public static QueryElement zeroOrMore(QueryElement aElement)
    {
        return () -> aElement.getQueryString() + "*";
    }

    public static QueryElement oneOrMore(QueryElement aElement)
    {
        return () -> aElement.getQueryString() + "+";
    }
}
