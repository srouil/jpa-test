package jpatest.blobstreaming;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Entity shows an example of LOB attribute mapped as Blob because it is considered as too large to be mapped as byte array (and hence held in memory)
 */
@Entity
@Table(name = "large_document")
public class LargeDocument {

    @Id
    @GeneratedValue(generator = "DOCUMENT_ID_GENERATOR")
    @SequenceGenerator(name = "DOCUMENT_ID_GENERATOR", sequenceName = "DOCUMENT_SEQ", allocationSize = 1)
    private Long id;

    private String fileName;

    private String contentType;

    private String description;

    @Lob
    private Blob binaryData;

    // createTs, createUser, ... omitted

    @Version
    @Column(name = "VERSION")
    private Integer version;

    /**
     * Default constructor
     */
    public LargeDocument() {
    }

    /**
     * Constructor used in "select new entity" named queries
     */
    public LargeDocument(Long id, String fileName, String mimeType, String description, Integer version) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = mimeType;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String mimeType) {
        this.contentType = mimeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Blob getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(Blob binaryData) {
        this.binaryData = binaryData;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
