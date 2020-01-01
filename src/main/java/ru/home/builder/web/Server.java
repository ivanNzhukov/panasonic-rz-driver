package ru.home.builder.web;

import ru.home.builder.main.Main;
import ru.home.builder.vlc.listeners.VlcComands;
import spark.Route;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import static spark.Spark.*;

public class Server {

    Route route;
    EmbeddedMediaPlayer player = Main.vlc.getPlayer();
    EmbeddedMediaPlayerComponent component = Main.vlc.getComponent();
    VlcComands comands = new VlcComands();

    public void start() {

        port(8080);
        path("/volume", () -> {
            get("/get", (req, res) -> comands.getVolume());
            get("/set/:volume", (req, res) -> {
                int vol = Integer.valueOf(req.params(":volume"));
                // comands.setVolume(vol);
                player.submit(() -> player.audio().setVolume(vol));
                System.out.println(vol);
                System.out.println("comands" + comands.getVolume());
                return player.audio().volume();//comands.getVolume();
            });
        });

        get("/fullscreen", (req, res) -> {
            comands.setFullScreen();
            return null;
        });

        get("/filename", (req, res) -> comands.getCurrentFilename());

        path("/projector", () -> {

            get("/status", (req, res) -> "");
        });
    }
}
