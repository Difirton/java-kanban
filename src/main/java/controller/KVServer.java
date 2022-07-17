package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {
	private static int PORT;
	private static String hostname;
	private final String apiToken;
	private final HttpServer server;
	private final Map<String, String> data = new HashMap<>();

	public KVServer() throws IOException {
		readKVServerConfig();
		apiToken = generateApiToken();
		server = HttpServer.create(new InetSocketAddress(hostname, PORT), 0);
		server.createContext("/register", this::register);
		server.createContext("/save", this::save);
		server.createContext("/load", this::load);
	}

	private static void readKVServerConfig() {
		try (FileInputStream propertiesReader = new FileInputStream("config.properties")) {
			Properties properties = new Properties();
			properties.load(propertiesReader);
			hostname = properties.getProperty("KVServer.hostname");
			String port = properties.getProperty("KVServer.port");
			PORT = Integer.parseInt(port);
		} catch (IOException exception) {
			throw new RuntimeException("There is no data on the hostname and port, the hostname is located " +
					"KVServer. Check that there is a config.properties file at the root of the project with " +
					"the keys KVServer.hostname and KVServer.port " + exception.getMessage());
		}
	}

	private void load(HttpExchange h) throws IOException {
		String response = "";
		try {
			System.out.println("\n/load");
			if ("GET".equals(h.getRequestMethod())) {
				String key = h.getRequestURI().getPath().substring("/save/".length());
				if (data.containsKey(key)) {
					response = data.get(key);
				} else {
					System.out.println("Key для извлечения данных пустой. Key указывается в теле запроса");
					h.sendResponseHeaders(400, 0);
					return;
				}
			} else {
				System.out.println("/load ждёт GET-запрос, а получил " + h.getRequestMethod());
				h.sendResponseHeaders(405, 0);
			}
			h.sendResponseHeaders(200, response.length());
			try (OutputStream outputStream = h.getResponseBody()) {
				outputStream.write(response.getBytes());
			}
		} finally {
			h.close();
		}
	}

	private void save(HttpExchange h) throws IOException {
		try {
			System.out.println("\n/save");
			if (!hasAuth(h)) {
				System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
				h.sendResponseHeaders(403, 0);
				return;
			}
			if ("POST".equals(h.getRequestMethod())) {
				String key = h.getRequestURI().getPath().substring("/save/".length());
				if (key.isEmpty()) {
					System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
					h.sendResponseHeaders(400, 0);
					return;
				}
				String value = readText(h);
				if (value.isEmpty()) {
					System.out.println("Value для сохранения пустой. value указывается в теле запроса");
					h.sendResponseHeaders(400, 0);
					return;
				}
				data.put(key, value);
				System.out.println("Значение для ключа " + key + " успешно обновлено!");
				h.sendResponseHeaders(200, 0);
			} else {
				System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
				h.sendResponseHeaders(405, 0);
			}
		} finally {
			h.close();
		}
	}

	private void register(HttpExchange h) throws IOException {
		try {
			System.out.println("\n/register");
			if ("GET".equals(h.getRequestMethod())) {
				sendText(h, apiToken);
			} else {
				System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
				h.sendResponseHeaders(405, 0);
			}
		} finally {
			h.close();
		}
	}

	public KVServer start() {
		System.out.println("Запускаем KVServer на порту " + PORT);
		System.out.println("Открой в браузере http://localhost:" + PORT + "/");
		System.out.println("API_TOKEN: " + apiToken);
		server.start();
		return this;
	}

	public void stop() {
		server.stop(1);
		System.out.println("Cервер на порту " + PORT +" остановлен");
	}

	private String generateApiToken() {
		return "" + System.currentTimeMillis();
	}

	protected boolean hasAuth(HttpExchange h) {
		String rawQuery = h.getRequestURI().getRawQuery();
		return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
	}

	protected String readText(HttpExchange h) throws IOException {
		return new String(h.getRequestBody().readAllBytes(), UTF_8);
	}

	protected void sendText(HttpExchange h, String text) throws IOException {
		byte[] resp = text.getBytes(UTF_8);
		h.getResponseHeaders().add("Content-Type", "application/json");
		h.sendResponseHeaders(200, resp.length);
		h.getResponseBody().write(resp);
	}
}
