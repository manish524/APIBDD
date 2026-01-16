package base;

import config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import utils.TokenManager;

public class BaseRequest {

	protected RequestSpecification request;

	public BaseRequest() {
//		request = RestAssured.given().baseUri(ConfigReader.get("base.url")).contentType(ContentType.JSON)
//				.accept(ContentType.JSON);
		request = RestAssured.given().baseUri("https://gorest.co.in/public/v2/").contentType(ContentType.JSON)
				.accept(ContentType.JSON);
				//.header("Authorization", "Bearer " + TokenManager.getToken());
	}
}
