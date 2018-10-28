package kr.geun.t.app.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 응답 데이터
 *
 * @author akageun
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResData<T> {

    private T data;
    private String msg;

    public ResData(String msg) {
        this.msg = msg;
    }
}
