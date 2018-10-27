var CmnUtils = {
    /**
     * 페이징처리
     *
     * @param pagenation2 : 전체 데이터 수
     * @param callFuncNm : 호출 함수명
     *
     * @returns {string}
     */
    pagination: function (param, callFuncNm) {
        var rtnHtml = "";

        if (param.pageNumber > param.pageBlockSize) {
            rtnHtml += '<li class="page-item"><a class="page-link"  onclick="' + callFuncNm + '(' + param.firstPageNo + ');">&laquo;</a></li>';
            rtnHtml += '<li class="page-item"><a class="page-link"  onclick="' + callFuncNm + '(' + param.preBlockPageNo + ');">&lt;</a></li>';
        }

        for (var i = param.firstBlockPageNo; i < param.lastBlockPageNo; i++) {
            if (i === param.pageNumber) {
                rtnHtml += '<li class="page-item active"><a class="page-link"  >' + i + '</a></li>';
            } else {
                rtnHtml += '<li class="page-item"><a class="page-link"  onclick="' + callFuncNm + '(' + i + ');">' + i + '</a></li>';
            }

            if (i === param.totalPageCount) {
                break;
            }
        }

        if (param.totalPages > param.nextBlockPageNo) {
            rtnHtml += '<li class="page-item"><a class="page-link"  onclick="' + callFuncNm + '(' + (param.nextBlockPageNo + 1) + ');">&gt;</a></li>';
            rtnHtml += '<li class="page-item"><a class="page-link"  onclick="' + callFuncNm + '(' + param.lastPageNo + ');">&raquo;</a></li>';
        }

        return rtnHtml;
    },

    /**
     * 모달창 열기
     *
     * @param modalId
     */
    openModal: function (modalId) {
        $("#" + modalId).modal({backdrop: false});
    },

    closeModal: function(modalId){
        $("#" + modalId).modal('hide')
    }
}
