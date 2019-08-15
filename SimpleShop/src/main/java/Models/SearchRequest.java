package Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchRequest {
    @JsonProperty
    String search;
    @JsonProperty
    List<String> products;
    @JsonProperty
    Integer page;
    @JsonProperty
    Integer pageSize;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
