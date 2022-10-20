package com.rpi.alexandria.config;
package com.SpringBootEmail.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailConfig {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
