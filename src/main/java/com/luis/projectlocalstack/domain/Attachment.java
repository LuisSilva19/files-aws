package com.luis.projectlocalstack.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Attachment {
    private String bucket;
    private String key;
    private String fileName;
    private String contentType;
}
