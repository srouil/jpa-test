package jpatest.jodatime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
//@formatter:off
@NamedQueries(value = { 
        @NamedQuery(
                name = Event.SELECT_EVENTS_AFTER, 
                query = "select e from Event e where e.createTs > :instant"),
        @NamedQuery(
                name = Event.SELECT_EVENTS_IN_YEAR, 
                query = "select e from Event e where YEAR(e.createTs) = YEAR(:instant)"),
              
})
//@formatter:on
public class Event {

    public static final String SELECT_EVENTS_AFTER = "selectEventsAfter";
    public static final String SELECT_EVENTS_IN_YEAR = "selectEventsInYear";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "create_ts", nullable = false)
    private DateTime createTs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DateTime getCreateTs() {
        return createTs;
    }

    public void setCreateTs(DateTime createTs) {
        this.createTs = createTs;
    }

}
