package sequencegenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "ALERT_EVENT")
public class AlertEvent {

    /*
     * Hibernate-specific mapping equivalent to following JPA annotation when using Hibernate 3.x.
     * 
     * @Id
     * @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_ID_GENERATOR") // strategy is important, when not specified org.hibernate.id.SequenceGenerator is used
     * @SequenceGenerator(name = "EVENT_ID_GENERATOR", sequenceName = "ALERT_EVENT_SEQ")
     * 
     * SequenceHiLoGenerator multiply value obtained by sequence with allocation size (Hi) and increments an internate counter (Lo).
     * ID is defined as (db_sequence * allocationSize) + counter
     */
    //@formatter:off
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_SEQ_GEN")
    @GenericGenerator(
            name = "GEN_SEQ_GEN", 
            strategy = "org.hibernate.id.SequenceHiLoGenerator", 
            parameters = { 
                    @Parameter(name = "sequence", value = "ALERT_EVENT_SEQ"),
                    @Parameter(name = "max_lo", value = "49") 
    })
    //@formatter:on
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
