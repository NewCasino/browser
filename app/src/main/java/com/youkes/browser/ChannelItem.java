
package com.youkes.browser;

import java.io.Serializable;

public class ChannelItem implements Serializable {

    public String id;

    private String siteId;

    public String name;

    public Integer orderId;

    public Integer selected;

    public ChannelItem() {
    }

    public ChannelItem(String siteId, String id, String name, int orderId, int selected) {
        this.siteId=siteId;
        this.id = id;
        this.name = name;
        this.orderId = Integer.valueOf(orderId);
        this.selected = Integer.valueOf(selected);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getOrderId() {
        return this.orderId.intValue();
    }

    public Integer getSelected() {
        return this.selected;
    }

    public void setId(String paramInt) {
        this.id = paramInt;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setOrderId(int paramInt) {
        this.orderId = Integer.valueOf(paramInt);
    }

    public void setSelected(Integer paramInteger) {
        this.selected = paramInteger;
    }

    @Override
    public String toString() {
        return "ChannelItem [id=" + this.id + ", name=" + this.name
                + ", selected=" + this.selected + "]";
    }

    public String getSiteId() {
        return siteId;
    }
}
