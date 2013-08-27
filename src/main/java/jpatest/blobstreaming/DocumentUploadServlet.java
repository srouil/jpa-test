package jpatest.blobstreaming;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;

@SuppressWarnings("serial")
@WebServlet(name = "DocumentUploadServlet", urlPatterns = "/document_upload")
@MultipartConfig
public class DocumentUploadServlet extends HttpServlet {

    @Resource
    UserTransaction ut;

    @PersistenceContext(unitName = "jpaTestPU")
    EntityManager em;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        InputStream filePartIs = null;

        try {
            Part filePart = request.getPart("file");
            String fileName = getFileName(filePart);
            String description = request.getParameter("description");
            filePartIs = filePart.getInputStream();

            ut.begin();
            HibernateEntityManager hem = em.unwrap(HibernateEntityManager.class);
            Session session = hem.getSession();

            LargeDocument document = new LargeDocument();
            document.setBinaryData(Hibernate.getLobCreator(session).createBlob(filePartIs, -1));
            document.setFileName(fileName);
            document.setContentType(filePart.getContentType());
            document.setDescription(description);

            em.persist(document);
            ut.commit();

        } catch (Exception e) {
            try {
                if (ut.getStatus() == Status.STATUS_ACTIVE) {
                    ut.rollback();
                }
            } catch (SystemException se) {
                // some logging
            }
        }
    }

    // Get file name from part header like 
    // Content-Disposition: form-data; name="file"; filename="sample.txt"
    private static String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
