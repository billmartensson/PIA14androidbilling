package se.magictechnology.pia14androidbilling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.billingclient.api.BillingClient
import se.magictechnology.pia14androidbilling.ui.theme.PIA14androidbillingTheme

class MainActivity : ComponentActivity() {

    private var billingClient = BillingClient.newBuilder(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var shopvm = ShoppingViewmodel()
        shopvm.activity = this
        shopvm.setupBillingClient(billingClient)

        enableEdgeToEdge()
        setContent {
            PIA14androidbillingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Shopping(shopvm = shopvm)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PIA14androidbillingTheme {
        Greeting("Android")
    }
}