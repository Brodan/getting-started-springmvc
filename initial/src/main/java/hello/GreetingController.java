package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Call;
 
import java.util.ArrayList;
import java.util.List;

@Controller
public class GreetingController {

	// Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String AUTH_TOKEN = "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY";
    public static final String TWILIO_NUMBER = "+15555555555";
    
    @RequestMapping("/greeting")
    public String greeting(
        @RequestParam(value="mode", required=false, defaultValue="text") String mode,
        @RequestParam(value="number", required=true) String number,
        Model model) {
        model.addAttribute("number", number);
        model.addAttribute("mode", mode);
        
        if(mode.equalsIgnoreCase("text")){
            sendSMS(number);
        }
        else if (mode.equalsIgnoreCase("call")) {
            makeCall(number);
        }
        return "greeting";
    }

    public void sendSMS(String to){
        try {
            TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
        
            // Build a filter for the MessageList
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Body", "Jenny please?! I love you <3"));
            params.add(new BasicNameValuePair("To", to));
            params.add(new BasicNameValuePair("From", TWILIO_NUMBER));
        
            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params);
        } 
        catch (TwilioRestException e) {
            System.out.println(e.getErrorMessage());
        }
    }

    public void makeCall(String to) {
        try {
            TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Url", "http://demo.twilio.com/welcome"));
            params.add(new BasicNameValuePair("To", to));
            params.add(new BasicNameValuePair("From", TWILIO_NUMBER));
             
            CallFactory callFactory = client.getAccount().getCallFactory();
            Call call = callFactory.create(params);
        } 
        catch (TwilioRestException e) {
                System.out.println(e.getErrorMessage());
        }
    }
}