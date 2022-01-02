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
package de.tudarmstadt.ukp.inception.recommendation.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import de.tudarmstadt.ukp.clarin.webanno.api.event.AfterDocumentResetEvent;
import de.tudarmstadt.ukp.clarin.webanno.model.AnnotationFeature;
import de.tudarmstadt.ukp.clarin.webanno.model.AnnotationLayer;
import de.tudarmstadt.ukp.clarin.webanno.model.SourceDocument;
import de.tudarmstadt.ukp.clarin.webanno.security.model.User;
import de.tudarmstadt.ukp.inception.recommendation.api.LearningRecordService;
import de.tudarmstadt.ukp.inception.recommendation.api.model.AnnotationSuggestion;
import de.tudarmstadt.ukp.inception.recommendation.api.model.LearningRecord;
import de.tudarmstadt.ukp.inception.recommendation.api.model.LearningRecordChangeLocation;
import de.tudarmstadt.ukp.inception.recommendation.api.model.LearningRecordType;
import de.tudarmstadt.ukp.inception.recommendation.config.RecommenderServiceAutoConfiguration;

/**
 * <p>
 * This class is exposed as a Spring Component via
 * {@link RecommenderServiceAutoConfiguration#learningRecordService}.
 * </p>
 */
public class LearningRecordServiceImpl
    implements LearningRecordService
{
    private final EntityManager entityManager;

    public LearningRecordServiceImpl(EntityManager aEntityManager)
    {
        entityManager = aEntityManager;
    }
    
    @Transactional
    @EventListener
    public void afterDocumentReset(AfterDocumentResetEvent aEvent) {
        SourceDocument currentDocument = aEvent.getDocument().getDocument();
        String currentUser = aEvent.getDocument().getUser();
        deleteRecords(currentDocument, currentUser);
    }
    
    @Transactional
    @Override
    public void logRecord(SourceDocument aDocument, String aUsername,
            AnnotationSuggestion aSuggestion, AnnotationLayer aLayer, AnnotationFeature aFeature,
            LearningRecordType aUserAction, LearningRecordChangeLocation aLocation)
    {
        logRecord(aDocument, aUsername, aSuggestion, aSuggestion.getLabel(), aLayer,
                aFeature, aUserAction, aLocation);
    }
    
    @Transactional
    @Override
    public void logRecord(SourceDocument aDocument, String aUsername,
            AnnotationSuggestion aSuggestion, String aAlternativeLabel, AnnotationLayer aLayer,
            AnnotationFeature aFeature, LearningRecordType aUserAction, 
            LearningRecordChangeLocation aLocation)
    {
        // It doesn't make any sense at all to have duplicate entries in the learning history,
        // so when adding a new entry, we dump any existing entries which basically are the
        // same as the one added. Mind that the actual action performed by the user does not 
        // matter since there should basically be only one action in the log for any suggestion,
        // irrespective of what that action is.
        String query = String.join("\n",
                "DELETE FROM LearningRecord WHERE",
                "user = :user AND",
                "sourceDocument = :sourceDocument AND",
                "offsetCharacterBegin = :offsetCharacterBegin AND",
                "offsetCharacterEnd = :offsetCharacterEnd AND",
                "layer = :layer AND",
                "annotationFeature = :annotationFeature AND",
                "annotation = :annotation");
        entityManager.createQuery(query)
                .setParameter("user", aUsername)
                .setParameter("sourceDocument", aDocument)
                .setParameter("offsetCharacterBegin", aSuggestion.getBegin())
                .setParameter("offsetCharacterEnd", aSuggestion.getEnd())
                .setParameter("layer", aLayer)
                .setParameter("annotationFeature", aFeature)
                .setParameter("annotation", aAlternativeLabel)
                .executeUpdate();
        
        LearningRecord record = new LearningRecord();
        record.setUser(aUsername);
        record.setSourceDocument(aDocument);
        record.setUserAction(aUserAction);
        record.setOffsetCharacterBegin(aSuggestion.getBegin());
        record.setOffsetCharacterEnd(aSuggestion.getEnd());
        record.setOffsetTokenBegin(-1);
        record.setOffsetTokenEnd(-1);
        record.setTokenText(aSuggestion.getCoveredText());
        record.setAnnotation(aAlternativeLabel);
        record.setLayer(aLayer);
        record.setChangeLocation(aLocation);
        record.setAnnotationFeature(aFeature);

        create(record);
    }

    @Transactional
    @Override
    public List<LearningRecord> listRecords(
            String aUsername, AnnotationLayer aLayer, int aLimit)
    {
        String sql = String.join("\n",
                "FROM LearningRecord l WHERE",
                "l.user = :user AND",
                "l.layer = :layer AND",
                "l.userAction != :action",
                "ORDER BY l.id desc");
        TypedQuery<LearningRecord> query = entityManager.createQuery(sql, LearningRecord.class)
                .setParameter("user", aUsername)
                .setParameter("layer", aLayer)
                .setParameter("action", LearningRecordType.SHOWN); // SHOWN records NOT returned
        if (aLimit > 0) {
            query = query.setMaxResults(aLimit);
        }
        return query.getResultList();
    }
    
    @Transactional
    @Override
    public List<LearningRecord> listRecords(
            String aUsername, AnnotationLayer aLayer)
    {
        return listRecords(aUsername, aLayer, 0);
    }

    @Transactional
    @Override
    public LearningRecord getRecordById(long recordId) {
        String sql = "FROM LearningRecord l where l.id = :id";
        LearningRecord learningRecord = entityManager.createQuery(sql, LearningRecord.class)
                .setParameter("id",recordId)
                .getSingleResult();
        return learningRecord;
    }

    @Transactional
    @Override
    public void deleteRecords(SourceDocument document, String user) {
        String sql = "DELETE FROM LearningRecord l where l.sourceDocument = :document and l.user " +
            "= :user";
        entityManager.createQuery(sql)
            .setParameter("document", document)
            .setParameter("user",user)
            .executeUpdate();
    }

    @Override
    @Transactional
    public void create(LearningRecord learningRecord) {
        entityManager.persist(learningRecord);
        entityManager.flush();
    }

    @Override
    @Transactional
    public void update(LearningRecord learningRecord) {
        entityManager.merge(learningRecord);
        entityManager.flush();
    }

    @Override
    @Transactional
    public void delete(LearningRecord learningRecord) {
        entityManager.remove(entityManager.contains(learningRecord) ? learningRecord :
            entityManager.merge(learningRecord));
    }

    @Override
    @Transactional
    public void deleteById(long recordId) {
        LearningRecord learningRecord = this.getRecordById(recordId);
        if (learningRecord != null) {
            this.delete(learningRecord);
        }
    }
    
    @Override
    @Transactional
    public boolean hasSkippedSuggestions(User aUser, AnnotationLayer aLayer)
    {
        String sql = String.join("\n",
                "SELECT COUNT(*) FROM LearningRecord WHERE",
                "user = :user AND",
                "layer = :layer AND",
                "userAction = :action");
        long count = entityManager.createQuery(sql, Long.class)
                .setParameter("user", aUser.getUsername())
                .setParameter("layer", aLayer)
                .setParameter("action", LearningRecordType.SKIPPED)
                .getSingleResult();
        return count > 0;
    }
    
    @Override
    @Transactional
    public void deleteSkippedSuggestions(User aUser, AnnotationLayer aLayer)
    {
        String sql = String.join("\n",
                "DELETE FROM LearningRecord WHERE",
                "user = :user AND",
                "layer = :layer AND",
                "userAction = :action");
        entityManager.createQuery(sql)
                .setParameter("user", aUser.getUsername())
                .setParameter("layer", aLayer)
                .setParameter("action", LearningRecordType.SKIPPED)
                .executeUpdate();
    }
}
