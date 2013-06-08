package fi.neter.kissani;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import fi.neter.kissani.fb.FBCookie;

public class CookieTest {
	private static final String realCookie = "97f-IykGPXGaQOz4Db0y2pgW_gsk96vRfWfnvTpHKxI.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImNvZGUiOiJhMTMxYzk5MzA4MDY0ZTA3ZDM2Yjg4OTQuMS03MTM2NDU2NjF8eVd2R0V6dmhMYkRTUGdvV0pMVGljTWV5VEJBIiwiaXNzdWVkX2F0IjoxMzQyODk2NjE4LCJ1c2VyX2lkIjoiNzEzNjQ1NjYxIn0";
	@Value("#{keys.secret}")
	private static final String appSecret;
	
	@Test
	public void testCookieDecoding() {
		FBCookie result = FBCookie.decode(realCookie, appSecret);
		assertEquals("a131c99308064e07d36b8894.1-713645661|yWvGEzvhLbDSPgoWJLTicMeyTBA", result.getCode());
	}
}
