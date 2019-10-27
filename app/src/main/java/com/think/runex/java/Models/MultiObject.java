package com.think.runex.java.Models;

public class MultiObject {
    private Object attachedObject;
    private int layoutId = 0;
    private int layoutTypeId = 0;

    public int getLayoutTypeId() {
        return layoutTypeId;
    }

    public MultiObject setLayoutTypeId(int layoutTypeId) {
        this.layoutTypeId = layoutTypeId;
        return this;
    }

    public Object getAttachedObject() {
        return attachedObject;
    }

    public void setAttachedObject(Object attachedObject) {
        this.attachedObject = attachedObject;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public MultiObject setLayoutId(int layoutId) {
        this.layoutId = layoutId;
        return this;
    }
}
