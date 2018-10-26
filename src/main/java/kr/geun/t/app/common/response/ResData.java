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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResData<T> {

    private T data;
    private String msg;
}
