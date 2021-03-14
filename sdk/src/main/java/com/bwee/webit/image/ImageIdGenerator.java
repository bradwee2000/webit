package com.bwee.webit.image;

import com.bwee.webit.service.IdGenerator;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Base64;

@Component
public class ImageIdGenerator implements IdGenerator<byte[]> {

    private final MessageDigest md;

    public ImageIdGenerator(final MessageDigest md) {
        this.md = md;
    }

    @Override
    public String generateId(byte[] model) {
        final byte[] hash = md.digest(model);
        return new String(Base64.getUrlEncoder().encode(hash));
    }
}
