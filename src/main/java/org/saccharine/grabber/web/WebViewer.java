package org.saccharine.grabber.web;

import org.apache.log4j.Logger;
import org.saccharine.grabber.db.AppDataBaseService;
import org.saccharine.grabber.db.Post;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class WebViewer {
    public static final Logger LOGGER = Logger.getLogger(WebViewer.class);
    private final AppDataBaseService appDataBaseService;
    private final int port;

    public WebViewer(AppDataBaseService appDataBaseService, int port) {
        this.appDataBaseService = appDataBaseService;
        this.port = port;
    }

    public void web() {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        for (Post post : appDataBaseService.getAll()) {
                            String output = String.format("ID: %s%nTitle: %s%nLink: %s%nDescription: %s%nCreated: %s%n%n",
                                    post.getId(),
                                    post.getTitle(),
                                    post.getLink(),
                                    post.getDescription(),
                                    post.getCreated());
                            out.write(output.getBytes(Charset.forName("Windows-1251")));
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        LOGGER.info("Started web service on port " + port);
    }
}