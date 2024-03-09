package org.docs;

import org.apache.commons.text.StringEscapeUtils;

import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        CrptApi crpt = new CrptApi(10, Duration.ofSeconds(10));
        crpt.createDocument("{\"description\": { \"participantInn\": \"string\" }, \"doc_id\": \"string\", " +
                "\"doc_status\": \"string\", \"doc_type\": \"LP_INTRODUCE_GOODS\", \"importRequest\": true, " +
                "\"owner_inn\": \"string\", \"participant_inn\": \"string\", \"producer_inn\": \"string\", " +
                "\"production_date\": \"2020-01-23\", \"production_type\": \"string\", " +
                "\"products\": [ { \"certificate_document\": \"string\", \"certificate_document_date\": \"2020-01-23\", " +
                "\"certificate_document_number\": \"string\", \"owner_inn\": \"string\", \"producer_inn\": \"string\", " +
                "\"production_date\": \"2020-01-23\", \"tnved_code\": \"string\", \"uit_code\": \"string\", \"uitu_code\": \"string\" } ], " +
                "\"reg_date\": \"2020-01-23\", \"reg_number\": \"string\"}", "MR. DENNIS");
    }
}