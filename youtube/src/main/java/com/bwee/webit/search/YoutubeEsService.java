package com.bwee.webit.search;

import com.bwee.webit.exception.YoutubeContentNotFoundException;
import com.bwee.webit.model.YoutubeVideo;
import com.bwee.webit.search.document.YoutubeDocument;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Service
public class YoutubeEsService {

    private static final String[] SEARCH_FIELD_NAMES = {"title", "description", "author", "categories", "tags"};
    @Autowired
    private YoutubeEsRepository youtubeEsRepository;

    @Autowired
    private ElasticsearchOperations es;

    public boolean isExist(final String id) {
        return youtubeEsRepository.existsById(id);
    }

    public YoutubeVideo save(final YoutubeVideo video) {
        return youtubeEsRepository.save(new YoutubeDocument(video)).toModel();
    }

    public List<YoutubeVideo> saveAll(final YoutubeVideo video1, final YoutubeVideo video2) {
        return saveAll(Arrays.asList(video1, video2));
    }

    public List<YoutubeVideo> saveAll(final YoutubeVideo video1, final YoutubeVideo video2, final YoutubeVideo video3) {
        return saveAll(Arrays.asList(video1, video2, video3));
    }

    public List<YoutubeVideo> saveAll(final Collection<YoutubeVideo> videos) {
        final List<YoutubeDocument> docs = videos.stream().map(v -> new YoutubeDocument(v)).collect(Collectors.toList());
        return StreamSupport.stream(youtubeEsRepository.saveAll(docs).spliterator(), false)
                .map(d -> d.toModel())
                .collect(Collectors.toList());
    }

    public List<YoutubeVideo> search(final String keywords, Pageable pageable) {
        final Query query = new NativeSearchQuery(multiMatchQuery(keywords, SEARCH_FIELD_NAMES).fuzziness(Fuzziness.ONE))
                .setPageable(pageable);

        return es.search(query, YoutubeDocument.class).stream()
                .sorted(Comparator.comparing(hit -> hit.getScore()))
                .map(m -> m.getContent().toModel())
                .collect(Collectors.toList());
    }

    public void delete(final YoutubeVideo video) {
        youtubeEsRepository.delete(new YoutubeDocument(video));
    }

    public void deleteById(final String id) {
        youtubeEsRepository.deleteById(id);
    }

    public Optional<YoutubeVideo> findById(final String id) {
        return youtubeEsRepository.findById(id).map(v -> v.toModel());
    }

    public YoutubeVideo findByIdStrict(final String id) {
        return findById(id).orElseThrow(() -> new YoutubeContentNotFoundException(id));
    }

    public Page<YoutubeVideo> findAll(final Pageable pageable) {
        return youtubeEsRepository.findAll(pageable).map(v -> v.toModel());
    }
}
