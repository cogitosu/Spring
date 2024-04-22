package com.cogitosum;

import com.github.jsonldjava.core.DocumentLoader;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.RDFDataset;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Json {

    public static void main(String[] args) {
        String jsonWithReference = "{ \"$ref\": \"other.json\" }";

        try {
            // Parse the JSON document
            Object input = JsonLdProcessor.fromString(jsonWithReference);

            // Create a custom DocumentLoader to handle JSON references
            DocumentLoader documentLoader = new DocumentLoader() {
                @Override
                public DocumentLoader url(String url) throws IOException {
                    // Implement your own logic to load the referenced document
                    // For simplicity, we'll load it from a URL in this example
                    return new DocumentLoader().url(new URL(url));
                }
            };

            // Create options with a custom document loader
            JsonLdOptions options = new JsonLdOptions();
            options.setDocumentLoader(documentLoader);

            // Process the JSON-LD input
            RDFDataset rdfDataset = JsonLdProcessor.toRDF(input, options);

            // Get the normalized JSON-LD output
            Object normalized = JsonLdProcessor.normalize(rdfDataset, options);

            // Convert the normalized output back to a Map for easier handling
            Map<String, Object> result = JsonLdProcessor.fromRDF(normalized, options);

            // Print the resolved JSON
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

