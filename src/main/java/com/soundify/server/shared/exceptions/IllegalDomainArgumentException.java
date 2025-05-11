package com.soundify.server.shared.exceptions;

public class IllegalDomainArgumentException extends DomainException {
    public IllegalDomainArgumentException(String message) {
        super(message);
    }
}
