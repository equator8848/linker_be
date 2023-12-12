package xyz.equator8848.linker.model.constant;

public enum DeleteType {
    SOFT_DELETE("逻辑删除"),

    HARD_DELETE("物理删除");

    private String name;

    DeleteType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
