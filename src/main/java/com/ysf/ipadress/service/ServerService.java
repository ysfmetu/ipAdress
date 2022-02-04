package com.ysf.ipadress.service;

import com.ysf.ipadress.model.Server;

import java.io.IOException;
import java.util.Collection;

public interface ServerService {
    Server create(Server server);
    Collection<Server> list(int limit);
    Server get(long id);
    Server update(Server server);
    Boolean delete(Long id);
    Server ping(String ipAddress) throws IOException;
    Server pasif(long id);

    Server[] servers(Long id);
}
