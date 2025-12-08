package dr.shadcn.kmp.pages.webview

//import android.annotation.SuppressLint
//import android.view.ViewGroup
//import android.webkit.WebChromeClient
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.viewinterop.AndroidView
//import com.drna.shadcn.ui.WebViewSlug
//
//@SuppressLint("SetJavaScriptEnabled")
//@Composable
//fun WebviewPage(slug: WebViewSlug) {
//    val url = when (slug) {
//        WebViewSlug.Github -> "https://github.com/derangga/shadcn-compose"
//        else -> "https://shadcn-compose.site/"
//    }
//
//    Scaffold { ip ->
//        Box(
//            modifier = Modifier.padding(ip)
//        ) {
//            AndroidView(
//                factory = { context ->
//                    WebView(context).apply {
//                        layoutParams = ViewGroup.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT
//                        )
//                        settings.also {
//                            it.javaScriptEnabled = true
//                            it.useWideViewPort = true
//                            it.domStorageEnabled = true
//                        }
//                        webViewClient = WebViewClient()
//                        webChromeClient = WebChromeClient()
//                    }
//                },
//                update = { webView ->
//                    webView.loadUrl(url)
//                }
//            )
//        }
//    }
//}