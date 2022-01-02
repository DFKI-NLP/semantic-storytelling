package de.tudarmstadt.ukp.inception.htmleditor.filter;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.tudarmstadt.ukp.clarin.webanno.model.Tag;
import de.tudarmstadt.ukp.inception.htmleditor.HtmlAnnotationEditor;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class FilterPanel extends Panel {
    private ModalWindow dialog;
    private List<Tag> tagList;
    private Tag activeTag;
    public FilterPanel(String id, List<Tag> aTagList, ModalWindow aDialog){
        super(id);
        this.tagList = aTagList;
        this.activeTag = HtmlAnnotationEditor.getFilter();
        this.dialog = aDialog;
        add(new DropDownChoice<Tag>(
            "filterLabel",
            new Model<Tag>() {
                @Override
                public Tag getObject() {
                    return activeTag;
                }

                @Override
                public void setObject(Tag object) {
                    activeTag = object;
                }
            },
            new LoadableDetachableModel<List<Tag>>() {
                @Override
                protected List<Tag> load() {
                    return tagList;
                }
            }
        ).add(new AjaxFormComponentUpdatingBehavior("change") {
            /**
             * Called when a option is selected of a dropdown list.
             */
            protected void onUpdate(AjaxRequestTarget aTarget) {
                Tag tag = (Tag) getFormComponent().getModelObject();
                HtmlAnnotationEditor.setFilter(tag);
                dialog.close(aTarget);
            }
        }));
        add(new AjaxButton("clearFilter"){
            @Override
            protected void onSubmit(AjaxRequestTarget aTarget){
                super.onSubmit(aTarget);
                HtmlAnnotationEditor.setFilter(null);
                dialog.close(aTarget);
            }
        });
    }
    public void setTagList(List<Tag> aTagList){
        this.tagList = aTagList;
    }
}
