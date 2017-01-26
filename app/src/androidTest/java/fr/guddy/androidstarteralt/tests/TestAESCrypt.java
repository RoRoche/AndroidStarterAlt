package fr.guddy.androidstarteralt.tests;

import com.scottyab.aescrypt.AESCrypt;

import junit.framework.Assert;

import org.frutilla.FrutillaTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.GeneralSecurityException;

@RunWith(FrutillaTestRunner.class)
public class TestAESCrypt {

    //region Test methods
    @Test
    public void testMethod() throws GeneralSecurityException {
        // given
        final String password = "android_starter_alt";
        final String message = "https://api.github.com";

        // when
        final String encryptedMsg = AESCrypt.encrypt(password, message);

        // then
        Assert.assertEquals("7CmaOSXXCA12deowjBxa71++4535ooMW2EvjCiUdoJo=", encryptedMsg);
    }
    //endregion
}
