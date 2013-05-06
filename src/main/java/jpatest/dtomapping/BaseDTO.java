package jpatest.dtomapping;

public abstract class BaseDTO {

    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public abstract Long getId();
}
