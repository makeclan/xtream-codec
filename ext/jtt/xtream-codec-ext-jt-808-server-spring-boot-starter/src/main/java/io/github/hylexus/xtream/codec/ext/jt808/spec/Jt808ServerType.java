package io.github.hylexus.xtream.codec.ext.jt808.spec;

public enum Jt808ServerType {
    INSTRUCTION_SERVER("指令服务器"),
    ATTACHMENT_SERVER("附件服务器"),
    ;
    private final String description;

    Jt808ServerType(String description) {
        this.description = description;
    }
}
