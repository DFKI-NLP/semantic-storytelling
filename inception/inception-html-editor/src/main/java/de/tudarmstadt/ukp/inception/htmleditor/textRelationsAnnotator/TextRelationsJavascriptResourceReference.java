package de.tudarmstadt.ukp.inception.htmleditor.textRelationsAnnotator;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class TextRelationsJavascriptResourceReference
    extends JavaScriptResourceReference {
    private static final long serialVersionUID = 1L;

    private static final TextRelationsJavascriptResourceReference INSTANCE =
        new TextRelationsJavascriptResourceReference();

    /**
     * Gets the instance of the resource reference
     *
     * @return the single instance of the resource reference
     */
    public static TextRelationsJavascriptResourceReference get()
    {
        return INSTANCE;
    }

    /**
     * Private constructor
     */
    private TextRelationsJavascriptResourceReference()
    {
        super(TextRelationsJavascriptResourceReference.class, "text-relations.js");
    }
}
