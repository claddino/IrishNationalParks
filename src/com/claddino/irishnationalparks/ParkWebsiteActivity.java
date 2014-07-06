package com.claddino.irishnationalparks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ParkWebsiteActivity extends Activity {
 
	private WebView webView;
 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
 
		webView = (WebView) findViewById(R.id.webView1);
		Intent intent= getIntent();
		String value = intent.getStringExtra("ParkWebsite");
		webView.loadUrl(value);
		
		webView.setWebViewClient(new WebViewClient() {
		     public boolean shouldOverrideUrlLoading(WebView view, String url) {
		    	 webView.loadUrl(url);
		  return false;
		     }

		     @Override
		     public void onPageFinished(WebView view, String url) {
		    	
		    	
		     }
		 });
 
	}
 
}