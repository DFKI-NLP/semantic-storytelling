package de.tudarmstadt.ukp.inception.htmleditor.statistic;

import de.tudarmstadt.ukp.clarin.webanno.model.Tag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class StatisticValueModel implements IModel<int[]> {
    private int value;
    private int totalNumber;
    public StatisticValueModel(int aValue, int aTotalNumber){
        super();
        this.value = aValue;
        this.totalNumber = aTotalNumber;
    }
    public void setValues(int aValue, int aTotalNumber){
        this.value = aValue;
        this.totalNumber = aTotalNumber;
    }

    @Override
    public int[] getObject() {
        int[] result = {0, 0, 0};
        if(totalNumber < 1){
            return result;
        }
        result[0] = (int)Math.round((double)value / (double)totalNumber * 100);
        result[1] = (int)value;
        result[2] = (int)totalNumber;
        return result;
    }

}
