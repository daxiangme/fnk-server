package com.fnk.app.system.biz.service.messages.mail;

import cn.hutool.core.util.StrUtil;
import com.fnk.app.system.biz.dal.entity.SystemMailAccountDO;
import com.fnk.common.bean.exception.LogicException;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

/**
 * 基于 JDK Socket 的 SMTP 邮件发送适配器。
 *
 * @author Enigma
 */
@Component
public class SmtpMailDeliveryClient implements MailDeliveryClient {
    private static final int TIMEOUT_MS = 15000;

    @Override
    public void send(MailSendContext context) {
        try (SmtpSession session = connect(context.account())) {
            session.command("MAIL FROM:<" + context.account().getMail() + ">", "250");
            session.command("RCPT TO:<" + context.toMail() + ">", "250", "251");
            session.command("DATA", "354");
            session.writeData(buildMessage(context));
            session.expect("250");
            session.command("QUIT", "221");
        } catch (IOException ex) {
            throw new LogicException("SMTP 发送失败：" + ex.getMessage());
        }
    }

    private SmtpSession connect(SystemMailAccountDO account) throws IOException {
        Socket socket = openSocket(account);
        SmtpSession session = new SmtpSession(socket, account.getHost());
        session.expect("220");
        session.command("EHLO localhost", "250");
        if (Boolean.TRUE.equals(account.getStarttlsEnable()) && !Boolean.TRUE.equals(account.getSslEnable())) {
            session.command("STARTTLS", "220");
            session.upgradeTls();
            session.command("EHLO localhost", "250");
        }
        if (StrUtil.isNotBlank(account.getUsername())) {
            session.command("AUTH LOGIN", "334");
            session.command(base64(account.getUsername()), "334");
            session.command(base64(StrUtil.nullToEmpty(account.getPassword())), "235");
        }
        return session;
    }

    private Socket openSocket(SystemMailAccountDO account) throws IOException {
        Socket socket;
        if (Boolean.TRUE.equals(account.getSslEnable())) {
            socket = SSLSocketFactory.getDefault().createSocket(account.getHost(), account.getPort());
        } else {
            socket = new Socket();
            socket.connect(new InetSocketAddress(account.getHost(), account.getPort()), TIMEOUT_MS);
        }
        socket.setSoTimeout(TIMEOUT_MS);
        if (socket instanceof SSLSocket sslSocket) {
            sslSocket.startHandshake();
        }
        return socket;
    }

    private String buildMessage(MailSendContext context) {
        String from = formatFrom(context.account().getMail(), context.fromName());
        String body = Base64.getMimeEncoder(76, "\r\n".getBytes(StandardCharsets.UTF_8))
                .encodeToString(context.content().getBytes(StandardCharsets.UTF_8));
        String message = "From: " + from + "\r\n"
                + "To: " + context.toMail() + "\r\n"
                + "Subject: " + encodeHeader(context.title()) + "\r\n"
                + "Date: " + DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()) + "\r\n"
                + "MIME-Version: 1.0\r\n"
                + "Content-Type: text/html; charset=UTF-8\r\n"
                + "Content-Transfer-Encoding: base64\r\n"
                + "\r\n"
                + body;
        return message.replace("\r\n.", "\r\n..") + "\r\n.\r\n";
    }

    private String formatFrom(String mail, String fromName) {
        if (StrUtil.isBlank(fromName)) {
            return mail;
        }
        return encodeHeader(fromName) + " <" + mail + ">";
    }

    private String encodeHeader(String value) {
        return "=?UTF-8?B?" + base64(value) + "?=";
    }

    private String base64(String value) {
        return Base64.getEncoder().encodeToString(StrUtil.nullToEmpty(value).getBytes(StandardCharsets.UTF_8));
    }

    private static class SmtpSession implements Closeable {
        private Socket socket;
        private final String host;
        private BufferedReader reader;
        private BufferedWriter writer;

        SmtpSession(Socket socket, String host) throws IOException {
            this.socket = socket;
            this.host = host;
            refreshStreams();
        }

        void upgradeTls() throws IOException {
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = socketFactory.createSocket(socket, host, socket.getPort(), true);
            ((SSLSocket) socket).startHandshake();
            socket.setSoTimeout(TIMEOUT_MS);
            refreshStreams();
        }

        void command(String command, String... expectedCodes) throws IOException {
            writer.write(command);
            writer.write("\r\n");
            writer.flush();
            expect(expectedCodes);
        }

        void writeData(String data) throws IOException {
            writer.write(data);
            writer.flush();
        }

        void expect(String... expectedCodes) throws IOException {
            String response = readResponse();
            String code = response.length() >= 3 ? response.substring(0, 3) : "";
            if (!List.of(expectedCodes).contains(code)) {
                throw new IOException("SMTP 响应异常：" + response);
            }
        }

        private String readResponse() throws IOException {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (!response.isEmpty()) {
                    response.append(" | ");
                }
                response.append(line);
                if (line.length() < 4 || line.charAt(3) != '-') {
                    break;
                }
            }
            if (response.isEmpty()) {
                throw new IOException("SMTP 连接无响应");
            }
            return response.toString();
        }

        private void refreshStreams() throws IOException {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }
    }
}
