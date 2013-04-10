package jpatest.blob;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DocumentRepositoryBean  {

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    public void createDocument(Document document) {
        em.persist(document);
    }
 
    public List<Document> findAllDocuments() {
        
        // TypedQuery cannot be used together with constructor expression in the SELECT clause
        Query q = em.createNamedQuery(Document.SELECT_ALL_DOCUMENTS);
        return q.getResultList();
    }
    
    public Document findDocumentById(long id) {
        return em.find(Document.class, id);
    }
    
    public Document updateDocument(Document document) {
        return em.merge(document);
    }
}
