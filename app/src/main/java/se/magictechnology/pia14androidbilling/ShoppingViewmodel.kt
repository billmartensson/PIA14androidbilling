package se.magictechnology.pia14androidbilling

import androidx.lifecycle.ViewModel
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.DeveloperProvidedBillingDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams

class ShoppingViewmodel : ViewModel() {

    lateinit var billingclient : BillingClient


    fun setupBillingClient(billingbuilder : BillingClient.Builder) {
        billingclient = billingbuilder.setListener(purchasesUpdatedListener).enablePendingPurchases().build()

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
                productDetailsList.forEach { prod ->
                }
            } else {
                // FAIL GET PRODUCTS
            }
        }

    }


    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->

        }

}