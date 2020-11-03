package com.longhdi.service;

import com.longhdi.component.MayTransform;
import com.longhdi.component.video.VideoFactory;
import com.longhdi.entity.User;
import com.longhdi.entity.Video;
import com.longhdi.entity.VideoCount;
import com.longhdi.qualifier.Preview;
import com.longhdi.qualifier.Text;
import com.longhdi.qualifier.YouTube;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Stateless
public class VideoService {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    @Inject
    @YouTube
    private VideoFactory<String> videoFactory;

    @Inject
    @Preview @Text(length = 450)
    public MayTransform<String> transformDescriptionToPreview;

    public List<Video> findAll() {
        return entityManager.createNamedQuery("Video.findAll", Video.class).getResultList();
    }

    public List<Video> findWithOffset(int pageNumber, int pageSize) {
        return entityManager.createNamedQuery("Video.findAll", Video.class)
                .setFirstResult(pageNumber)
                .setMaxResults(pageSize).getResultList();
    }

    public int countAll() {
        TypedQuery<VideoCount> query = entityManager.createNamedQuery("VideoCount.countAllVideo", VideoCount.class);
        return query.getSingleResult().getValue();
    }

    public List<Video> findByUser(String userId) {
        return entityManager.createNamedQuery("Video.findByUser", Video.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public Optional<Video> create(String url, User user) {
        Optional<Video> possibleVideo = videoFactory.buildVideo(url);
        if (possibleVideo.isPresent()) {
            entityManager.merge(user);
            Video video = possibleVideo.get();
            video.setUser(user);
            video.setUrl(url);
            entityManager.persist(video);
            return Optional.of(video);
        }
        return Optional.empty();
    }

    public String makePreviewDescription(Video video) {
        Optional<String> descriptionPreview = transformDescriptionToPreview.apply(video.getDescription());
        return descriptionPreview.isPresent() ? descriptionPreview.get() : "";
    }

}
