package ru.otus.model;

import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public ObjectForMessage() {
    }

    public ObjectForMessage(ObjectForMessage objectForMessage) {
        this.data = List.copyOf(objectForMessage.getData());
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
