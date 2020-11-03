package com.longhdi.controller;

import com.longhdi.entity.Video;
import com.longhdi.service.UrlMessageProducer;
import com.longhdi.service.VideoService;
import com.longhdi.validation.Url;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ViewScoped
@Named("videoCreator")
public class VideoController implements Serializable {

    @Url
    private String url;

    @Inject
    private HttpServletRequest request;

    @Inject
    private VideoService videoService;

    @Inject
    private UrlMessageProducer producer;

    private LazyDataModel<Video> lazyDataModel;

    @PostConstruct
    public void init() {
        this.lazyDataModel = new LazyDataModel<>() {
            @Override
            public List<Video> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, FilterMeta> filterBy) {
                return videoService.findWithOffset(first, pageSize);
            }
        };
        this.lazyDataModel.setRowCount(videoService.countAll());
    }

    public void create() {
        String email = request.getUserPrincipal().getName();
        producer.sendMessage(Map.of("email", email, "url", url));
        setUrl("");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LazyDataModel<Video> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Video> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public VideoService getVideoService() {
        return videoService;
    }

    public void setVideoService(VideoService videoService) {
        this.videoService = videoService;
    }
}
