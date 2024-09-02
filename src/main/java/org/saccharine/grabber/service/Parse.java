package org.saccharine.grabber.service;

import java.io.IOException;
import java.util.List;

public interface Parse<E> {
    List<E> list(String link) throws IOException;
}