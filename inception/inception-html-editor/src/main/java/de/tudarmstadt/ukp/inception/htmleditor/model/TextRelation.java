package de.tudarmstadt.ukp.inception.htmleditor.model;

import de.tudarmstadt.ukp.clarin.webanno.model.Tag;

import java.io.Serializable;

public class TextRelation implements Serializable {
    private String sentence1, sentence2;
    private Tag relationRight, relationLeft;

    // Constructors
    public TextRelation(String aSentence1, String aSentence2){
        this.sentence1 = aSentence1;
        this.sentence2 = aSentence2;
    }
    public TextRelation(String aSentence1, String aSentence2, Tag aRelationRight, Tag aRelationLeft){
        this.sentence1 = aSentence1;
        this.sentence2 = aSentence2;
        this.relationRight = aRelationRight;
        this.relationLeft = aRelationLeft;
    }

    // Getter & Setter
    public void setSentences(String sentence1, String sentence2){
        this.sentence1 = sentence1;
        this.sentence2 = sentence2;
    }
    public void setRelationRight(Tag aRelationRight){ this.relationRight = aRelationRight; }
    public void setRelationLeft(Tag aRelationLeft){ this.relationLeft = aRelationLeft; }
    public Tag getRelationRight(){
        return this.relationRight;
    }
    public Tag getRelationLeft(){
        return this.relationLeft;
    }

    @Override
    public String toString() {
        String relRight = "null", relLeft = "null";
        if(this.relationRight != null){
            relRight = this.relationRight.getName();
        }
        if(this.relationLeft != null){
            relLeft = this.relationLeft.getName();
        }
        return "TextRelation {\n" +
            " sentence1 = " + sentence1 +
            ",\n sentence2 = " + sentence2 +
            ",\n relationRight = " + relRight +
            ",\n relationLeft = " + relLeft +
            "\n}";
    }
}
