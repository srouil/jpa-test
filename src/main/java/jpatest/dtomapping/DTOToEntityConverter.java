package jpatest.dtomapping;

import javax.persistence.EntityManager;

import org.dozer.CustomConverter;

public class DTOToEntityConverter implements CustomConverter {

    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public Object convert(Object destination, Object source, Class<?> destinationClass, Class<?> sourceClass) {

        BaseDTO dto = (BaseDTO) source;
        return em.find(destinationClass, dto.getId());
    }

}
