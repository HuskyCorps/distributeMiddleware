package com.xinyunkeji.bigdata.convenience.server.service.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * test
 *
 * @author Yuezejian
 * @date 2020年 09月04日 09:10:25
 */
public abstract class SayService {
    List<String> data = new ArrayList<>();

    public void say() {
        data.add(IntStream.rangeClosed(1,1000000)
        .mapToObj(__->"a")
        .collect(Collectors.joining("")) + UUID.randomUUID().toString());
    }

}