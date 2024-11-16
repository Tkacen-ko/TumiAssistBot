package com.tkachenko.BasicTelegramBot.service.tg.respondent.utils;

import java.util.LinkedList;

public class LimitedSizeMessageList <E> extends LinkedList<E> {
    private final int maxSize;

    public LimitedSizeMessageList(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        if (size() >= maxSize) {
            removeLast();
        }
        addFirst(e);
        return true;
    }
}
