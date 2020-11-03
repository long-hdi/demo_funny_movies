package com.longhdi.component.video;

import com.longhdi.entity.Video;

import java.util.Optional;

@FunctionalInterface
public interface VideoFactory<T> {

    Optional<Video> buildVideo(T t);

}
