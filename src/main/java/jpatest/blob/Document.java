package jpatest.blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
//@formatter:off
@NamedQueries({ 
    
    @NamedQuery(name = Document.SELECT_ALL_DOCUMENTS,             
        query = "SELECT new Document(d.id, d.fileName, d.description, d.version) " +
                "FROM Document d")
})    
//@formatter:on
public class Document {

    public static final String SELECT_ALL_DOCUMENTS = "selectAllDocuments";

    @Id
    @SequenceGenerator(name = "DOCUMENT_ID_GENERATOR", sequenceName = "DOCUMENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_ID_GENERATOR")
    private Long id;

    private String fileName;

    private String description;
    
    @Lob()
    private byte[] binaryData;

    // createTs, createUser, ... omitted
    
    @Version
    @Column(name = "VERSION")
    private Integer version;

    /**
     * Default constructor
     */
    public Document() {
    }

    /**
     * Constructor used in "select new entity" named queries
     */
    public Document(Long id, String fileName, String description, Integer version) {
        this.id = id;
        this.fileName = fileName;
        this.description = description;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
}
