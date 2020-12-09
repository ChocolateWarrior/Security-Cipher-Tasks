package com.security.util;

import org.apache.commons.codec.DecoderException;

//
public interface ExampleCommand {
    void execute() throws DecoderException, InterruptedException;
}
