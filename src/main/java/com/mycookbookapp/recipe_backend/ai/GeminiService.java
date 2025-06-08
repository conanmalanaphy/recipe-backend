package com.mycookbookapp.recipe_backend.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycookbookapp.recipe_backend.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired; // Import Autowired
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity; // Import HttpEntity
import org.springframework.http.HttpHeaders; // Import HttpHeaders
import org.springframework.http.MediaType; // Import MediaType
import org.springframework.http.ResponseEntity; // Import ResponseEntity
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; // Import RestTemplate
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {


    // CHANGE THIS LINE: Update the model ID from gemini-pro-vision to gemini-1.5-flash
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent"; // <--- UPDATED MODEL

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired // Inject the RestTemplate bean
    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Recipe parseRecipeFromImage(MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty.");
        }

        // 1. Convert image to Base64
        String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
        String mimeType = imageFile.getContentType();
        if (mimeType == null || mimeType.isEmpty()) {
            mimeType = "image/jpeg"; // Fallback if type not provided
        }

        // 2. Craft the prompt for Gemini
        // ... inside parseRecipeFromImage method ...
        String promptText = "Extract the following details from this image as a JSON object, focusing on recipe information.\n" +
                "**For 'ingredients', provide a single comma-separated string, converting any newlines or list items into commas.**\n" +
                "**For 'instructions', provide a single string with steps separated by periods, converting any newlines or list items into periods.**\n" +
                "If a field is not found, use an empty string. Ensure the output is valid JSON and directly parsable.\n" +
                "{\n" +
                "  \"title\": \"\",\n" +
                "  \"description\": \"\",\n" +
                "  \"ingredients\": \"\",\n" +
                "  \"instructions\": \"\",\n" +
                "  \"prepTime\": \"\",\n" +
                "  \"cookTime\": \"\",\n" +
                "  \"servings\": \"\",\n" +
                "  \"category\": \"\",\n" +
                "  \"cuisine\": \"\",\n" +
                "  \"imageUrl\": \"\"\n" +
                "}";
// ... rest of the method ...
        // 3. Construct the request body for Gemini Pro Vision API
        // This maps to the JSON structure:
        // { "contents": [ { "parts": [ { "text": "prompt" }, { "inlineData": { "mimeType": "...", "data": "..." } } ] } ] }
        Map<String, Object> inlineData = new HashMap<>();
        inlineData.put("mimeType", mimeType);
        inlineData.put("data", base64Image);

        Map<String, Object> imagePart = new HashMap<>();
        imagePart.put("inlineData", inlineData);

        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", promptText);

        Map<String, Object> contentParts = new HashMap<>();
        contentParts.put("parts", List.of(textPart, imagePart)); // Text part first, then image part

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(contentParts));

        // 4. Set up HTTP Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 5. Create the HttpEntity (request body + headers)
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // 6. Make the API call
        String fullUrl = GEMINI_API_URL + "?key=" + "AIzaSyC4hUx_loakQQp7eSRjEv5L4phCkDN9Cco";
        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(fullUrl, requestEntity, JsonNode.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            // 7. Parse Gemini's Response
            JsonNode rootNode = responseEntity.getBody();
            JsonNode candidatesNode = rootNode.path("candidates");

            if (candidatesNode.isArray() && !candidatesNode.isEmpty()) {
                JsonNode contentNode = candidatesNode.get(0).path("content");
                JsonNode partsNode = contentNode.path("parts");

                if (partsNode.isArray() && !partsNode.isEmpty()) {
                    JsonNode textNode = partsNode.get(0).path("text"); // Get the text part from Gemini's response
                    if (textNode.isTextual()) {
                        String geminiResponseText = textNode.asText();

                        // Gemini might wrap JSON in markdown, e.g., ```json{...}```
                        // Extract only the JSON part
                        int jsonStartIndex = geminiResponseText.indexOf('{');
                        int jsonEndIndex = geminiResponseText.lastIndexOf('}');
                        if (jsonStartIndex != -1 && jsonEndIndex != -1 && jsonEndIndex > jsonStartIndex) {
                            geminiResponseText = geminiResponseText.substring(jsonStartIndex, jsonEndIndex + 1);
                        }

                        // Parse the extracted JSON into a Recipe object
                        try {
                            Recipe parsedRecipe = objectMapper.readValue(geminiResponseText, Recipe.class);

                            // --- NEW: Post-process ingredients and instructions for better formatting ---
                            if (parsedRecipe.getIngredients() != null) {
                                parsedRecipe.setIngredients(
                                        parsedRecipe.getIngredients()
                                                .replaceAll("\\s*\\n\\s*", ", ") // Replace newlines with comma-space
                                                .replaceAll("\\s*,\\s*", ", ")   // Normalize multiple commas/spaces
                                                .replaceAll("^\\s*,\\s*", "")   // Remove leading comma
                                                .trim()
                                );
                            }

                            if (parsedRecipe.getInstructions() != null) {
                                parsedRecipe.setInstructions(
                                        parsedRecipe.getInstructions()
                                                .replaceAll("\\s*\\n\\s*", ". ") // Replace newlines with period-space
                                                .replaceAll("\\s*\\.\\s*", ". ")   // Normalize multiple periods/spaces
                                                .replaceAll("^\\s*\\.\\s*", "")   // Remove leading period
                                                .trim()
                                );
                            }
// --- END NEW ---

                            return parsedRecipe;
                        } catch (Exception e) {
                            throw new IOException("Failed to parse Gemini's JSON response into Recipe: " + geminiResponseText, e);
                        }
                    }
                }
            }
            throw new IOException("Gemini response format unexpected or empty content.");
        } else {
            throw new IOException("Gemini API call failed with status: " + responseEntity.getStatusCode() + " body: " + responseEntity.getBody());
        }
    }
}