package de.tudarmstadt.ukp.inception.htmleditor.statistic;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

public class StatisticPanel extends Panel {
    private StatisticValuePanel[] valuePanel;
    public StatisticPanel(String id, String[] aNames, int[] aValues, int aTotalNumber){
        super(id);
        this.valuePanel = new StatisticValuePanel[aNames.length];
        RepeatingView listItems = new RepeatingView("item");
        for (int i = 0; i < aNames.length; i++) {
            this.valuePanel[i] = new StatisticValuePanel(listItems.newChildId(), aNames[i], aValues[i], aTotalNumber);
            listItems.add(this.valuePanel[i]);
        }
        add(listItems);
    }
    public void setNewValues(int[] aValues, int aTotalNumber){
        for (int i = 0; i < aValues.length; i++) {
            this.valuePanel[i].setNewValues(aValues[i], aTotalNumber);
        }
    }
}
