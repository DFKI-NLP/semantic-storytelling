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
package de.tudarmstadt.ukp.inception.curation;

import static de.tudarmstadt.ukp.clarin.webanno.api.annotation.util.WebAnnoCasUtil.selectAnnotationByAddr;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.tudarmstadt.ukp.clarin.webanno.api.AnnotationSchemaService;
import de.tudarmstadt.ukp.clarin.webanno.api.DocumentService;
import de.tudarmstadt.ukp.clarin.webanno.api.WebAnnoConst;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.AnnotationEditorExtension;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.AnnotationEditorExtensionImplBase;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.action.AnnotationActionHandler;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.adapter.TypeAdapter;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.exception.AnnotationException;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.model.AnnotatorState;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.model.VID;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.rendering.PreRenderer;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.rendering.model.VArc;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.rendering.model.VComment;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.rendering.model.VCommentType;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.rendering.model.VDocument;
import de.tudarmstadt.ukp.clarin.webanno.api.annotation.rendering.model.VSpan;
import de.tudarmstadt.ukp.clarin.webanno.curation.casmerge.CasMerge;
import de.tudarmstadt.ukp.clarin.webanno.curation.casmerge.CasMergeOperationResult;
import de.tudarmstadt.ukp.clarin.webanno.model.AnnotationFeature;
import de.tudarmstadt.ukp.clarin.webanno.model.AnnotationLayer;
import de.tudarmstadt.ukp.clarin.webanno.model.Mode;
import de.tudarmstadt.ukp.clarin.webanno.model.SourceDocument;
import de.tudarmstadt.ukp.clarin.webanno.security.UserDao;
import de.tudarmstadt.ukp.clarin.webanno.security.model.User;

@Component(CurationEditorExtension.EXTENSION_ID)
public class CurationEditorExtension
    extends AnnotationEditorExtensionImplBase
    implements AnnotationEditorExtension
{
    public static final String EXTENSION_ID = "curationEditorExtension";
    
    // actions from the ui when selecting span or arc annotation
    private static final String ACTION_SELECT_ARC = "arcOpenDialog"; 
    private static final String ACTION_SELECT_SPAN = "spanOpenDialog";
    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    private @Autowired CurationService curationService;
    private @Autowired PreRenderer preRenderer;
    private @Autowired AnnotationSchemaService annotationService;
    private @Autowired DocumentService documentService;
    private @Autowired UserDao userRepository;
    
    @Override
    public String getBeanName()
    {
        return EXTENSION_ID;
    }

    @Override
    public void handleAction(AnnotationActionHandler aPanel, AnnotatorState aState,
            AjaxRequestTarget aTarget, CAS aCas, VID aParamId, String aAction)
        throws AnnotationException, IOException
    {
        // only process actions relevant to curation
        if (!aParamId.getExtensionId().equals(EXTENSION_ID)) {
            return;
        }
        VID extendedVID = parse(aParamId);
        
        if (!aAction.equals(ACTION_SELECT_ARC) && !aAction.equals(ACTION_SELECT_SPAN)) {
            return;
        }
        // Annotation has been selected for gold
        saveAnnotation(aAction, aPanel, aState, aTarget, aCas, extendedVID);        
    }

    /**
     * Save annotation identified by aVID from user CAS to given curator's CAS
     */
    private void saveAnnotation(String aAction, AnnotationActionHandler aPanel,
            AnnotatorState aState, AjaxRequestTarget aTarget, CAS aTargetCas, VID aVID)
        throws IOException, AnnotationException
    {
        AnnotationLayer layer = annotationService.getLayer(aVID.getLayerId());
        
        // get user CAS and annotation (to be merged into curator's)
        SourceDocument doc = aState.getDocument();
        String srcUser = ((CurationVID) aVID).getUsername();
        
        if (!documentService.existsAnnotationDocument(doc, srcUser)) {
            log.error(
                  String.format("Source CAS of %s for curation not found", srcUser));
            return;
        }
        
        CAS srcCas = documentService.readAnnotationCas(doc, srcUser);
        AnnotationFS sourceAnnotation = selectAnnotationByAddr(srcCas, aVID.getId());
        
        // merge into curator's CAS depending on annotation type (span or arc)
        CasMerge casMerge = new CasMerge(annotationService);
        CasMergeOperationResult mergeResult;
        if (ACTION_SELECT_SPAN.equals(aAction.toString())) {
            mergeResult = casMerge.mergeSpanAnnotation(doc, srcUser, layer, aTargetCas,
                    sourceAnnotation, layer.isAllowStacking());
            // open created/updates FS in annotation detail editorpanel
            aState.getSelection().selectSpan(new VID(mergeResult.getResultFSAddress()), aTargetCas,
                    sourceAnnotation.getBegin(), sourceAnnotation.getEnd());
            
        }
        else if (ACTION_SELECT_ARC.equals(aAction.toString())) {
            // this is a slot arc
            if (aVID.isSlotSet()) {
                TypeAdapter adapter = annotationService.getAdapter(layer);
                AnnotationFeature feature = adapter.listFeatures().stream().sequential()
                        .skip(aVID.getAttribute()).findFirst().get();

                mergeResult = casMerge.mergeSlotFeature(doc, srcUser, layer, aTargetCas,
                        sourceAnnotation, feature.getName(), aVID.getSlot());
                // open created/updates FS in annotation detail editorpanel
                aState.getSelection().selectSpan(new VID(mergeResult.getResultFSAddress()),
                        aTargetCas, sourceAnnotation.getBegin(), sourceAnnotation.getEnd());
            }
            // normal relation annotation arc is clicked
            else {
                mergeResult = casMerge.mergeRelationAnnotation(doc, srcUser, layer, aTargetCas,
                        sourceAnnotation, layer.isAllowStacking());
                // open created/updates FS in annotation detail editorpanel 
                AnnotationFS mergedAnno = selectAnnotationByAddr(aTargetCas,
                        mergeResult.getResultFSAddress());
                Type depType = mergedAnno.getType();
                Feature originFeat = depType.getFeatureByBaseName(WebAnnoConst.FEAT_REL_SOURCE);
                Feature targetFeat = depType.getFeatureByBaseName(WebAnnoConst.FEAT_REL_TARGET);
                AnnotationFS originFS = (AnnotationFS) mergedAnno.getFeatureValue(originFeat);
                AnnotationFS targetFS = (AnnotationFS) mergedAnno.getFeatureValue(targetFeat);
                aState.getSelection().selectArc(new VID(mergeResult.getResultFSAddress()), originFS,
                        targetFS);
            }
        }
        
        aPanel.actionSelect(aTarget);
        aPanel.actionCreateOrUpdate(aTarget, aTargetCas);  //should also update timestamps
    }

    /**
     * Parse extension payload of given VID into CurationVID
     */
    protected VID parse(VID aParamId)
    {
        // format of extension payload is <USER>:<VID> with standard VID format
        // <ID>-<SUB>.<ATTR>.<SLOT>@<LAYER>
        Matcher matcher = Pattern.compile("(?:(?<USER>\\w+)\\:)" 
                + "(?<VID>.+)").matcher(aParamId.getExtensionPayload());
        if (!matcher.matches()) {
            return aParamId;
        }
        
        if (matcher.group("VID") == null || 
                matcher.group("USER") == null ) {
            return aParamId;
        }
        
        String vidStr = matcher.group("VID");
        String username = matcher.group("USER");
        return new CurationVID(aParamId.getExtensionId(), username, 
                VID.parse(vidStr), aParamId.getExtensionPayload());
    }

    @Override
    public void render(CAS aCas, AnnotatorState aState, VDocument aVdoc, int aWindowBeginOffset,
            int aWindowEndOffset)
    {
        if (!aState.getMode().equals(Mode.ANNOTATION)) {
            return;
        }
        
        // check if user already finished with this document
        User currentUser = userRepository.getCurrentUser();
        if (documentService.isAnnotationFinished(aState.getDocument(), currentUser)) {
            return;
        }
        
        //check annotatorstate metadata if user is currently curating for this project
        long projectId = aState.getProject().getId();
        Boolean isCurating = aState.getMetaData(CurationMetadata.CURATION_USER_PROJECT);
        if (isCurating == null || !isCurating) {
            return;
        }
            
        List<User> selectedUsers = curationService
                .listUsersReadyForCuration(currentUser.getUsername(), 
                        aState.getProject(), aState.getDocument());
        if (selectedUsers.isEmpty()) {
            return;
        }

        for (User user : selectedUsers) {
                        
            try {
                CAS userCas = documentService.readAnnotationCas(aState.getDocument(),
                        user.getUsername());
                if (userCas == null) {
                    log.error(String.format("Could not retrieve CAS for user %s and project %d",
                            user.getUsername(), projectId));
                    continue;
                }
                VDocument tmpDoc = new VDocument();
                preRenderer.render(tmpDoc, aWindowBeginOffset, aWindowEndOffset, userCas,
                        aState.getAnnotationLayers());
                
                String username = user.getUsername();
                String color = "#ccccff";  //"#cccccc" is the color for recommendations

                // copy all arcs and spans to existing doc with new VID
                
                // copy all spans and add to map as possible varc dependents
                // spans with new vids identified by their old vid for lookup in varcs
                Map<VID, VSpan> newIdSpan = new HashMap<>();
                for (VSpan vspan : tmpDoc.spans()) {
                    VID aDepVID = vspan.getVid();
                    VID prevVID = VID.copyVID(aDepVID);
                    VID newVID = new CurationVID(EXTENSION_ID, username,
                            new VID(vspan.getLayer().getId(), aDepVID.getId(), aDepVID.getSubId(),
                                    aDepVID.getAttribute(), aDepVID.getSlot()));
                    vspan.setVid(newVID);
                    vspan.setColorHint(color);
                    // TODO: might be better to change after bugfix #1389
                    vspan.setLazyDetails(Collections.emptyList());
                    newIdSpan.put(prevVID, vspan);
                    // set user name as comment
                    aVdoc.add(new VComment(newVID, VCommentType.INFO, username));
                    aVdoc.add(vspan);
                }

                // copy arcs to VDoc
                for (VArc varc : tmpDoc.arcs()) {
                    // update varc vid
                    VID vid = varc.getVid();
                    VID extendedVID = new CurationVID(EXTENSION_ID, username,
                            new VID(varc.getLayer().getId(), vid.getId(), vid.getSubId(),
                                    vid.getAttribute(), vid.getSlot()));
                    // set target and src with new vids for arc
                    VSpan targetSpan = newIdSpan.get(varc.getTarget());
                    VSpan srcSpan = newIdSpan.get(varc.getSource());
                    VArc newVarc = new VArc(varc.getLayer(),extendedVID, varc.getType(), 
                            srcSpan.getVid(), targetSpan.getVid(), varc.getLabelHint(), 
                            varc.getFeatures(), color);
                    // set user name as comment
                    aVdoc.add(new VComment(extendedVID, VCommentType.INFO, username));
                    aVdoc.add(newVarc);
                }

            }
            catch (IOException e) {
                log.error(String.format("Could not retrieve CAS for user %s and project %d",
                        user.getUsername(), projectId));
                e.printStackTrace();
            }

        }
    }

}
