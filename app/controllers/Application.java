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
package controllers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryHook;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import play.Play;
import play.libs.Mail;
import play.mvc.Before;
import play.mvc.Controller;
import client.PubSubHubbubClient;

public class Application extends Controller {

	@Before
	public static void setup() {
		renderArgs.put("base", request.getBase());
	}

	public static void index() {
		RepositoryService repositoryService = new RepositoryService(getClient());
		try {
			List<Repository> repositories = repositoryService.getRepositories();
			render(repositories);
		} catch (Exception e) {
			flash.error("Error while getting repositories from github, check credentials from configuration files");
			render();
		}
	}

	public static void repo(String name) {
		RepositoryService repositoryService = new RepositoryService(getClient());

		try {
			Repository repository = repositoryService.getRepository(
					Play.configuration.getProperty("github.user").toString(),
					name);
			List<RepositoryHook> hooks = repositoryService.getHooks(repository);
			render(repository, hooks);
		} catch (IOException e) {
			error(e);
		}
	}

	public static void switchHook(String name, long id) {
		// TODO
		repo(name);
	}

	/**
	 * Subscribes to fork events
	 * 
	 * @param name
	 */
	public static void subscribeToForks(String name) {
		String org = null;
		String target = org != null ? org : Play.configuration.getProperty(
				"github.user").toString();

		getPubSubClient().subscribe(target, name, "fork",
				request.getBase() + "/subscriber/fork");
		flash.success("Subscribed to %s fork events", name);
		index();
	}

	public static void subscribeToWatchs(String name) {
		String org = null;
		String target = org != null ? org : Play.configuration.getProperty(
				"github.user").toString();

		getPubSubClient().subscribe(target, name, "watch",
				request.getBase() + "/subscriber/watch");
		flash.success("Subscribed to %s watch events", name);
		index();
	}

	private static final GitHubClient getClient() {
		GitHubClient client = new GitHubClient();
		client.setCredentials(Play.configuration.get("github.user").toString(),
				Play.configuration.get("github.password").toString());
		return client;
	}

	private static PubSubHubbubClient getPubSubClient() {
		return new PubSubHubbubClient(Play.configuration.getProperty(
				"github.user").toString(), Play.configuration.getProperty(
				"github.password").toString());
	}

	/**
	 * Just a test method to chek if mail works on the target platform...
	 */
	public static void mail() {
		try {
			SimpleEmail email = new SimpleEmail();
			email.setFrom(Play.configuration.getProperty("notification.from"));
			email.addTo(Play.configuration.getProperty("notification.to"));
			email.setSubject("Ping");
			email.setMsg("This is a mail sent from " + request.getBase());
			Mail.send(email);
			flash.success("Mail has been sent to %s",
					Play.configuration.getProperty("notification.to"));
		} catch (Exception e) {
			flash.error("Can not send email, cause : " + e.getMessage());
		}
		index();
	}

}