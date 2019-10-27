package com.think.runex.java.Models;

public class MultiObject {
    private Object attachedObject;
    private int layoutId = 0;

    public Object getAttachedObject() {
        return attachedObject;
    }

    public void setAttachedObject(Object attachedObject) {
        this.attachedObject = attachedObject;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }
}
