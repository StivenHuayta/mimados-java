package pe.pucp.progra3.mimados.model.dto.response;

import java.io.Serializable;
import java.util.List;

public class PageResultDTO<T> implements Serializable {

    private int totalCount;
    private List<T> page;

    public PageResultDTO() {}

    public PageResultDTO(int totalCount, List<T> page) {
        this.totalCount = totalCount;
        this.page = page;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getPage() {
        return page;
    }

    public void setPage(List<T> page) {
        this.page = page;
    }
}
