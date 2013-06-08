package fi.neter.kissani.fb;

import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (c) <year> <copyright holders>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * 
 * <p>This will decipher and validate the OAuth 2.0 cookie you get from Facebook.  You can recognize it because
 * it starts with "fbsr_".  This code does *not* work with the older "fbs_" OAuth 1.0 cookie.</p>
 * 
 * <p>This is the ONLY class in batchfb that relies on commons-codec.  This is just for the Base64 decoder,
 * which intelligently handles "urlsafe" base64 strings with _ and - instead of + and *.  The base64 decoder
 * in JAXB doesn't, so we need the 3rd party lib.</p>
 */
public class FBCookie
{
	private static final Logger logger = LoggerFactory
			.getLogger(FBCookie.class);
	
	/** */
	private static final ObjectMapper MAPPER = new ObjectMapper();
	static {
		MAPPER.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/** */
	@JsonProperty("algorithm")
	String algorithm;

	/** */
	@JsonProperty("user_id")
	long fbId;
	public long getFbId() { return this.fbId; }

	@JsonProperty("oauth_token")
	String accessToken;
	/** Despite being present in Python running samples app, this doesn't seem to actually show up. Expect it to be null. */
	public String getAccessToken() { return this.accessToken; }

	@JsonProperty("code")
	String code;
	/** This shows up in the cookie payload instead of oauth_token, but isn't documented */
	public String getCode() { return this.code; }
	
	private static final String UTF8 = "UTF-8";
	private static final String ASCII = "ASCII";

	/**
	 * Decodes and validates the cookie.
	 * @param cookie is the fbsr_YOURAPPID cookie from Facebook
	 * @param appSecret is your application secret from the Facebook Developer application console
	 * @throws IllegalStateException if the cookie does not validate 
	 */
	public static FBCookie decode(String cookie, String appSecret)
	{
		// Parsing and verifying signature seems to be poorly documented, but here's what I've found:
		// Look at parseSignedRequest() in https://github.com/facebook/php-sdk/blob/master/src/base_facebook.php
		// Python version:  https://developers.facebook.com/docs/samples/canvas/

		try {
			String[] parts = cookie.split("\\.");
			byte[] plaintext = parts[1].getBytes();	// careful, we compute against the base64 encoded version

			String part0 = parts[0].replace("_", "/").replace("-", "+");
			String part1 = parts[1].replace("_", "/").replace("-", "+");
			while (part0.length() % 4 != 0) part0 = part0 + "=";
			while (part1.length() % 4 != 0) part1 = part1 + "=";

			byte[] sig = Base64.decodeBase64(part0.getBytes());
			byte[] json = Base64.decodeBase64(part1.getBytes());
			FBCookie decoded = MAPPER.readValue(json, FBCookie.class);

			// "HMAC-SHA256" doesn't work, but "HMACSHA256" does.
			String algorithm = decoded.algorithm.replace("-", "");

			SecretKey secret = new SecretKeySpec(appSecret.getBytes(), algorithm);
			
			Mac mac = Mac.getInstance(algorithm);
			mac.init(secret);
			byte[] digested = mac.doFinal(plaintext);
			
			if (!Arrays.equals(sig, digested))
				throw new IllegalStateException("Signature failed: " + new String(Hex.encodeHex(sig)) + " != "
					+ new String(Hex.encodeHex(digested)));

			return decoded;
			
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to decode cookie", ex);
		}
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}