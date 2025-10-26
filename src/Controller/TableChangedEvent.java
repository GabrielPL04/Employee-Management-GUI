package Controller;

public
    class TableChangedEvent {

    private String title;
    private Object[][] data;

    public TableChangedEvent(String title, Object[][] data) {
        this.title = title;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public Object[][] getData() {
        return data;
    }
}
