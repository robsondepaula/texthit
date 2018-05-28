/*
 * Created on 15.02.2007
 * @author Stephan Richard Palm
 * Copyright by Stephan Richard Palm
 * 
 */
package net.sf.jacinth;

import net.sf.jacinth.modules.Module;

public class ObjectListenerEvent {

    final private Module module;
    final private Object prevObj;
    final private Object newObj;
    
    public ObjectListenerEvent(final Module module, final Object prevObj, final Object newObj) {
        this.module = module;
        this.prevObj = prevObj;
        this.newObj = newObj;
    }
    
    public Module getModule() {
        return module;
    }
    public Object getNewObject() {
        return newObj;
    }
    public Object getPrevObject() {
        return prevObj;
    }
    
    
}
