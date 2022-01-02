package de.tudarmstadt.ukp.inception.htmleditor.statistic;

import de.tudarmstadt.ukp.inception.htmleditor.progress.ProgressIndicatorCSSModel;
import de.tudarmstadt.ukp.inception.htmleditor.progress.ProgressLabelModel;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class StatisticValuePanel extends Panel {
    private StatisticLabelModel label;
    private StatisticIndicatorCSSModel css;
    private StatisticValueModel valueModel;
    private String name;
    private int value;
    private int totalNumber = 0;
    public StatisticValuePanel(String id, String aName, int aValue, int aTotalNumber){
        super(id);
        this.name = aName;
        this.value = aValue;
        this.totalNumber = aTotalNumber;
        createModels();
        // Add HTML
        add(new Label("name", this.name));
        add(new Label("label", this.label));
        WebMarkupContainer indicator = new WebMarkupContainer("indicator");
        indicator.add(new AttributeAppender("style", css));
        add(indicator);
    }
    public void setNewValues(int aValue, int aTotalNumber){
        this.value = aValue;
        this.totalNumber = aTotalNumber;
        this.valueModel.setValues(aValue, aTotalNumber);
    }
    private void createModels(){
        this.valueModel = new StatisticValueModel(this.value, this.totalNumber);
        this.label = new StatisticLabelModel(this.valueModel);
        this.css = new StatisticIndicatorCSSModel(this.valueModel);
    }
}
