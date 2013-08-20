package sequencegenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_EVENT")
public class SystemEvent {

    /*
     * Starting from Hibernate 4.0, behavior of @SequenceGenerator for ID generation has changed:
     * - with older versions, @SequenceGenerator was mapped to org.hibernate.id.SequenceHiLoGenerator. 
     *   Sequence number was defined as (db_sequence * allocationSize) + increment.
     * - starting from Hibernate 4.0, @SequenceGenerator is mapped to org.hibernate.id.enhanced.SequenceStyleGenerator with an 
     *   implementation of org.hibernate.id.Optimizer depending on allocationSize:
     *   - org.hibernate.id.enhanced.OptimizerFactory.NoopOptimizer when allocationSize = 1. 
     *   - org.hibernate.id.enhanced.OptimizerFactory.PooledOptimizer when allocationSize > 1
     *   
     * NoopOptimizer calls DB sequence for each ID generation.
     * PoolOptimizer calls DB sequence once (gets e.g. 51) and allocates IDs until it reaches the DB sequence value (48, 49, 50)
     *   
     * When using SequenceGenerator, allocationSize must match increment size of DB sequence. 
     * By convention we decide to always set allocationSize = 1 to disable sequence caching on Hibernate and use caching on Oracle. 
     * It is possible to set an arbitrary start value for the sequence (e.g. 1000)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_ID_GENERATOR")
    @SequenceGenerator(name = "EVENT_ID_GENERATOR", sequenceName = "SYS_EVENT_SEQ", allocationSize = 1)
    private Long id;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

}
