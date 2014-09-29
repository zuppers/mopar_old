package net.scapeemulator.game.net.register;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Calendar;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.net.Session;

public final class RegisterSession extends Session {

    public RegisterSession(GameServer server, Channel channel) {
        super(server, channel);
    }

    @Override
    public void messageReceived(Object message) throws IOException {
        if (message instanceof RegisterPersonalDetailsRequest) {
            RegisterPersonalDetailsRequest req = (RegisterPersonalDetailsRequest) message;
            int age = Calendar.getInstance().get(Calendar.YEAR) - req.getDateOfBirth().get(Calendar.YEAR);
            if (age < 0) {
                channel.write(new RegisterResponse(RegisterResponse.STATUS_DOB_FUTURE)).addListener(ChannelFutureListener.CLOSE);
            } else if (age == 0) {
                channel.write(new RegisterResponse(RegisterResponse.STATUS_DOB_THIS_YEAR)).addListener(ChannelFutureListener.CLOSE);
            } else if (age == 1) {
                channel.write(new RegisterResponse(RegisterResponse.STATUS_DOB_LAST_YEAR)).addListener(ChannelFutureListener.CLOSE);
            } else if (age < 12 || age > 100) {
                channel.write(new RegisterResponse(RegisterResponse.STATUS_DOB_INVALID)).addListener(ChannelFutureListener.CLOSE);
            } else {
                channel.write(new RegisterResponse(RegisterResponse.STATUS_OK));
            }
        } else if (message instanceof RegisterUsernameRequest) {
            RegisterUsernameRequest req = (RegisterUsernameRequest) message;
            // TODO maybe remove trailing/leading spaces?
            String username = req.getUsername();
            if (username.matches("^[a-z0-9_]{3,12}$")) {
                if (server.getSerializer().usernameAvailable(username)) {
                    channel.write(new RegisterResponse(RegisterResponse.STATUS_OK)).addListener(ChannelFutureListener.CLOSE);
                } else {
                    channel.write(new RegisterResponse(RegisterResponse.STATUS_USERNAME_UNAVAILABLE)).addListener(ChannelFutureListener.CLOSE);
                }
            } else {
                channel.write(new RegisterResponse(RegisterResponse.STATUS_USERNAME_INVALID)).addListener(ChannelFutureListener.CLOSE);
            }
        } else if (message instanceof RegisterCommitRequest) {
            RegisterCommitRequest req = (RegisterCommitRequest) message;

            /*
             * We check the username again in case its manipulated. Since it shouldn't happen with
             * the actual client, we aren't scared to just dispose without sending a response.
             */
            String username = req.getUsername();
            if (!username.matches("^[a-z0-9_]{3,12}$")) {
                return;
            }
            if (!server.getSerializer().usernameAvailable(username)) {
                return;
            }

            String password = req.getPassword();
            if (password.matches("^[a-zA-Z0-9_]{5,20}$")) {
                if (server.getSerializer().register(((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress(), username, password)) {
                    channel.write(new RegisterResponse(RegisterResponse.STATUS_OK)).addListener(ChannelFutureListener.CLOSE);
                } else {
                    channel.write(new RegisterResponse(RegisterResponse.STATUS_ERROR_CONTACTING_CREATE_SYSTEM)).addListener(ChannelFutureListener.CLOSE);
                }
            } else {
                if (password.length() < 5 || password.length() > 20) {
                    channel.write(new RegisterResponse(RegisterResponse.STATUS_PASSWORD_INVALID_LENGTH)).addListener(ChannelFutureListener.CLOSE);
                } else {
                    channel.write(new RegisterResponse(RegisterResponse.STATUS_PASSWORD_INVALID_CHARS)).addListener(ChannelFutureListener.CLOSE);
                }
            }
        } else {
            // invalid request
        }
    }
}
