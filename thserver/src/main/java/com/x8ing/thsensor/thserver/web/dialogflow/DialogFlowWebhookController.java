//package com.x8ing.thsensor.thserver.web.dialogflow;
//
//import com.google.api.client.json.JsonGenerator;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowCxV3WebhookRequest;
//import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2IntentMessage;
//import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2IntentMessageText;
//import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookResponse;
//import com.x8ing.thsensor.thserver.web.services.heating.HeatPumpDataService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.io.StringWriter;
//import java.util.List;
//
//@RestController
//public class DialogFlowWebhookController {
//
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    private final JacksonFactory jacksonFactory;
//
//    private final HeatPumpDataService heatPumpDataService;
//
//    // https://www.javacodemonk.com/dialoglfow-fulfillment-with-spring-boot-a933ec21
//
//    // https://botflo.com/dialogflow-python-webhook-tutorial/
//
//    public DialogFlowWebhookController(JacksonFactory jacksonFactory, HeatPumpDataService heatPumpDataService) {
//        this.jacksonFactory = jacksonFactory;
//        this.heatPumpDataService = heatPumpDataService;
//    }
//
//    // http://mindalyze.hopto.org:49088/pi11/dialalogflow/heating
//    // https://mindalyze.com/pi100/dialalogflow/heating
//    @PostMapping(value = "/dialalogflow/heating", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public String webhook(@RequestBody String rawData) throws Exception {
//
//        // API
//        // https://cloud.google.com/dialogflow/cx/docs/reference/rest/v3/Fulfillment
//
//        //Step 1. Parse the request
//
//
//        GoogleCloudDialogflowCxV3WebhookRequest request = jacksonFactory
//                .createJsonParser(rawData)
//                .parse(GoogleCloudDialogflowCxV3WebhookRequest.class);
//
//        log.info(request.toPrettyString());
//
//
//        //Step 2. Process the request
//        //Step 3. Build the response message
//        GoogleCloudDialogflowV2IntentMessage msg = new GoogleCloudDialogflowV2IntentMessage();
//        GoogleCloudDialogflowV2IntentMessageText text = new GoogleCloudDialogflowV2IntentMessageText();
//        text.setText(List.of("Boiler Temperatur ist "+heatPumpDataService.getCurrent().getBoilerTemp()));
//        msg.setText(text);
//
//        GoogleCloudDialogflowV2WebhookResponse response = new GoogleCloudDialogflowV2WebhookResponse();
//        response.setFulfillmentMessages(List.of(msg));
//        StringWriter stringWriter = new StringWriter();
//        JsonGenerator jsonGenerator = jacksonFactory.createJsonGenerator(stringWriter);
//        jsonGenerator.enablePrettyPrint();
//        jsonGenerator.serialize(response);
//        jsonGenerator.flush();
//        return stringWriter.toString();
//
//       // return "{'fulfillmentText2': 'This is a response from webhook.'}";
////        return "{\n" +
////                "  \"fulfillmentText\": \"The sum of the two numbers is:\",\n" +
////                "  \"source\": \"webhookdata\"\n" +
////                "}";
//    }
//}