package de.tudarmstadt.ukp.inception.htmleditor.statistic;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.HashSet;

public class StatisticLabelModel implements IModel<String> {
    private final StatisticValueModel statistic;
    public StatisticLabelModel(final StatisticValueModel statistic){
        super();
        this.statistic = statistic;
    }

    @Override
    public String getObject() {
        return statistic.getObject()[0] + "%\n" + statistic.getObject()[1] + "/" + statistic.getObject()[2];
    }

    @Override
    public void detach() {
        statistic.detach();
    }

}
