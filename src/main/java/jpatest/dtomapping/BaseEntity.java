package jpatest.dtomapping;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity {

    public static final int PRIME = 31;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public abstract Long getId();

    /**
     * Needed by Find-Map strategy so that Dozer updates list Element instead of adding a new one.
     * When equals is not implemented to compare primary or business key, Hibernate will throw
     * org.hibernate.PersistentObjectException: detached entity passed to persist: jpatest.dtomapping.Employee
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseEntity other = (BaseEntity) obj;
        if (getId() != null && other.getId() != null) {
            return getId().equals(other.getId());
        }
        return false;
    }

    /**
     * Overridden to maintain the general contract for the hashCode method (equal objects must have equal hash codes) 
     */
    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

}
