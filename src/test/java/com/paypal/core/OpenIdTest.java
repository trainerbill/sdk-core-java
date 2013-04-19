package com.paypal.core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.sdk.openidconnect.PPOpenIdSession;
import com.paypal.sdk.openidconnect.CreateFromAuthorizationCodeParameters;
import com.paypal.sdk.openidconnect.CreateFromRefreshTokenParameters;
import com.paypal.sdk.openidconnect.PayPalRESTException;
import com.paypal.sdk.openidconnect.Tokeninfo;
import com.paypal.sdk.openidconnect.Userinfo;
import com.paypal.sdk.openidconnect.UserinfoParameters;

public class OpenIdTest {

	private static final Logger logger = Logger.getLogger(OpenIdTest.class);

	private Tokeninfo info;

	Map<String, String> configurationMap = new HashMap<String, String>();

	public OpenIdTest() {
//		configurationMap.put("clientId", "");
//		configurationMap.put("clientSecret", "");
		configurationMap.put("mode", "live");
		configurationMap.put("http.ConnectionTimeOut", "5000");
		configurationMap.put("http.Retry", "2");
		configurationMap.put("http.ReadTimeOut", "30000");
		configurationMap.put("http.MaxConnection", "100");
		configurationMap.put("http.IPAddress", "127.0.0.1");
	}

	@Test(enabled = false)
	public void testCreateFromAuthorizationCodeDynamic()
			throws InvalidResponseDataException, HttpErrorException,
			ClientActionRequiredException, PayPalRESTException,
			URISyntaxException, IOException, InterruptedException {
		CreateFromAuthorizationCodeParameters param = new CreateFromAuthorizationCodeParameters();
		param.setCode("vmqgP0cyIeoL2t1rZu9m_0OpT59zSRpvEugsxdOOOlS5ewbYQ0EPWYwshIQ78MnOFKhytav0QkwM_yEpf2mZv5r5r_yhjlo-j7ct8jxj34LT4o0k");
		info = Tokeninfo.createFromAuthorizationCode(configurationMap, param);
		logger.info("Generated Access Token : " + info.getAccessToken());
		logger.info("Generated Refrest Token: " + info.getRefreshToken());
		String enc = URLEncoder.encode(info.getRefreshToken(), "UTF-8");
		info.setRefreshToken(enc);
	}

	@Test(dependsOnMethods = { "testCreateFromAuthorizationCodeDynamic" }, enabled = false)
	public void testCreateFromRefreshTokenDynamic()
			throws InvalidResponseDataException, HttpErrorException,
			ClientActionRequiredException, PayPalRESTException,
			URISyntaxException, IOException, InterruptedException {
		CreateFromRefreshTokenParameters param = new CreateFromRefreshTokenParameters();
		info = info.createFromRefreshToken(configurationMap, param);
		logger.info("Regenerated Access Token: " + info.getAccessToken());
		logger.info("Refresh Token: " + info.getRefreshToken());
	}

	@Test(dependsOnMethods = { "testCreateFromRefreshTokenDynamic" }, enabled = false)
	public void testUserinfoDynamic() throws InvalidResponseDataException,
			HttpErrorException, ClientActionRequiredException,
			PayPalRESTException, URISyntaxException, IOException,
			InterruptedException {
		UserinfoParameters param = new UserinfoParameters();
		param.setAccessToken(info.getAccessToken());
		Userinfo userInfo = Userinfo.userinfo(configurationMap, param);
		logger.info("User Info Email: " + userInfo.getEmail());
		logger.info("User Info Account Type: " + userInfo.getAccountType());
		logger.info("User Info Name: " + userInfo.getGivenName());
	}

	@Test()
	public void testAuthorizationURL() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("openid.RedirectUri",
				"https://www.paypal.com/webapps/auth/protocol/openidconnect");
		m.put("clientId", "ANdfsalkoiarT");
		List<String> l = new ArrayList<String>();
		l.add("openid");
		l.add("profile");
		String redirectURL = PPOpenIdSession.getRedirectURL("http://google.com",
				l, m);
		logger.info("Redirect URL: " + redirectURL);
		Assert.assertEquals(
				redirectURL,
				"https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize?client_id=ANdfsalkoiarT&response_type=code&scope=openid+profile+&redirect_uri=http%3A%2F%2Fgoogle.com");
	}
	
	@Test()
	public void testLogoutURL() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("openid.RedirectUri",
				"https://www.paypal.com/webapps/auth/protocol/openidconnect");
		String logoutURL = PPOpenIdSession.getLogoutUrl("http://google.com",
				"tokenId", m);
		logger.info("Redirect URL: " + logoutURL);
		Assert.assertEquals(
				logoutURL,
				"https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/endsession?id_token=tokenId&redirect_uri=http%3A%2F%2Fgoogle.com&logout=true");
	}
}
