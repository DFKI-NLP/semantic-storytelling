package de.tudarmstadt.ukp.inception.htmleditor.statistic;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class StatisticIndicatorCSSModel implements IModel<String> {
    private static final int HEIGHT = 100;
    private final StatisticValueModel statistic;
    public StatisticIndicatorCSSModel(final StatisticValueModel statistic){
        super();
        this.statistic = statistic;
    }
    @Override
    public String getObject() {
        return "height:" + ((statistic.getObject()[0] * HEIGHT) / 100) + "px";
    }

    @Override
    public void detach() {
        statistic.detach();
    }
}
