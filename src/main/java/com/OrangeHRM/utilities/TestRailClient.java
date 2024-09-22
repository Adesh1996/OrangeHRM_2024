package com.OrangeHRM.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.testng.Assert;

import com.OrangeHRM.constants.AppConstants;

/**
 * TestRail API binding for Java (API v2, available since TestRail 3.0) Updated
 * for TestRail 5.7
 *
 * Code is taken from :
 *
 * http://docs.gurock.com/testrail-api2/start
 * http://docs.gurock.com/testrail-api2/accessing
 */

public class TestRailClient {

	private String baseUrl;
	private String userName;
	private String apiKey;

	public TestRailClient(String uri, String userName, String apiKey) {
		if (!uri.endsWith("/")) {
			uri += "/";
		}

		this.baseUrl = uri + "index.php?/api/v2";
		this.userName = userName;
		this.apiKey = apiKey;
	}

	/**
	 * Send Get
	 *
	 * Issues a GET request (read) against the API and returns the result (as
	 * Object, see below).
	 *
	 * Arguments:
	 *
	 * uri The API method to call including parameters (e.g. get_case/1)
	 *
	 * Returns the parsed JSON response as standard object which can either be an
	 * instance of JSONObject or JSONArray (depending on the API method). In most
	 * cases, this returns a JSONObject instance which is basically the same as
	 * java.util.Map.
	 * 
	 * If 'get_attachment/:attachment_id', returns a String
	 */
	public Object sendGet(String uri, String data) throws Exception {
		return this.sendRequest("GET", uri, data);
	}

	public Object sendGet(String uri) throws Exception {
		return this.sendRequest("GET", uri, null);
	}

	/**
	 * Send POST
	 *
	 * Issues a POST request (write) against the API and returns the result (as
	 * Object, see below).
	 *
	 * Arguments:
	 *
	 * uri The API method to call including parameters (e.g. add_case/1) data The
	 * data to submit as part of the request (e.g.,a map) If adding an attachment,
	 * must be the path to the file
	 *
	 * Returns the parsed JSON response as standard object which can either be an
	 * instance of JSONObject or JSONArray (depending on the API method). In most
	 * cases, this returns a JSONObject instance which is basically the same as
	 * java.util.Map.
	 */
	public Object sendPost(String uri, Object data) throws Exception {
		return this.sendRequest("POST", uri, data);
	}

	private Object sendRequest(String method, String uri, Object data) throws Exception {
		URL url = new URL(this.baseUrl + uri);
		// Create the connection object and set the required HTTP method
		// (GET/POST) and headers (content type and basic auth).

		/*
		 * Use this when you are working on remote desktop Proxy webProxy = new
		 * Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.prod.local" , 8080));
		 * HttpURLConnection conn = (HttpURLConnection)url.openConnection(webProxy);
		 */
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		String auth = getAuthorization(this.userName, this.apiKey);
		conn.addRequestProperty("Authorization", "Basic " + auth);

		if (method.equals("POST")) {
			conn.setRequestMethod("POST");
			// Add the POST arguments, if any. We just serialize the passed
			// data object (i.e. a dictionary) and then add it to the
			// request body.
			if (data != null) {
				if (uri.startsWith("add_attachment")) // add_attachment API requests
				{
					String boundary = "TestRailAPIAttachmentBoundary"; // Can be any random string
					File uploadFile = new File((String) data);

					conn.setDoOutput(true);
					conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

					OutputStream ostreamBody = conn.getOutputStream();
					BufferedWriter bodyWriter = new BufferedWriter(new OutputStreamWriter(ostreamBody));

					bodyWriter.write("\n\n--" + boundary + "\r\n");
					bodyWriter.write("Content-Disposition: form-data; name=\"attachment\"; filename=\""
							+ uploadFile.getName() + "\"");
					bodyWriter.write("\r\n\r\n");
					bodyWriter.flush();

					// Read file into request
					InputStream istreamFile = new FileInputStream(uploadFile);
					int bytesRead;
					byte[] dataBuffer = new byte[1024];
					while ((bytesRead = istreamFile.read(dataBuffer)) != -1) {
						ostreamBody.write(dataBuffer, 0, bytesRead);
					}

					ostreamBody.flush();

					// end of attachment, add boundary
					bodyWriter.write("\r\n--" + boundary + "--\r\n");
					bodyWriter.flush();

					// Close streams
					istreamFile.close();
					ostreamBody.close();
					bodyWriter.close();
				} else // Not an attachment
				{
					conn.addRequestProperty("Content-Type", "application/json");
					byte[] block = JSONValue.toJSONString(data).getBytes("UTF-8");

					conn.setDoOutput(true);
					OutputStream ostream = conn.getOutputStream();
					ostream.write(block);
					ostream.close();
				}
			}
		} else // GET request
		{
			conn.addRequestProperty("Content-Type", "application/json");
		}

		// Execute the actual web request (if it wasn't already initiated
		// by getOutputStream above) and record any occurred errors (we use
		// the error stream in this case).
		int status = conn.getResponseCode();

		InputStream istream;
		if (status != 200) {
			istream = conn.getErrorStream();
			if (istream == null) {
				throw new Exception("TestRail API return HTTP " + status + " (No additional error message received)");
			}
		} else {
			istream = conn.getInputStream();
		}

		// If 'get_attachment' (not 'get_attachments') returned valid status code, save
		// the file
		if ((istream != null) && (uri.startsWith("get_attachment/"))) {
			FileOutputStream outputStream = new FileOutputStream((String) data);

			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = istream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			istream.close();
			return (String) data;
		}

		// Not an attachment received
		// Read the response body, if any, and deserialize it from JSON.
		String text = "";
		if (istream != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(istream, "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				text += line;
				text += System.getProperty("line.separator");
			}

			reader.close();
		}

		Object result;
		if (!text.equals("")) {
			result = JSONValue.parse(text);
		} else {
			result = new JSONObject();
		}

		// Check for any occurred errors and add additional details to
		// the exception message, if any (e.g. the error message returned
		// by TestRail).
		if (status != 200) {
			String error = "No additional error message received";
			if (result != null && result instanceof JSONObject) {
				JSONObject obj = (JSONObject) result;
				if (obj.containsKey("error")) {
					error = '"' + (String) obj.get("error") + '"';
				}
			}

			throw new Exception("TestRail API returned HTTP " + status + "(" + error + ")");
		}

		return result;
	}

	private static String getAuthorization(String user, String password) {
		try {
			return new String(Base64.getEncoder().encode((user + ":" + password).getBytes()));
		} catch (IllegalArgumentException e) {
			// Not thrown
		}

		return "";
	}

	public void updateTestRail(String scenerioName, String execuationStatus) {
		if (scenerioName.contains("|")) {
			String testRailID = scenerioName.substring(0, scenerioName.indexOf('|'));
			Integer status = 0;
			switch (execuationStatus) {
			case "PASSED":
				status = Integer.valueOf(1);
				break;
			case "FAILED":
				status = Integer.valueOf(5);
				break;
			case "SKIPPED":
				status = Integer.valueOf(2);
				break;

			default:
				Assert.fail("Status ' " + execuationStatus + " 'is not supported");

			}
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("status_id", status);
			try {
				sendPost("add_result_for_case/" + AppConstants.TESTRAIL_RUNID + "/" + testRailID, dataMap);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
