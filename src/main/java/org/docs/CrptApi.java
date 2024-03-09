package org.docs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.text.StringEscapeUtils;

import javax.print.Doc;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class CrptApi {
    private final int requestLimit;
    private final Duration requestInterval;
    private final Semaphore semaphore;
    private final Gson gson;

    public CrptApi(int requestLimit, Duration requestInterval) {
        this.requestLimit = requestLimit;
        this.requestInterval = requestInterval;
        this.semaphore = new Semaphore(requestLimit);
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    public void createDocument(Object docToConvert, String signature) {
        String json = StringEscapeUtils.unescapeJava((String) docToConvert);
        Doc doc = convertJsonToDoc(json);
        doc.setSignature(signature);

        try {
            semaphore.acquire(); // Acquire a permit from the semaphore

            // Convert the document and signature to JSON
            String requestBody = convertDocToJson(doc);

            // Create the HTTP client
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(requestBody))
                    .build();

            // Send the HTTP request and get the response
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            // Process the response
            int statusCode = response.statusCode();
            HttpHeaders headers = response.headers();
            String responseBody = response.body();

            // Handle the response according to the status code
            if (statusCode == 200) {
                System.out.println("Document created successfully");
                System.out.println("Response Body: " + responseBody);
            } else {
                System.out.println("Failed to create document");
                System.out.println("Status Code: " + statusCode);
                System.out.println("Response Body: " + responseBody);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted while acquiring semaphore permit");
        } catch (IOException e) {
            System.out.println("Timeout occurred while sending HTTP request");
        } finally {
            semaphore.release(); // Release the permit back to the semaphore
        }
    }

    private String convertDocToJson(Doc doc) {
        String json = gson.toJson(doc);
        return json;
    }
    private Doc convertJsonToDoc(String json) {
        return gson.fromJson(json, Doc.class);
    }
    private static class Doc implements Serializable {
        Description DescriptionObject;
        private String doc_id;
        private String doc_status;
        private String doc_type;
        private boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private String production_date;
        private String production_type;
        ArrayList< Object > products = new ArrayList < Object > ();
        private String reg_date;
        private String reg_number;
        private String signature;

        public Description getDescription() {
            return DescriptionObject;
        }

        public String getDoc_id() {
            return doc_id;
        }

        public String getDoc_status() {
            return doc_status;
        }

        public String getDoc_type() {
            return doc_type;
        }

        public boolean getImportRequest() {
            return importRequest;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public String getParticipant_inn() {
            return participant_inn;
        }

        public String getProducer_inn() {
            return producer_inn;
        }

        public String getProduction_date() {
            return production_date;
        }

        public String getProduction_type() {
            return production_type;
        }

        public String getReg_date() {
            return reg_date;
        }

        public String getReg_number() {
            return reg_number;
        }

        public String getSignature() {
            return signature;
        }

        public void setDescription(Description descriptionObject) {
            this.DescriptionObject = descriptionObject;
        }

        public void setDoc_id(String doc_id) {
            this.doc_id = doc_id;
        }

        public void setDoc_status(String doc_status) {
            this.doc_status = doc_status;
        }

        public void setDoc_type(String doc_type) {
            this.doc_type = doc_type;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public void setOwner_inn(String owner_inn) {
            this.owner_inn = owner_inn;
        }

        public void setParticipant_inn(String participant_inn) {
            this.participant_inn = participant_inn;
        }

        public void setProducer_inn(String producer_inn) {
            this.producer_inn = producer_inn;
        }

        public void setProduction_date(String production_date) {
            this.production_date = production_date;
        }

        public void setProduction_type(String production_type) {
            this.production_type = production_type;
        }

        public void setReg_date(String reg_date) {
            this.reg_date = reg_date;
        }

        public void setReg_number(String reg_number) {
            this.reg_number = reg_number;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        @Override
        public String toString() {
            return "Doc{" +
                    "DescriptionObject=" + DescriptionObject +
                    ", doc_id='" + doc_id + '\'' +
                    ", doc_status='" + doc_status + '\'' +
                    ", doc_type='" + doc_type + '\'' +
                    ", importRequest=" + importRequest +
                    ", owner_inn='" + owner_inn + '\'' +
                    ", participant_inn='" + participant_inn + '\'' +
                    ", producer_inn='" + producer_inn + '\'' +
                    ", production_date='" + production_date + '\'' +
                    ", production_type='" + production_type + '\'' +
                    ", products=" + products +
                    ", reg_date='" + reg_date + '\'' +
                    ", reg_number='" + reg_number + '\'' +
                    ", signature='" + signature + '\'' +
                    '}';
        }
    }
    private static class Description implements Serializable{
        private String participantInn;

        public String getParticipantInn() {
            return participantInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }
    }
}
