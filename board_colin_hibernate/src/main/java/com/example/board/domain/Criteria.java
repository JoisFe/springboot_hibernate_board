package com.example.board.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Criteria {
    private int page;
    private int perPageNum;

    public int getPageStart() {
        return (this.page - 1) * perPageNum;
    }

    public Criteria() {
        this.page = 1;
        this.perPageNum = 10;
    }


    public void setPage(int page) {
        if (page <= 0) {
            this.page = 1;

        } else {
            this.page = page;
        }
    }


}
