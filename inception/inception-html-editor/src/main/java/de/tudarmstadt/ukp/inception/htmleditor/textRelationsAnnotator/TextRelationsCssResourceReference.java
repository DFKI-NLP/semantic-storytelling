package de.tudarmstadt.ukp.inception.htmleditor.textRelationsAnnotator;

import org.apache.wicket.request.resource.CssResourceReference;

public class TextRelationsCssResourceReference
    extends CssResourceReference
{
    private static final long serialVersionUID = 1L;

    private static final TextRelationsCssResourceReference INSTANCE =
        new TextRelationsCssResourceReference();

    /**
     * Gets the instance of the resource reference
     *
     * @return the single instance of the resource reference
     */
    public static TextRelationsCssResourceReference get()
    {
        return INSTANCE;
    }

    /**
     * Private constructor
     */
    private TextRelationsCssResourceReference()
    {
        super(TextRelationsCssResourceReference.class, "text-relations.css");
    }
}
