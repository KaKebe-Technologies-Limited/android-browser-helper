package com.google.androidbrowserhelper.playbilling.provider;

import com.android.billingclient.api.BillingFlowParams;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.internal.DoNotInstrument;

import androidx.annotation.Nullable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@DoNotInstrument
@Config(manifest = Config.NONE)
public class MethodDataTest {
    @Test
    public void fromJson_basic() {
        String json = "{ 'sku' = 'mySku' }".replace('\'', '\"');
        MethodData data = MethodData.fromJson(json);

        assertMethodData(data, "mySku", null, null, null);
    }

    @Test
    public void fromJson_malformed() {
        assertNull(MethodData.fromJson("abc"));
    }

    @Test
    public void fromJson_noSku() {
        assertNull(MethodData.fromJson("{}"));
    }

    @Test
    public void fromJson_oldSku() {
        String json = "{ 'sku' = 'mySku', 'oldSku' = 'myOldSku' }"
                .replace('\'', '\"');
        MethodData data = MethodData.fromJson(json);

        assertMethodData(data, "mySku", "myOldSku", null, null);
    }

    @Test
    public void fromJson_purchaseToken() {
        String json = "{ 'sku' = 'mySku', 'purchaseToken' = 'abc123' }"
                .replace('\'', '\"');
        MethodData data = MethodData.fromJson(json);

        assertMethodData(data, "mySku", null, "abc123", null);
    }

    @Test
    public void fromJson_prorationMode() {
        prorationMode("deferred", BillingFlowParams.ProrationMode.DEFERRED);
        prorationMode("immediateAndChargeProratedPrice", BillingFlowParams.ProrationMode.IMMEDIATE_AND_CHARGE_PRORATED_PRICE);
        prorationMode("immediateWithoutProration", BillingFlowParams.ProrationMode.IMMEDIATE_WITHOUT_PRORATION);
        prorationMode("immediateWithTimeProration", BillingFlowParams.ProrationMode.IMMEDIATE_WITH_TIME_PRORATION);
        prorationMode("unknownSubscriptionUpgradeDowngradePolicy", BillingFlowParams.ProrationMode.UNKNOWN_SUBSCRIPTION_UPGRADE_DOWNGRADE_POLICY);
        prorationMode("invalid", null);
    }

    private static void assertMethodData(MethodData data, String sku, @Nullable String oldSku,
            @Nullable String purchaseToken, @Nullable Integer prorationMode) {
        assertEquals(sku, data.sku);
        assertEquals(oldSku, data.oldSku);
        assertEquals(purchaseToken, data.purchaseToken);
        assertEquals(prorationMode, data.prorationMode);
    }

    private static void prorationMode(String mode, Integer expected) {
        String json = ("{ 'sku' = 'mySku', 'prorationMode' = '" + mode + "' }")
                .replace('\'', '\"');
        MethodData data = MethodData.fromJson(json);

        assertMethodData(data, "mySku", null, null, expected);
    }
}
