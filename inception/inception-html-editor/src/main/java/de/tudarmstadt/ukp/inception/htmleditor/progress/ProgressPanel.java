package de.tudarmstadt.ukp.inception.htmleditor.progress;

import de.tudarmstadt.ukp.inception.htmleditor.model.Pair;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.HashSet;
import java.util.List;

public class ProgressPanel extends Panel {
    List<Pair<Integer, Integer>> possiblePairs;
    HashSet<Integer> taggedPairs;
    Model<HashSet<Integer>> taggedPairsModel;
    ProgressModel progress;
    public ProgressPanel(String id, HashSet<Integer> taggedPairs, List<Pair<Integer, Integer>> possiblePairs){
        super(id);
        this.possiblePairs = possiblePairs;
        this.taggedPairs = taggedPairs;
        this.taggedPairsModel = new Model<>(this.taggedPairs);
        this.progress = new ProgressModel(this.taggedPairsModel, possiblePairs.size());
        add(new Label("label", new ProgressLabelModel(this.progress)));
        WebMarkupContainer indicator = new WebMarkupContainer("indicator");
        indicator.add(new AttributeAppender("style", new ProgressIndicatorCSSModel(this.progress)));
        add(indicator);
    }

    public void setTaggedPairs(HashSet<Integer> taggedPairs) {
        this.taggedPairs = taggedPairs;
        this.taggedPairsModel.setObject(this.taggedPairs);
    }
    public HashSet<Integer> getTaggedPairs(){
        return this.taggedPairs;
    }
}
