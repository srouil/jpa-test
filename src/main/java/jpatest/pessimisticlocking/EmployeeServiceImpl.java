package jpatest.pessimisticlocking;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

@Stateless
public class EmployeeServiceImpl {

    @PersistenceContext
    EntityManager em;

    public void adjustEmployeeVacation(Long employeeId, int vacationDays) {

        Employee e = em.find(Employee.class, employeeId, LockModeType.PESSIMISTIC_WRITE);
        e.setVacationDays(e.getVacationDays() + vacationDays);
    }
}
