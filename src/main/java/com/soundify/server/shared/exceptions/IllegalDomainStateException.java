package com.soundify.server.shared.exceptions;

public class IllegalDomainStateException extends DomainException {
    public IllegalDomainStateException(String message) {
        super(message);
    }
}
