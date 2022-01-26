package com.example.board.domain;

import lombok.Getter;

@Getter
public class Paging {
    private final Long totalCount;
    private final int displayPageNum = 10;

    private final int startPage;
    private int endPage;
    private final boolean prev;
    private final boolean next;

    private final Criteria cri;

    public Paging(Criteria cri, Long totalCount) {
        this.cri = cri;
        this.totalCount = totalCount;

        endPage = (int) (Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);

        startPage = (endPage - displayPageNum) + 1;


        int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));

        if (endPage > tempEndPage) {
            endPage = tempEndPage;
        }

        prev = startPage != 1;

        next = endPage * cri.getPerPageNum() < totalCount;
    }

}
