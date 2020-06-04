package com.codegym.demo.controller;

import com.codegym.demo.model.*;
import com.codegym.demo.service.city.ICityService;
import com.codegym.demo.service.cron.ICronJobTaskService;
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
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

import static com.codegym.demo.StaticVariable.*;
import static com.github.messenger4j.Messenger.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.tomcat.util.file.ConfigFileLoader.getInputStream;

@RestController
@CrossOrigin("*")
@RequestMapping("/webhook")
public class WebhookController {
    @Autowired
    private UserService userService;

    @Autowired
    private ITemperatureService temperatureService;

    @Autowired
    private ICronJobTaskService cronJobTaskService;

    @Autowired
    private ICityService cityService;

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final Messenger messenger;

    private Long cronJobId;

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
        Optional<CronJobTask> cronJobTaskOptional1 = cronJobTaskService.findById(1L);
        Optional<CronJobTask> cronJobTaskOptional2 = cronJobTaskService.findById(2L);
        Optional<CronJobTask> cronJobTaskOptional3 = cronJobTaskService.findById(3L);
        Optional<CronJobTask> cronJobTaskOptional4 = cronJobTaskService.findById(4L);
        Optional<CronJobTask> cronJobTaskOptional5 = cronJobTaskService.findById(5L);
        Optional<CronJobTask> cronJobTaskOptional6 = cronJobTaskService.findById(6L);
        List<CronJobTask> cronJobTaskList = (List<CronJobTask>) cronJobTaskService.findAll();
        Long id = Long.parseLong(senderId);
        if (userService.findById(id).isPresent()) {
            Optional<User> userOptional = userService.findById(id);
            if (userOptional.isPresent()) {
                if (messageText.equalsIgnoreCase("hủy")) {
                    userOptional.get().setStatus(false);
                    userService.save(userOptional.get());
                    sendTextMessageUser(senderId, "Bạn đã dừng dịch vụ nhận thông tin thời tiết định kỳ");
                    for (CronJobTask cronJobTask : cronJobTaskList) {
                        cronJobTask.setStatus(false);
                        cronJobTaskService.save(cronJobTask);
                    }
                } else {
                    if (!userOptional.get().isStatus()) {
                        userOptional.get().setStatus(true);
                        userService.save(userOptional.get());
                        sendTextMessageUser(senderId, "Xin chào");
                        sendTextMessageUser(senderId, "Bạn đã đăng ký dịch vụ cập nhật thông tin thời tiết");
                    } else {
                        sendTextMessageUser(senderId, "Xin chào");
                        sendTextMessageUser(senderId, "Bạn đã đăng ký dịch vụ này.");
                        sendTextMessageUser(senderId, "Nếu muốn hủy dịch vụ vui lòng gõ hủy.");
                    }
                    sendTextMessageUser(senderId, "Bạn có thể chọn thời gian để nhận thông tin thời tiết định kỳ trả lời như sau (mặc định là 1 phút/lần):" +
                            "\n10 phút" +
                            "\n1 giờ" +
                            "\n3 giờ" +
                            "\n6 giờ" +
                            "\n3 ngày" +
                            "\n1 tuần");
                }
            }
        } else {
            User user = new User();
            user.setId(id);
            user.setStatus(true);
            userService.save(user);
            sendTextMessageUser(senderId, "Xin chào, bạn đã đăng ký nhận thông tin thời tiết định kỳ thời gian nhận thông tin mặc định là 1 phút/lần");
            sendTextMessageUser(senderId, "Ngoài ra bạn có thể chọn thời gian để nhận thông tin thời tiết định kỳ bằng cách trả lời như sau:" +
                    "\n10 phút" +
                    "\n1 giờ" +
                    "\n3 giờ" +
                    "\n6 giờ" +
                    "\n3 ngày" +
                    "\n1 tuần");
            sendTextMessageUser(senderId, "Nếu bạn muốn dừng không nhận thông tin thời tiết định kỳ \nGõ hủy");
        }
        String text = "Bạn đã đăng ký nhận thông tin thời tiết định kỳ trong ";
        if (messageText.equalsIgnoreCase("10 phút")) {
            if (cronJobTaskOptional1.isPresent()) {
                cronJobTaskOptional1.get().setStatus(true);
                cronJobTaskService.save(cronJobTaskOptional1.get());
                for (CronJobTask cronJobTask : cronJobTaskList) {
                    if (!cronJobTask.getId().equals(cronJobTaskOptional1.get().getId())) {
                        cronJobTask.setStatus(false);
                    }
                }
                text += "10 phút";
            }
        } else if (messageText.equalsIgnoreCase("1 giờ")) {
            if (cronJobTaskOptional2.isPresent()) {
                cronJobTaskOptional2.get().setStatus(true);
                cronJobTaskService.save(cronJobTaskOptional2.get());
                for (CronJobTask cronJobTask : cronJobTaskList) {
                    if (!cronJobTask.getId().equals(cronJobTaskOptional2.get().getId())) {
                        cronJobTask.setStatus(false);
                    }
                }
                text += "1 giờ";
            }
        } else if (messageText.equalsIgnoreCase("3 giờ")) {
            if (cronJobTaskOptional3.isPresent()) {
                cronJobTaskOptional3.get().setStatus(true);
                cronJobTaskService.save(cronJobTaskOptional3.get());
                for (CronJobTask cronJobTask : cronJobTaskList) {
                    if (!cronJobTask.getId().equals(cronJobTaskOptional3.get().getId())) {
                        cronJobTask.setStatus(false);
                    }
                }
                text += "3 giờ";
            }
        } else if (messageText.equalsIgnoreCase("6 giờ")) {
            if (cronJobTaskOptional4.isPresent()) {
                cronJobTaskOptional4.get().setStatus(true);
                cronJobTaskService.save(cronJobTaskOptional4.get());
                for (CronJobTask cronJobTask : cronJobTaskList) {
                    if (!cronJobTask.getId().equals(cronJobTaskOptional4.get().getId())) {
                        cronJobTask.setStatus(false);
                    }
                }
                text += "6 giờ";
            }
        } else if (messageText.equalsIgnoreCase("3 ngày")) {
            if (cronJobTaskOptional5.isPresent()) {
                cronJobTaskOptional5.get().setStatus(true);
                cronJobTaskService.save(cronJobTaskOptional5.get());
                for (CronJobTask cronJobTask : cronJobTaskList) {
                    if (!cronJobTask.getId().equals(cronJobTaskOptional5.get().getId())) {
                        cronJobTask.setStatus(false);
                    }
                }
                text += "3 ngày";
            }
        } else if (messageText.equalsIgnoreCase("1 tuần")) {
            if (cronJobTaskOptional6.isPresent()) {
                cronJobTaskOptional6.get().setStatus(true);
                cronJobTaskService.save(cronJobTaskOptional6.get());
                for (CronJobTask cronJobTask : cronJobTaskList) {
                    if (!cronJobTask.getId().equals(cronJobTaskOptional6.get().getId())) {
                        cronJobTask.setStatus(false);
                    }
                }
                text += "1 tuần";
            }
        } else {
            text += "1 phút";
        }
        sendTextMessageUser(senderId, text + "/lần");
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

    @Bean
    public Long getCronValue() {
        Long time;
        Optional<CronJobTask> cronJobTaskOptional1 = cronJobTaskService.findById(1L);
        Optional<CronJobTask> cronJobTaskOptional2 = cronJobTaskService.findById(2L);
        Optional<CronJobTask> cronJobTaskOptional3 = cronJobTaskService.findById(3L);
        Optional<CronJobTask> cronJobTaskOptional4 = cronJobTaskService.findById(4L);
        Optional<CronJobTask> cronJobTaskOptional5 = cronJobTaskService.findById(5L);
        Optional<CronJobTask> cronJobTaskOptional6 = cronJobTaskService.findById(6L);
        if (cronJobTaskOptional1.isPresent() && cronJobTaskOptional1.get().isStatus()) {
            time = cronJobTaskOptional1.get().getTime();
        } else if (cronJobTaskOptional2.isPresent() && cronJobTaskOptional2.get().isStatus()) {
            time = cronJobTaskOptional2.get().getTime();
        } else if (cronJobTaskOptional3.isPresent() && cronJobTaskOptional3.get().isStatus()) {
            time = cronJobTaskOptional3.get().getTime();
        } else if (cronJobTaskOptional4.isPresent() && cronJobTaskOptional4.get().isStatus()) {
            time = cronJobTaskOptional4.get().getTime();
        } else if (cronJobTaskOptional5.isPresent() && cronJobTaskOptional5.get().isStatus()) {
            time = cronJobTaskOptional5.get().getTime();
        } else if (cronJobTaskOptional6.isPresent() && cronJobTaskOptional6.get().isStatus()) {
            time = cronJobTaskOptional6.get().getTime();
        } else {
            time = 60000L;
        }
        return time;
    }

    @Scheduled(fixedDelayString = "#{@getCronValue}", zone = "Asia/Saigon")
    private void sendTemperatureMessage() {
        if (cronJobId != null) {
            ArrayList<User> users = (ArrayList<User>) userService.findAllByStatusIsTrue();
            Temperatures currentHNTemperature = crawlerData(URL_CRAWL_HN, PATTERN_TEMPERATURE, PATTERN_CITY_HN);
            Temperatures currentDNTemperature = crawlerData(URL_CRAWL_DN, PATTERN_TEMPERATURE, PATTERN_CITY_DN);
            Temperatures currentHCMTemperature = crawlerData(URL_CRAWL_HCM, PATTERN_TEMPERATURE, PATTERN_CITY_HCM);
            Optional<Cities> citiesOptional = cityService.findById(currentHNTemperature.getCities().getId());
            if (citiesOptional.isPresent()) {
                String city = citiesOptional.get().getName();
                for (User user : users) {
                    sendTextMessageUser(user.getId().toString(), "Thông tin thời tiết được cập nhật bằng crawl");
                    sendTextMessageUser(user.getId().toString(), "Thời tiết hiện tại thành phố "
                            + city + " là " + currentHNTemperature.getTemperature() + " độ C");
                }
            }
            citiesOptional = cityService.findById(currentDNTemperature.getCities().getId());
            if (citiesOptional.isPresent()) {
                String city = citiesOptional.get().getName();
                for (User user : users) {
                    sendTextMessageUser(user.getId().toString(), "Thời tiết hiện tại thành phố "
                            + city + " là " + currentDNTemperature.getTemperature() + " độ C");
                }
            }
            citiesOptional = cityService.findById(currentHCMTemperature.getCities().getId());
            if (citiesOptional.isPresent()) {
                String city = citiesOptional.get().getName();
                for (User user : users) {
                    sendTextMessageUser(user.getId().toString(), "Thời tiết hiện tại thành phố "
                            + city + " là " + currentHCMTemperature.getTemperature() + " độ C");
                }
            }
            try {
                currentHNTemperature = callAPI(API_URL_HN);
                currentDNTemperature = callAPI(API_URL_DN);
                currentHCMTemperature = callAPI(API_URL_HCM);
            } catch (IOException e) {
                e.printStackTrace();
            }
            citiesOptional = cityService.findById(currentHNTemperature.getCities().getId());
            if (citiesOptional.isPresent()) {
                String city = citiesOptional.get().getName();
                for (User user : users) {
                    sendTextMessageUser(user.getId().toString(), "Thông tin thời tiết được cập nhật bằng cách gọi api");
                    sendTextMessageUser(user.getId().toString(), "Thời tiết hiện tại thành phố "
                            + city + " là " + currentHNTemperature.getTemperature() + " độ C");
                }
            }
            citiesOptional = cityService.findById(currentDNTemperature.getCities().getId());
            if (citiesOptional.isPresent()) {
                String city = citiesOptional.get().getName();
                for (User user : users) {
                    sendTextMessageUser(user.getId().toString(), "Thời tiết hiện tại thành phố "
                            + city + " là " + currentDNTemperature.getTemperature() + " độ C");
                }
            }
            citiesOptional = cityService.findById(currentHCMTemperature.getCities().getId());
            if (citiesOptional.isPresent()) {
                String city = citiesOptional.get().getName();
                for (User user : users) {
                    sendTextMessageUser(user.getId().toString(), "Thời tiết hiện tại thành phố "
                            + city + " là " + currentHCMTemperature.getTemperature() + " độ C");
                }
            }
        }
    }

    private Temperatures crawlerData(String urlCrawl, String patternTemperature, String patternCity) {
        URL url = null;
        Scanner scanner = null;
        Temperatures temperatures = new Temperatures();
        Cities cities = new Cities();
        try {
            url = new URL(urlCrawl);
            scanner = new Scanner(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter("\\\\Z");
        String content = scanner.next();
        scanner.close();
        content = content.replace("\\\\R", "");
        String temperature = getTemperature(content, patternTemperature);
        String city = getCity(content, patternCity);
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

    private String getTemperature(String content, String patternTemperature) {
        Pattern temperature = Pattern.compile(patternTemperature);
        Matcher result = temperature.matcher(content);
        String temperatures = "";
        while (result.find()) {
            temperatures = result.group(1);
        }
        return temperatures;
    }

    private String getCity(String content, String patternCity) {
        Pattern city = Pattern.compile(patternCity);
        String cities = "";
        Matcher result = city.matcher(content);
        while (result.find()) {
            cities = result.group(1);
        }
        return cities;
    }

    Temperatures callAPI(String apiUrl) throws IOException {
        return getLocalWeatherData(getInputStream(apiUrl));
    }

    Temperatures getLocalWeatherData(InputStream is) {
        Temperatures temperatures = new Temperatures();
        Data data = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            data = (Data) jaxbUnmarshaller.unmarshal(is);

            if (true) {
                CurrentCondition currentCondition = data.getCurrent_condition();
                Request request = data.getRequest();
                String cityName = request.getQuery().split(",")[0].trim();
                Optional<Cities> citiesOptional = cityService.findByName(cityName);
                Cities cities = new Cities();
                if (!citiesOptional.isPresent()) {
                    cities.setName(cityName);
                    cityService.save(cities);
                } else {
                    cities.setId(citiesOptional.get().getId());
                }
                temperatures.setTemperature(currentCondition.getTemp_C());
                temperatures.setCities(cities);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return temperatures;
    }
}