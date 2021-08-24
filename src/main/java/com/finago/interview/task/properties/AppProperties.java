package com.finago.interview.task.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
public class AppProperties {
    @Value("${dataIn}")
    private String dataIn;
    @Value("${dataOut}")
    private String dataOut;
    @Value("${dataError}")
    private String dataError;
    @Value("${dataArchive}")
    private String dataArchive;
}
