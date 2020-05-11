package com.codegym.demo.controller;

import com.codegym.demo.model.Cities;
import com.codegym.demo.model.Temperatures;
import com.codegym.demo.model.User;
import com.codegym.demo.service.city.ICityService;
import com.codegym.demo.service.temperature.ITemperatureService;
import com.codegym.demo.service.user.UserService;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.messenger4j.Messenger.*;
import static com.github.messenger4j.Messenger.CHALLENGE_REQUEST_PARAM_NAME;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@RestController
@CrossOrigin("*")
@RequestMapping("/webhook")
public class WebhookController {
    @Autowired
    private UserService userService;

    @Autowired
    private ITemperatureService temperatureService;

    @Autowired
    private ICityService cityService;

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final Messenger messenger;


    @Autowired
    public WebhookController(final Messenger messenger) {
        this.messenger = messenger;
    }

    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
                                                @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken, @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge) {
        logger.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyToken, challenge);
        try {
            this.messenger.verifyWebhook(mode, verifyToken);
            return ResponseEntity.ok(challenge);
        } catch (MessengerVerificationException e) {
            logger.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) throws MessengerVerificationException {
        this.messenger.onReceiveEvents(payload, of(signature), event -> {
            if (event.isTextMessageEvent()) {
                try {
                    logger.info("0");
                    handleTextMessageEvent(event.asTextMessageEvent());
                    logger.info("1");
                } catch (MessengerApiException e) {
                    logger.info("2");
                    e.printStackTrace();
                } catch (MessengerIOException e) {
                    logger.info("3");
                    e.printStackTrace();
                }
            } else {
                String senderId = event.senderId();
                sendTextMessageUser(senderId, "Bot chỉ có thể xử lý tin nhắn văn bản.");
            }
        });
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private void handleTextMessageEvent(TextMessageEvent event) throws MessengerApiException, MessengerIOException {
        final String messageText = event.text();
        final String senderId = event.senderId();
        Long id = Long.parseLong(senderId);
        if (userService.findById(id).isPresent()) {
            Optional<User> userOptional = userService.findById(id);
            if (userOptional.isPresent()) {
                if (messageText.equalsIgnoreCase("stop")) {
                    sendTextMessageUser(senderId, "Bạn đã dừng dịch vụ nhận thông tin thời tiết định kỳ");
                    userOptional.get().setEnable(false);
                } else {
                    if (!userOptional.get().isEnable()) {
                        userOptional.get().setEnable(true);
                        sendTextMessageUser(senderId, "Xin chào, bạn đã đăng ký dịch vụ nhận thông tin thời tiết định kỳ");
                    } else {
                        sendTextMessageUser(senderId, "Xin chào. Bạn đã đăng ký dịch vụ này");
                    }
                }
            }
        } else {
            User user = new User();
            user.setId(id);
            user.setEnable(true);
            userService.save(user);
            sendTextMessageUser(senderId, "Xin chào, bạn đã đăng ký nhận thông tin thời tiết định kỳ");
            sendTextMessageUser(senderId, "Chúng tôi sẽ gửi thông tin thời tiết cho bạn 3 giờ/lần");
            sendTextMessageUser(senderId, "Nếu bạn muốn dừng không nhận thông tin thời tiết định kỳ \nGõ stop");
        }

    }

    private void sendTextMessageUser(String idSender, String text) {
        try {
            final IdRecipient recipient = IdRecipient.create(idSender);
            final NotificationType notificationType = NotificationType.REGULAR;
            final String metadata = "DEVELOPER_DEFINED_METADATA";

            final TextMessage textMessage = TextMessage.create(text, empty(), of(metadata));
            final MessagePayload messagePayload = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessage,
                    of(notificationType), empty());
            this.messenger.send(messagePayload);
        } catch (MessengerApiException | MessengerIOException e) {
            handleSendException(e);
        }
    }

    private void handleSendException(Exception e) {
        logger.error("Message could not be sent. An unexpected error occurred.", e);
    }

    @Scheduled(cron = "*/10 * * * * *", zone = "Asia/Saigon")
    private Temperatures crawlerData() {
        URL url = null;
        Scanner scanner = null;
        Temperatures temperatures = new Temperatures();
        Cities cities = new Cities();
        try {
            url = new URL("https://forecast.weather.gov/MapClick.php?lat=37.7772&lon=-122.4168&fbclid=IwAR0vy1obwdR8YYh-o_R1Nmh0_lNpXzaDv1XSKfizhF1fIGASa3_TG_Mi43g#.XrWB7BMzb_T");
            scanner = new Scanner(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter("\\\\Z");
        String content = scanner.next();
        scanner.close();
        content = content.replace("\\\\R", "");
        String temperature = getTemperature(content);
        String city = getCity(content);
        Optional<Cities> citiesOptional = cityService.findByName(city);
        if (!citiesOptional.isPresent()) {
            cities.setName(city);
            cityService.save(cities);
        } else {
            cities.setId(citiesOptional.get().getId());
        }
        temperatures.setCities(cities);
        temperatures.setTemperature(temperature);
        return temperatureService.save(temperatures);
    }

    private String getTemperature(String content) {
        Pattern temperature = Pattern.compile("class=\"myforecast-current-sm\">(.*?)&deg;C</p>");
        Matcher result = temperature.matcher(content);
        String temperatures = "";
        while (result.find()) {
            temperatures = result.group(1);
        }
        return temperatures;
    }

    private String getCity(String content) {
        Pattern city = Pattern.compile("href=\"https://www.weather.gov/mtr\">(.*?)</a>");
        String cities = "";
        Matcher result = city.matcher(content);
        while (result.find()) {
            cities = result.group(1);
        }
        return cities;
    }
}