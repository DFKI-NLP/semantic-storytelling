package de.tudarmstadt.ukp.inception.htmleditor.meta;

import de.tudarmstadt.ukp.clarin.webanno.api.annotation.feature.editor.FeatureEditor;
import de.tudarmstadt.ukp.clarin.webanno.model.Tag;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.util.List;

public class SegmentAnnotationFeatureModel implements
    IModel<String> {
    private final List<AnnotationFS> segments;
    private final String featureName;
    private final IModel<Integer> id;
    public SegmentAnnotationFeatureModel(final List<AnnotationFS> segments,final Model<Integer> id, final String featureName){
        super();
        this.segments = segments;
        this.featureName = featureName;
        this.id = id;
    }

    @Override
    public String getObject() {
        int id = this.id.getObject();
        Feature feature = segments.get(id).getType().getFeatureByBaseName(featureName);
        return segments.get(id).getFeatureValueAsString(feature);
    }
    @Override
    public void detach() {
        id.detach();
    }
}