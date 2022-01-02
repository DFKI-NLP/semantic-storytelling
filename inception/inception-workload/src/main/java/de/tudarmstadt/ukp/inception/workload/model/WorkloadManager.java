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
package de.tudarmstadt.ukp.inception.workload.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.tudarmstadt.ukp.clarin.webanno.model.Project;
import de.tudarmstadt.ukp.inception.workload.config.WorkloadManagementAutoConfiguration;

/**
 * <p>
 * This class is exposed as a Spring Component via
 * {@link WorkloadManagementAutoConfiguration#workloadManager}.
 * A persistence object for the workflow and workload properties of each project
 * </p>
 */
@Entity
@Table(name = "workload_manager", uniqueConstraints = { @UniqueConstraint(columnNames = {
    "project", "workloadType" }) })
public class WorkloadManager implements Serializable
{
    private static final long serialVersionUID = -3289504168531309833L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project", nullable = false)
    private Project project;

    @Column(columnDefinition = "VARCHAR(255)")
    private String workloadType;

    @Lob
    @Column(length = 64000)
    private String traits;

    public WorkloadManager()
    {

    }

    public WorkloadManager(Project aProject, String aWorkloadType, String aTraits)
    {
        project = aProject;
        workloadType = aWorkloadType;
        traits = aTraits;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long aId)
    {
        id = aId;
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject(Project aProject)
    {
        project = aProject;
    }

    public String getType() {
        return workloadType;
    }

    public void setType(String aWorkloadType) {
        workloadType = aWorkloadType;
    }

    public String getTraits()
    {
        return traits;
    }

    public void setTraits(String aTraits)
    {
        traits = aTraits;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof WorkloadManager)) {
            return false;
        }
        WorkloadManager castOther = (WorkloadManager) other;
        return Objects.equals(project, castOther.project)
            && Objects.equals(workloadType, castOther.workloadType);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(project, workloadType);
    }
}
