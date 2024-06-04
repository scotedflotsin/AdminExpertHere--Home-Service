package www.experthere.adminexperthere.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import www.experthere.adminexperthere.R;

public class FirebaseFragment extends Fragment {

    Activity activity;
    private WebView webView;

    LinearProgressIndicator progressIndicator;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_firebase, container, false);




        webView = view.findViewById(R.id.webView);
        progressIndicator = view.findViewById(R.id.linearProgressIndicator);

        // Enable JavaScript (optional)
        webView.getSettings().setJavaScriptEnabled(true);

        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        WebSettings webSettings = webView.getSettings();


        webView.setWebChromeClient(new WebChromeClient());
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        // WebView Tweaks
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        String userAgent = System.getProperty("http.agent");
        webSettings.setUserAgentString(userAgent + "AdminExpertHere");


        // Set up a WebViewClient to monitor loading events
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                progressIndicator.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                progressIndicator.setVisibility(View.GONE);


            }


        });

        webView.loadUrl(getString(R.string.firebase_web_url));

        view.findViewById(R.id.refreshBrowser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                webView.loadUrl(getString(R.string.firebase_web_url));

            }
        });

        view.findViewById(R.id.backBrowser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (webView.canGoBack()){

                    webView.goBack();
                }

            }
        });



        return view;

    }




}