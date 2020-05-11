package com.codegym.demo.controller;

import com.codegym.demo.model.User;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final Messenger messenger;


    @Autowired
    public WebhookController(final Messenger messenger) {
        this.messenger = messenger;
    }

    @GetMapping("/webhook")
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


    @PostMapping("/webhook")
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
                sendTextMessageUser(senderId, "Tôi là bot chỉ có thể xử lý tin nhắn văn bản.");
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
                    sendTextMessageUser(senderId, "Ban da dung cuoc tro chuyen");
                    userOptional.get().setEnable(false);
                }
            }

//            else {
//                if (userOptional.isPresent()) {
//                    if (!userOptional.get().isEnable()) {
//                        userOptional.get().setEnable(true);
//                    } else {
//                        if (messageText.equalsIgnoreCase("1")) {
//                            Script scriptMenu = scriptService.findScriptByContent(scriptContent.get(0));
//                            scriptContent.clear();
//                            scriptMenu(senderId, scriptMenu, scriptContent);
//                            handleTextMessageEvent(event);
//                        } else if (messageText.equalsIgnoreCase("2")) {
//                            Script scriptMenu = scriptService.findScriptByContent(scriptContent.get(1));
//                            scriptContent.clear();
//                            scriptMenu(senderId, scriptMenu, scriptContent);
//                            ;
//                            handleTextMessageEvent(event);
//                        }
//                    }
//                }
//            }
        } else {
            User user = new User();
            user.setId(id);
            user.setEnable(true);
            userService.save(user);
            sendTextMessageUser(senderId, "Xin chao thanh vien moi");
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
}