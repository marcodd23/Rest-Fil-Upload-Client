package it.marcodd.restfileupload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author marco
 */
public class FileUploadClient {

    public static void main(String[] args) {
        File cv = new File("/home/marco/CV_Marco_Di_Dionisio.pdf");
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost("http://cvu.zanox.com/rest/upload/cv");
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addBinaryBody("file", cv, ContentType.APPLICATION_OCTET_STREAM, "CV_Marco_Di_Dionisio.pdf");
        entityBuilder.addTextBody("firstname", "Marco", ContentType.TEXT_PLAIN);
        entityBuilder.addTextBody("lastname", "Di Dionisio", ContentType.TEXT_PLAIN);
        entityBuilder.addTextBody("email", "marco.dd23@gmail.com", ContentType.TEXT_PLAIN);
        entityBuilder.addTextBody("jobtitle", "Senior Java Developer Role", ContentType.TEXT_PLAIN);
        entityBuilder.addTextBody("source", "Salt Recruitment Website", ContentType.TEXT_PLAIN);
        HttpEntity entity = entityBuilder.build();
        postRequest.setEntity(entity);
        HttpResponse response;
        try {
            response = client.execute(postRequest);
            HttpEntity respEntity = response.getEntity();
            System.out.println(getStringFromInputStream(respEntity.getContent()));
        } catch (IOException ex) {
            Logger.getLogger(FileUploadClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUploadClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(FileUploadClient.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }

        return sb.toString();

    }
}
