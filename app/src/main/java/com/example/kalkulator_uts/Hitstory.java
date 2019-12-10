package com.example.kalkulator_uts;

public class Hitstory
{
    String id;
    String history;

    public Hitstory(String id, String history)
    {
        this.id = id;
        this.history = history;
    }
    public Hitstory()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
