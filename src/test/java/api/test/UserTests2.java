package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints;
import api.endpoints.UserEndpoints2;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests2 {

	Faker faker;
	User userPayload;
	public Logger logger;
	
	@BeforeClass
	public void setupdata() {
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//logs
		logger = LogManager.getLogger(this.getClass());
		logger.debug("debugging......");
	}
	
	@Test(priority=1)
	public void testPostUserByName() {
		logger.info("********creating user **********");
		Response response = UserEndpoints2.createUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("********created user **********");
	}
	
	@Test(priority=2)
	public void testGetUserByName() {
		
		logger.info("********reading user **********");
		Response response = UserEndpoints2.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("********got user info **********");
	}
	
	@Test(priority=3)
	public void testUpdateUserByName() {
		
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		
		logger.info("********updating user **********");
		Response response = UserEndpoints2.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//after updating user data
		Response updatedResponse = UserEndpoints.readUser(this.userPayload.getUsername());
		updatedResponse.then().log().all();
		Assert.assertEquals(updatedResponse.getStatusCode(), 200);
		logger.info("********updated user **********");
	}
	
	@Test(priority=4)
	public void testDeleteUserByName() {
		
		logger.info("********deleting user **********");
		Response response = UserEndpoints2.deleteUser(this.userPayload.getUsername());
		response.then().log().all();
		response.then().log().body().statusCode(200);
		logger.info("********deleted user **********");
	}
}
