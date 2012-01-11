package edu.cmu.travelbuddy.currency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Currency;
import java.util.Locale;

import android.util.Log;

/**
 * This class gets the currency conversion from the web, everytime it is asked
 * to provide for conversion.
 * 
 * @author Ashwin Das
 * 
 */
public class CurrencyProvider {

	private static final String CURRENCY_URL = "http://currencies.apps.grandtrunk.net/getlatest/";

	public String getConvertedCurrenyValue(Locale locale) {
		try {
			Currency currency = Currency.getInstance(locale);
			String localCurrency = currency.getCurrencyCode();

			String homeCurrency = "INR";

			URL url = new URL(CURRENCY_URL + localCurrency + "/" + homeCurrency);

			URLConnection spoof;
			spoof = url.openConnection();

			// Spoof the connection so we look like a web browser
			spoof.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
			BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String strLine = null;

			// Loop through every line in the source
			while ((strLine = in.readLine()) != null) {

				// Appends each line from the website
				builder.append(strLine);
			}
			return builder.toString();
		} catch (IOException e) {
			Log.w("T_CurrencyProvider", e);
		}
		return "";

	}

}
