package se.magictechnology.pia14androidbilling

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Shopping(shopvm : ShoppingViewmodel = viewModel()) {

    val premiumprod = shopvm.premiumproduct.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text("SHOPPING")

        if(premiumprod.value != null) {
            Text("PRODUCT")
            Text(premiumprod.value!!.name)
            Text(premiumprod.value!!.title)
            Text(premiumprod.value!!.description)
            Text(premiumprod.value!!.oneTimePurchaseOfferDetails!!.formattedPrice)

            Button(onClick = {
                shopvm.buyProduct(premiumprod.value!!)
            }) {
                Text("BUY")
            }
        }
    }
}

@Composable
fun ShoppingPreview() {
    Shopping()
}