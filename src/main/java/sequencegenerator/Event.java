package sequencegenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_EVENT")
public class Event {

    @Id
    @SequenceGenerator(name = "EVENT_ID_GENERATOR", sequenceName = "EVENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_ID_GENERATOR")
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
