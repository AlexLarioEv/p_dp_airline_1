package app.services.interfaces;

import app.entities.search.Search;
import app.entities.search.SearchResult;
import app.repositories.SearchResultProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    SearchResult saveSearch(Search search);
    Search findSearchById(long id);
    void saveSearchResult(SearchResult searchResult);
    Page<SearchResultProjection> findSearchResultByID(Long id, Pageable pageable);
}
