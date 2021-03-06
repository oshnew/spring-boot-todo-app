package kr.geun.t.app.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 응답 데이터
 *
 * @author akageun
 */
@Getter
@AllArgsConstructor
public class ResData<T> {

	private Boolean result;

	private T data;
	private String msg;

	public ResData(String msg) {
		this.msg = msg;
	}

	public ResData(Boolean result, String msg) {
		this.result = result;
		this.msg = msg;

	}

}
