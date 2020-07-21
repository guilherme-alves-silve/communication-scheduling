package br.com.communication.scheduling.domain.entity;

public enum StatusMessage {

    UNSENT(0), SENT(1);

    private final int value;

    StatusMessage(int value) {
        this.value = value;
    }
}
