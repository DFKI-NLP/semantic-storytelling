package de.tudarmstadt.ukp.inception.htmleditor.model;

import de.tudarmstadt.ukp.clarin.webanno.api.AnnotationSchemaService;
import de.tudarmstadt.ukp.clarin.webanno.model.AnnotationFeature;
import de.tudarmstadt.ukp.clarin.webanno.model.AnnotationLayer;
import de.tudarmstadt.ukp.inception.htmleditor.HtmlAnnotationEditor;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class RelationFeatureSupplier implements Supplier {
    private static final Logger LOG = LoggerFactory.getLogger(HtmlAnnotationEditor.class);
    private AnnotationLayer relationLayer;
    private @SpringBean AnnotationSchemaService annotationService;
    public RelationFeatureSupplier(AnnotationLayer aRelationLayer){
        this.relationLayer = aRelationLayer;
    }

    @Override
    public Object get() {
        Collection<AnnotationFeature> list = new ArrayList<>();
        for (AnnotationFeature feat : annotationService.listSupportedFeatures(relationLayer)) {
            // Get Feature(s) -> should be one (with tagset)
            if(feat.getTagset() != null){
                list.add(feat);
            }
        }
        return list;
    }
}
