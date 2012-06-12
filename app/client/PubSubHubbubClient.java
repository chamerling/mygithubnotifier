/*
 * Copyright 2012 Christophe Hamerling
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import java.net.HttpURLConnection;

import org.eclipse.egit.github.core.client.GitHubClient;

import play.Logger;
import play.libs.WS;
import play.libs.WS.WSRequest;

/**
 * @author chamerling
 * 
 */
public class PubSubHubbubClient {
	
	private String username;
	private String password;

	public PubSubHubbubClient(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public void subscribe(String user, String repository, String type, String callback) {
		
		WSRequest request = WS
				.url("https://api.github.com/hub");
		request.authenticate(username, password);
		
		request.setParameter("hub.mode", "subscribe");
		request.setParameter("hub.topic", String.format("https://github.com/%s/%s/events/%s", user, repository, type));
		request.setParameter("hub.callback", callback);

		play.libs.WS.HttpResponse response = request.post();
		
		Logger.info("Response status : " + response.getStatus());
		Logger.info("Response body : " + response.getString());
		
		if (response.getStatus() == 204) {

		} else {
			// TODO
		}
	}
	
	public void unsubscribe(String user, String repository, String type) {
		// TODO
	}
}
