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
