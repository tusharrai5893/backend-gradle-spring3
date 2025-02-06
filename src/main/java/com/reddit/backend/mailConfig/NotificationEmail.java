package com.reddit.backend.mailConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NotificationEmail {

    private String subject;
    private String recipient;
    private String body;
    private String link;
    private String msg;
}
