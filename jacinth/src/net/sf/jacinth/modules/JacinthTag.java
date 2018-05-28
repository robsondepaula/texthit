/*
 * Created on 18.01.2007
 * @author Stephan Richard Palm
 * Copyright by Stephan Richard Palm
 * 
 */
package net.sf.jacinth.modules;

import javax.swing.text.html.HTML.Tag;

public class JacinthTag {

    final String content;
    final Tag type;
    final int popDepth;
    final int pushDepth;
    
    public JacinthTag(String content, Tag type, int popDepth, int pushDepth) {
        this.content = content;
        this.type = type;
        this.popDepth = popDepth;
        this.pushDepth = pushDepth;
    }
    public String getContent() {
        return content;
    }
    public Tag getType() {
        return type;
    }
    public int getPopDepth() {
        return popDepth;
    }
    public int getPushDepth() {
        return pushDepth;
    }
    
    
    
}
