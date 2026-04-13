package se.magictechnology.pia14androidbilling

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.DeveloperProvidedBillingDetails
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShoppingViewmodel : ViewModel() {

    lateinit var activity : Activity
    lateinit var billingclient : BillingClient


    private var _premiumproduct = MutableStateFlow<ProductDetails?>(null)
    val premiumproduct: StateFlow<ProductDetails?> = _premiumproduct.asStateFlow()

    fun setupBillingClient(billingbuilder : BillingClient.Builder) {

        billingclient = billingbuilder.setListener(purchasesUpdatedListener).enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()).build()

        startBilling()
    }

    fun startBilling() {
        billingclient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    getproducts()
                } else {
                    // FAIL
                }
            }
            override fun onBillingServiceDisconnected() {
            }
        })
    }


    fun getproducts() {
        var queryProducts = mutableListOf<QueryProductDetailsParams.Product>()

        var prod1 = QueryProductDetailsParams.Product.newBuilder()
            .setProductId("pia14premium")
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        queryProducts.add(prod1)

        var queryProductsParam = QueryProductDetailsParams.newBuilder().setProductList(queryProducts).build()

        billingclient.queryProductDetailsAsync(queryProductsParam) { billingResult, productDetailsList ->
            // Process the result.
            if(billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.i("PIA14DEBUG", "GOT PRODUCTS")

                productDetailsList.productDetailsList.forEach { prod ->
                    if(prod.productId == "pia14premium") {
                        _premiumproduct.value = prod
                    }
                }

            } else {
                // FAIL GET PRODUCTS
                Log.i("PIA14DEBUG", "FAIL GET PRODCUTS")
            }
        }

    }


    fun buyProduct(prod : ProductDetails) {

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(prod)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingclient.launchBillingFlow(activity, billingFlowParams)

    }




    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    for(buyproduct in purchase.products) {
                        if(buyproduct == "pia14premium") {
                            Log.i("PIA14DEBUG", "KÖPT PREMIUM")

                            // Ge tillgång till köpt sak


                            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.purchaseToken).build()

                            billingclient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                                // ALLT KLART
                                if(billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                    // OK ACK
                                } else {
                                    // FAIL ACK
                                }
                            }
                        }
                    }
                }
            }
        }

}