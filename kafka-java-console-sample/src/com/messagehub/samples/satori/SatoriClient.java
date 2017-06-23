package com.messagehub.samples.satori;
import com.messagehub.samples.MessageHubConsoleSample;
import com.satori.rtm.*;
import com.satori.rtm.model.*;
import java.util.concurrent.*;

public class SatoriClient { 

	public final static RtmClient createClient(String endpoint, String appkey) throws Exception {
		final RtmClient client = new RtmClientBuilder(endpoint, appkey)  
		        .setListener(new RtmClientAdapter() {
		          @Override
		          public void onEnterConnected(RtmClient client) {
		            System.out.println("Connected to RTM!");
		          }
		        })
		        .build();
		return client;
	}
	
	public static void getMessages(RtmClient client, String channel) throws Exception {	  	 		
	    final CountDownLatch success = new CountDownLatch(1);
	        
	    
	    
	    SubscriptionListener listener = new SubscriptionAdapter() {
	      @Override
	      public void onSubscriptionData(SubscriptionData data) {
	        for (AnyJson json : data.getMessages()) {
	          System.out.println("Got message: " + json);
	          MessageHubConsoleSample.messages = data.getMessages();
	        }
	        success.countDown();
	      }
	    };
	
	    client.createSubscription(channel, SubscriptionMode.SIMPLE, listener);
	    
	    client.start();	
	    success.await(1000, TimeUnit.SECONDS);
	    
	    if(client.isConnected())client.removeSubscription(channel);	    
	    
	    //client.shutdown();
	    client.stop();
  }
}