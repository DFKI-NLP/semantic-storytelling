package de.tudarmstadt.ukp.inception.htmleditor.progress;

import de.tudarmstadt.ukp.clarin.webanno.model.Tag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class ProgressModel implements IModel<int[]> {
    private final Model<HashSet<Integer>> taggedPairsModel;
    private final int totalSize;
    public ProgressModel(final Model<HashSet<Integer>> taggedPairsModel, int totalSize){
        super();
        this.taggedPairsModel = taggedPairsModel;
        this.totalSize = totalSize;
    }

    @Override
    public int[] getObject() {
        int[] result = {0, 0, 0};
        if(totalSize < 1){
            return result;
        }
        result[0] = (int)Math.round((double)taggedPairsModel.getObject().size() / ((double)totalSize) * 100);
        result[1] = (int)taggedPairsModel.getObject().size();
        result[2] = (int)totalSize;
        return result;
    }

    @Override
    public void detach() {
        taggedPairsModel.detach();
    }


}
